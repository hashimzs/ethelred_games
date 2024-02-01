package org.ethelred.games.core;

import java.util.Map;

/**
 * Game can be serialized (via Jackson, not Java serialization)
 */
public interface Game extends Identifiable<Game>
{
    String type();

    void shortCode(String shortCode);

    String shortCode();

    void log(Player player, Action action);


    enum Status
    {
        PRESTART, IN_PROGRESS, ENDED
    }

    Status status();

    void addPlayer(Player player);

    Map<Player, PlayerView> playerViews();
    PlayerView playerView(Player player);

    void playerReady(Player player);

    int playerCount();
    Player currentPlayer();
    Player nextPlayer();

    int playAgainCount();

    void playAgainCount(int i);
}
