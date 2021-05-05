package org.ethelred.games.core;

import java.util.Map;
import java.util.Set;

/**
 * Game can be serialized (via Jackson, not Java serialization)
 */
public interface Game<P extends Player> extends Identifiable<Game<P>>
{
    String type();

    enum Status
    {
        PRESTART, IN_PROGRESS, ENDED
    }

    Status status();

    void addPlayer(Player player);

    Map<Long, PlayerView> playerViews();

    interface PlayerView
    {
        record ActionDefinition(String name, Object... possibleArguments) {}
        Set<ActionDefinition> availableActions();
    }

    int playerCount();
    P currentPlayer();
    P nextPlayer();
}
