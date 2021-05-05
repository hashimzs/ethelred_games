package org.ethelred.games.core;

import java.util.Objects;

public class BasePlayer implements Player
{
    private final long id;
    private final String name;

    public BasePlayer(long id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public BasePlayer(Player player)
    {
        this(player.id(), player.name());
    }

    @Override
    public long id()
    {
        return id;
    }

    @Override
    public String name()
    {
        return name;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Player that)) return false;
        return id == that.id();
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id);
    }
}
