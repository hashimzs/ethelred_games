package org.ethelred.games.core;

import java.util.List;
import java.util.function.BiConsumer;

public interface Engine
{
    void registerCallback(BiConsumer<Channel, Game.PlayerView> callback);
    void registerGame(GameDefinition<?> gameDefinition);

    List<String> gameTypes();

    long newId();

    interface Channel
    {
        String gameType();
        long gameId();
        long playerId();
    }

    Channel createGame(long playerId, String gameType);
    Channel joinGame(long playerId, long gameId);

    void message(Channel channel, Action action);
}
