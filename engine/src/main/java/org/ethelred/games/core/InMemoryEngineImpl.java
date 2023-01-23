package org.ethelred.games.core;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Scheduler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ethelred.games.bot.BotAdapter;
import org.ethelred.games.bot.RandomPlayer;

import java.security.SecureRandom;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.LongSupplier;
import java.util.function.Supplier;

public class InMemoryEngineImpl implements Engine
{
    private static final Logger LOGGER = LogManager.getLogger();
    private final Map<String, GameDefinition<?>> gameDefinitionMap = new ConcurrentSkipListMap<>();
    private final Map<String, ActionPerformer<? super Game>> actionPerformerMap = new ConcurrentSkipListMap<>();

    private final Cache<Long, Game> gameMap = Caffeine.newBuilder()
            .expireAfterWrite(30, TimeUnit.MINUTES)
            .scheduler(Scheduler.systemScheduler())
            .build();
    private final Cache<String, Long> shortCodes = Caffeine.newBuilder()
            .expireAfterWrite(30, TimeUnit.MINUTES)
            .scheduler(Scheduler.systemScheduler())
            .build();

    private final Executor botRunner = Executors.newWorkStealingPool();
    private final Map<Long, RandomPlayer> botPlayers = new ConcurrentSkipListMap<>();
    private final PlayerManager playerManager;
    private final LongSupplier idSupplier;
    private final Supplier<String> shortCodeSupplier;

    private BiConsumer<Channel, PlayerView> messageSender;

    private final Random random = new SecureRandom();

    public InMemoryEngineImpl(PlayerManager playerManager, LongSupplier idSupplier, Supplier<String> shortCodeSupplier)
    {
        this.playerManager = playerManager;
        this.idSupplier = idSupplier;
        this.shortCodeSupplier = shortCodeSupplier;
    }

    @Override
    public List<String> gameTypes()
    {
        return List.copyOf(gameDefinitionMap.keySet());
    }

    @Override
    public PlayerView createGame(Channel channel)
    {
        var game = createInternal(channel.gameType(), channel.gameId());
        var player = playerManager.get(channel.playerId());
        game.addPlayer(player);
        return game.playerView(player);
    }

    private Game createInternal(String gameType, long gameId) {
        var definition = gameDefinitionMap.get(gameType);
        if (definition == null)
        {
            throw new IllegalArgumentException("Unknown game type " + gameType);
        }
        var shortCode = shortCodeSupplier.get();
        var game = definition.create(gameId);
        game.shortCode(shortCode);
        gameMap.put(gameId, game);
        shortCodes.put(shortCode, gameId);
        return game;
    }

    @Override
    public ChannelAndView joinGame(long playerId, String shortCode)
    {
        var normal = shortCode.toUpperCase().replace('0', 'O');
        var gameId = shortCodes.getIfPresent(normal);
        if (gameId == null) {
            throw new NoSuchElementException("Unknown short code");
        }
        var game = gameMap.getIfPresent(gameId);
        if (game == null) {
            throw new NoSuchElementException("Unknown game Id");
        }
        synchronized (game) {
            var player = playerManager.get(playerId);
            game.addPlayer(player);
            return new ChannelAndView(new Channel(gameId, game.type(), playerId), game.playerView(player));
        }
    }

    @Override
    public void registerCallback(BiConsumer<Channel, PlayerView> callback)
    {
        messageSender = callback;
    }

    public void registerGame(GameDefinition<?> gameDefinition)
    {
        gameDefinitionMap.put(gameDefinition.gameType(), gameDefinition);
        gameDefinition.actionPerformers().forEach(ap -> {
            //noinspection unchecked
            actionPerformerMap.put(ap.actionName(), (ActionPerformer<? super Game>) ap);
            if (ap instanceof EngineAccess engineAccess) {
                engineAccess.engine(this);
            }
        });
    }

    @Override
    public MessagePlayerView message(Channel channel, Action action)
    {
        var performer = actionPerformerMap.get(action.name());
        var game = gameMap.getIfPresent(channel.gameId());
        var player = playerManager.get(channel.playerId());
        synchronized (game) {
            try {
                performer.perform(game, player, action);
                game.log(player, action);
                var views = game.playerViews();
                views.forEach((p, pv) -> messageSender.accept(
                        new Channel(game.id(), game.type(), p.id()), pv
                ));
                LOGGER.info("Player '{}' {}", player.name(), action);
                return new MessagePlayerView(null, views.get(player));
            } catch (InvalidActionException e) {
                LOGGER.warn("Player '{}' INVALID {}", player.name(), action, e);
                return new MessagePlayerView("invalid action", game.playerView(player));
            } catch (PlayAgainException e) {
                return handlePlayAgain(channel, game, e.getPlayers());
            }
        }
    }

    private MessagePlayerView handlePlayAgain(Channel channel, Game game, Collection<Player> players) {
        // don't restart if the only players are bots
        if (players.stream().allMatch(player -> botPlayers.containsKey(player.id()))) {
            players.forEach(this::stopBot);
            return new MessagePlayerView(null, game.playerView(playerManager.get(channel.playerId())));
        }
        var newGame = createInternal(channel.gameType(), game.id());
        players.forEach(newGame::addPlayer);
        players.forEach(newGame::playerReady);
        players.forEach(p -> messageSender.accept(new Channel(game.id(), game.type(), p.id()), newGame.playerView(p)));
        return new MessagePlayerView(null, newGame.playerView(playerManager.get(channel.playerId())));
    }

    private void stopBot(Player player) {
        var randomPlayer = botPlayers.remove(player.id());
        if (randomPlayer != null) {
            randomPlayer.stop();
        }
    }

    @Override
    public PlayerView playerView(Channel channel) {
        var game = gameMap.getIfPresent(channel.gameId());
        synchronized (game) {
            return game.playerView(playerManager.get(channel.playerId()));
        }
    }

    @Override
    public void playerName(long playerId, String name) {
        playerManager.setName(playerId, name);
    }

    @Override
    public void addBot(Player requester, String shortCode) {
        if (botPlayers.containsKey(requester.id())) {
            // ignore, it's another bot
            return;
        }
        var playerId = idSupplier.getAsLong();
        var bot = new RandomPlayer(random, shortCode, new BotAdapter(playerId, this, playerManager));
        botPlayers.put(playerId, bot);
        botRunner.execute(bot);
    }
}
