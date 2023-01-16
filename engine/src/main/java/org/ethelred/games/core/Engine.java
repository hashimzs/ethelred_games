package org.ethelred.games.core;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BiConsumer;

public interface Engine
{
    void registerCallback(BiConsumer<Channel, PlayerView> callback);
    void registerGame(GameDefinition<?> gameDefinition);

    List<String> gameTypes();

    PlayerView createGame(Channel channel);

    record ChannelAndView(Channel channel, PlayerView view){}
    ChannelAndView joinGame(long playerId, String shortCode);

    record MessagePlayerView(@Nullable String message, PlayerView playerView){}
    MessagePlayerView message(Channel channel, Action action);

    PlayerView playerView(Channel channel);

    void playerName(long playerId, String name);
}
