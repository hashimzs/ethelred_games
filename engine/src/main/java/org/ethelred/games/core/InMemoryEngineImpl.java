package org.ethelred.games.core;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.BiConsumer;
import java.util.function.LongSupplier;

public class InMemoryEngineImpl implements Engine
{
    private final Map<String, GameDefinition<?>> gameDefinitionMap = new ConcurrentSkipListMap<>();
    private final Map<String, ActionPerformer<? super Game<?>>> actionPerformerMap = new ConcurrentSkipListMap<>();
    private final LongSupplier idSupplieer;

    private final Map<Long, Game<?>> gameMap = new ConcurrentHashMap<>();
    private final PlayerManager playerManager;

    private BiConsumer<Channel, Game.PlayerView> messageSender;

    public InMemoryEngineImpl(LongSupplier idSupplier, PlayerManager playerManager)
    {
        this.idSupplieer = idSupplier;
        this.playerManager = playerManager;
    }

    @Override
    public List<String> gameTypes()
    {
        return List.copyOf(gameDefinitionMap.keySet());
    }

    @Override
    public long newId()
    {
        return idSupplieer.getAsLong();
    }

    @Override
    public Channel createGame(long playerId, String gameType)
    {
        var definition = gameDefinitionMap.get(gameType);
        if (definition == null)
        {
            throw new IllegalArgumentException("Unknown game type " + gameType);
        }
        var gameId = newId();
        var game = definition.create(gameId);
        gameMap.put(gameId, game);
        var player = playerManager.get(playerId);
        game.addPlayer(player);

        return new StandardChannel(gameId, gameType, playerId);
    }

    @Override
    public Channel joinGame(long playerId, long gameId)
    {
        var game = gameMap.get(gameId);
        var player = playerManager.get(playerId);
        game.addPlayer(player);
        var gameType = game.type();
        return new StandardChannel(gameId, gameType, playerId);
    }

    @Override
    public void registerCallback(BiConsumer<Channel, Game.PlayerView> callback)
    {
        messageSender = callback;
    }

    public void registerGame(GameDefinition<?> gameDefinition)
    {
        gameDefinitionMap.put(gameDefinition.gameType(), gameDefinition);
        gameDefinition.actionPerformers().forEach(ap -> actionPerformerMap.put(ap.actionName(), (ActionPerformer<? super Game<?>>) ap));
    }

    @Override
    public void message(Channel channel, Action action)
    {
        var performer = actionPerformerMap.get(action.name());
        var game = gameMap.get(channel.gameId());
        performer.perform(game, action);
        var views = game.playerViews();
        views.forEach((playerId, pv) -> {
            messageSender.accept(
                    new StandardChannel(game.id(), game.type(), playerId), pv
            );
        });
    }
}
