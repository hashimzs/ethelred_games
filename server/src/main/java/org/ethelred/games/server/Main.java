package org.ethelred.games.server;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.json.JavalinJackson;
import io.javalin.websocket.WsContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ethelred.games.core.Action;
import org.ethelred.games.core.Channel;
import org.ethelred.games.core.Engine;
import org.ethelred.games.core.PlayerView;
import org.ethelred.games.nuo.NuoGameDefinition;
import picocli.CommandLine;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.LongSupplier;

import static io.javalin.apibuilder.ApiBuilder.*;

/**
 * Server application.
 *
 * @author eharman
 * @since 2021-04-14
 */
@CommandLine.Command(name = "server", mixinStandardHelpOptions = true)
public class Main implements Runnable
{
    private static final Logger LOGGER = LogManager.getLogger(Main.class);
    public static final String PLAYER_ID_KEY = "playerId";
    public static final String PLAYER_NAME_KEY = "playerName";
    @org.jetbrains.annotations.VisibleForTesting
    public GameEngineComponent engineFactory = DaggerGameEngineComponent.create();

    @CommandLine.Option(names = {"-p", "--profile"}, defaultValue = "development")
    private String profileName;

    public static void main(String[] args)
    {
        new CommandLine(new Main()).execute(args);
    }

    private final Map<Channel, WsContext> channelToWs = new ConcurrentHashMap<>();

    private ObjectMapper objectMapper;
    private LongSupplier idSupplier;

    @Override
    public void run()
    {
        LOGGER.info("Server starting");
        var profile = DaggerProfileLoaderFactory.create().loader().load(profileName);
        Javalin server = Javalin.create(javalinConfig -> {
            javalinConfig.requestLogger.http((ctx, ms) -> LOGGER.debug("Request {} {}", ctx.method(), ctx.url()));
            profile.configureServer(javalinConfig);
        });
        objectMapper = engineFactory.mapper();
        idSupplier = engineFactory.idSupplier();
        var engine = engineFactory.engine();
        for (var game :
                engineFactory.gameDefinitions()) {
            engine.registerGame(game);
        }
        engine.registerGame(new NuoGameDefinition());
        engine.registerCallback(this::onMessage);
        server.updateConfig(cfg -> cfg.jsonMapper(new JavalinJackson(objectMapper)));
        _attach(server, engine, profile);
        server.start(profile.getPort());
    }

    private void onMessage(Channel channel, PlayerView message)
    {
        var ctx = channelToWs.get(channel);
        if (ctx != null)
        {
            ctx.send(message);
        }
    }

    private void _attach(Javalin server, Engine engine, @SuppressWarnings("unused") Profile profile)
    {
        server.routes(() -> {
            before(ctx -> {
                var cookieVal = ctx.cookie(PLAYER_ID_KEY);
                if (cookieVal == null)
                {
                    var playerId = idSupplier.getAsLong();
                    ctx.attribute(PLAYER_ID_KEY, playerId);
                    ctx.cookie(PLAYER_ID_KEY, String.valueOf(playerId));
                } else {
                    ctx.attribute(PLAYER_ID_KEY, Long.parseLong(cookieVal));
                }
            });
            path("api", () -> {
                get("games", ctx -> ctx.json(engine.gameTypes()));
                post("player/name", ctx -> {
                    var name = ctx.body();
                    if (name.startsWith("\"") && name.endsWith("\"") && name.length() > 2) {
                        name = name.substring(1, name.length() - 1);
                    }
                    engine.playerName(getPlayerId(ctx), name);
                    ctx.cookie(PLAYER_NAME_KEY, URLEncoder.encode(name, StandardCharsets.UTF_8));
                    ctx.status(HttpStatus.NO_CONTENT);
                });
                post("{game}", ctx -> {
                    long playerId = getPlayerId(ctx);
                    handlePlayerName(ctx, engine, playerId);
                    String gameType = ctx.pathParam("game");
                    var channel = new Channel(idSupplier.getAsLong(), gameType, playerId);
                    ctx.json(
                            new ServerPlayerView(channel, engine.createGame(channel))
                    );
                });
                put("join/{shortCode}", ctx -> {
                    long playerId = getPlayerId(ctx);
                    handlePlayerName(ctx, engine, playerId);
                    ctx.json(new ServerPlayerView(engine.joinGame(playerId, ctx.pathParam("shortCode"))));
                });
                get("{game}/{gameId}", ctx -> {
                    var channel = new Channel(
                            ctx.pathParamAsClass("gameId", Long.class).get(),
                            ctx.pathParam("game"),
                            getPlayerId(ctx));
                    var view = engine.playerView(channel);
                    ctx.json(new ServerPlayerView(channel, view));
                });
                post("{game}/{gameId}", ctx -> {
                    var channel = new Channel(
                            ctx.pathParamAsClass("gameId", Long.class).get(),
                            ctx.pathParam("game"),
                            getPlayerId(ctx));
                    var view = engine.message(channel, ctx.bodyAsClass(Action.class));

                    ctx.json(new ServerPlayerView(channel, view.playerView(), view.message()));

                });
                ws("{game}/{gameId}", ws -> {
                    ws.onConnect(ctx -> {
                        var channel = new Channel(
                            ctx.pathParamAsClass("gameId", Long.class).get(),
                            ctx.pathParam("game"),
                            getPlayerId(ctx));
                        channelToWs.put(channel, ctx);
                    });
                    ws.onMessage(ctx -> {
                        var channel = new Channel(
                            ctx.pathParamAsClass("gameId", Long.class).get(),
                            ctx.pathParam("game"),
                            getPlayerId(ctx));
                        engine.message(channel, ctx.messageAsClass(Action.class));
                    });
                    ws.onClose(ctx -> {
                        var channel = new Channel(
                            ctx.pathParamAsClass("gameId", Long.class).get(),
                            ctx.pathParam("game"),
                            getPlayerId(ctx));
                        channelToWs.remove(channel);
                    });
                });
            });
        });
    }
    
    private long getPlayerId(WsContext ctx)
    {
        return Objects.requireNonNull(ctx.attribute(PLAYER_ID_KEY));
    }

    private long getPlayerId(Context ctx)
    {
        return Objects.requireNonNull(ctx.attribute(PLAYER_ID_KEY));
    }

    private void handlePlayerName(Context context, Engine engine, long playerId) {
        var name = context.cookie(PLAYER_NAME_KEY);
        if (name != null) {
            engine.playerName(playerId, name);
        }
    }
    @JsonInclude(JsonInclude.Include.NON_NULL)
    record ServerPlayerView(String path, PlayerView playerView, String message) {
        public ServerPlayerView(Channel channel, PlayerView playerView, String message) {
            this(String.format("/api/%s/%d", channel.gameType(), channel.gameId()), playerView, message);
        }

        public ServerPlayerView(Channel channel, PlayerView playerView) {
            this(channel, playerView, null);
        }

        public ServerPlayerView(Engine.ChannelAndView channelAndView) {
            this(channelAndView.channel(), channelAndView.view(), null);
        }
    }
}
