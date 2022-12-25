package org.ethelred.games.core;

public class InMemoryPlayerManagerImpl implements PlayerManager
{
    public InMemoryPlayerManagerImpl()
    {
    }

    @Override
    public Player get(long id)
    {
        return new TodoPlayer(id);
    }
}
