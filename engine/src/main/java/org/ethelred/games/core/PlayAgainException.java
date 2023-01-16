package org.ethelred.games.core;

import java.util.Collection;

public class PlayAgainException extends RuntimeException {
    private final Collection<Player> players;

    public PlayAgainException(Collection<Player> players) {
        this.players = players;
    }

    public Collection<Player> getPlayers() {
        return players;
    }
}
