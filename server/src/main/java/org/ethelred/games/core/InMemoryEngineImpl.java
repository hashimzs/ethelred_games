package org.ethelred.games.core;

import com.callicoder.snowflake.Snowflake;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.BiConsumer;

public class InMemoryEngineImpl implements Engine
{
    private final Map<String, GameDefinition<?>> gameDefinitionMap = new ConcurrentSkipListMap<>();
    private final Map<String, ActionPerformer<? super Game<?>>> actionPerformerMap = new ConcurrentSkipListMap<>();
    private final Snowflake snowflake;

    private final Map<Long, Game<?>> gameMap = new ConcurrentHashMap<>();
    private final PlayerManager playerManager;
    private final ObjectMapper objectMapper;

    private BiConsumer<Channel, String> messageSender;

    @Inject
    public InMemoryEngineImpl(Snowflake snowflake, PlayerManager playerManager, ObjectMapper objectMapper)
    {
        this.snowflake = snowflake;
        this.playerManager = playerManager;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<String> gameTypes()
    {
        return List.copyOf(gameDefinitionMap.keySet());
    }

    @Override
    public long newId()
    {
        return snowflake.nextId();
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
    public void registerCallback(BiConsumer<Channel, String> callback)
    {
        messageSender = callback;
    }

    public void registerGame(GameDefinition<?> gameDefinition)
    {
        gameDefinitionMap.put(gameDefinition.gameType(), gameDefinition);
        gameDefinition.actionPerformers().forEach(ap -> actionPerformerMap.put(ap.actionName(), (ActionPerformer<? super Game<?>>) ap));
    }

    @Override
    public void message(Channel channel, String message)
    {
        var action = _parseAction(message);
        var performer = actionPerformerMap.get(action.name());
        var game = gameMap.get(channel.gameId());
        performer.perform(game, (Action) action);
        var views = game.playerViews();
        views.forEach((playerId, pv) -> {
            messageSender.accept(
                    new StandardChannel(game.id(), game.type(), playerId),
                    _writePlayerView(pv)
            );
        });
    }

    private String _writePlayerView(Game.PlayerView pv)
    {
        try
        {
            return objectMapper.writeValueAsString(pv);
        }
        catch (JsonProcessingException e)
        {
            throw new RuntimeException(e);
        }
    }

    private Action _parseAction(String message)
    {
        try
        {
            return objectMapper.readValue(message, Action.class);
        }
        catch (JsonProcessingException e)
        {
            throw new InvalidActionException();
        }
    }
}
