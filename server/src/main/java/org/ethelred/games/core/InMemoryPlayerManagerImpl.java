package org.ethelred.games.core;

import javax.inject.Inject;

public class InMemoryPlayerManagerImpl implements PlayerManager
{
    @Inject
    public InMemoryPlayerManagerImpl()
    {
    }

    @Override
    public Player get(long id)
    {
        return new TodoPlayer(id);
    }
}
