package org.ethelred.games.core;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.TimeUnit;

public class InMemoryPlayerManagerImpl implements PlayerManager
{
    Cache<Long, Player> playerCache = Caffeine.newBuilder().expireAfterAccess(30, TimeUnit.MINUTES).build();

    public InMemoryPlayerManagerImpl()
    {
    }

    @Override
    public Player get(long id)
    {
        return playerCache.get(id, idx -> new BasePlayer(idx, "Unknown"));
    }

    @Override
    public void setName(long playerId, String name) {
        ((BasePlayer) get(playerId)).name(name);
    }
}
