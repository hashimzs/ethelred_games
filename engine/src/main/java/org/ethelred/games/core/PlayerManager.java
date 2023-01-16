package org.ethelred.games.core;

public interface PlayerManager
{
    Player get(long id);

    void setName(long playerId, String name);
}
