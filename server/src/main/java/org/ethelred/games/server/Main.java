package org.ethelred.games.server;

import com.google.common.annotations.VisibleForTesting;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.websocket.WsContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ethelred.games.core.Engine;
import org.ethelred.games.nuo.NuoGameDefinition;
import org.jetbrains.annotations.Nullable;
import picocli.CommandLine;
import static io.javalin.apibuilder.ApiBuilder.*;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TODO
 *
 * @author eharman
 * @since 2021-04-14
 */
@CommandLine.Command(name = "server", mixinStandardHelpOptions = true)
public class Main implements Runnable
{
    private static final Logger LOGGER = LogManager.getLogger(Main.class);
    public static final String PLAYER_ID_KEY = "playerId";

    @CommandLine.Option(names = {"-p", "--profile"})
    private String profileName = "development";

    public static void main(String[] args)
    {
        new CommandLine(new Main()).execute(args);
    }

    private final Map<ServerChannel, WsContext> channelToWs = new ConcurrentHashMap<>();

    private Javalin server;

    @Override
    public void run()
    {
        LOGGER.atInfo().log("Server starting");
        var profile = DaggerProfileLoaderFactory.create().loader().load(profileName);
        server = Javalin.create(profile::configureServer);
        var engine = DaggerGameEngineFactory.create().engine();
        engine.registerGame(new NuoGameDefinition());
        engine.registerCallback(this::onMessage);
        _attach(server, engine);
        server.start(profile.getPort());
    }

    @VisibleForTesting
    public void close()
    {
        server.stop();
    }

    private void onMessage(Engine.Channel channel, String message)
    {
        var serverChannel = new ServerChannel(channel);
        var ctx = channelToWs.get(serverChannel);
        if (ctx != null)
        {
            ctx.send(message);
        }
        else
        {
            throw new IllegalStateException("No WS available for message");
        }
    }

    private void _attach(Javalin server, Engine engine)
    {
        server.routes(() -> {
            before(ctx -> {
                if (ctx.cookieStore().get(PLAYER_ID_KEY) == null)
                {
                    ctx.cookieStore().set(PLAYER_ID_KEY, engine.newId());
                }
                ctx.attribute(PLAYER_ID_KEY, ctx.cookieStore().get(PLAYER_ID_KEY));
            });
            path("api", () -> {
                get("games", ctx -> ctx.json(engine.gameTypes()));
                post("{game}", ctx -> {
                    long playerId = getPlayerId(ctx);
                    String gameType = ctx.pathParam("game");
                    ctx.json(engine.createGame(playerId, gameType));
                });
                put("{game}/{gameId}", ctx -> {
                    long playerId = getPlayerId(ctx);
                    long gameId = ctx.pathParamAsClass("gameId", Long.class).get();
                    ctx.json(engine.joinGame(playerId, gameId));
                });
                ws("{game}/{gameId}", ws -> {
                    ws.onConnect(ctx -> {
                        var channel = new ServerChannel(
                                getPlayerId(ctx),
                                ctx.pathParamAsClass("gameId", Long.class).get(),
                                ctx.pathParam("game"));
                        channelToWs.put(channel, ctx);
                    });
                    ws.onMessage(ctx -> {
                        var channel = new ServerChannel(
                                getPlayerId(ctx),
                                ctx.pathParamAsClass("gameId", Long.class).get(),
                                ctx.pathParam("game"));
                        engine.message(channel, ctx.message());
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

    static record ServerChannel(long playerId, long gameId, String gameType) implements Engine.Channel
    {
        public ServerChannel(Engine.Channel other)
        {
            this(other.playerId(), other.gameId(), other.gameType());
        }
    }
}
