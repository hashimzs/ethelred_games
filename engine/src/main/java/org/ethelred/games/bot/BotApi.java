package org.ethelred.games.bot;

import java.util.List;

public interface BotApi {
    GameResponse joinGame(String shortCode);
    void setName(String name);
    GameResponse action(String path, Action action);

    GameResponse poll(String path);

    record GameResponse(String path, PlayerView playerView){}
    record PlayerView(String status, List<ActionDefinition> availableActions){}
    record ActionDefinition(String name, List<String> possibleArguments){}
    record Action(String name, String value){}
}
