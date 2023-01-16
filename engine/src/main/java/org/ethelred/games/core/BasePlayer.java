package org.ethelred.games.core;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public final class BasePlayer implements Player
{
    private final long id;

    private volatile String name;

    public BasePlayer(long id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public BasePlayer(long id) {
        this(id, "Unknown");
    }

    public BasePlayer(Player player)
    {
        this(player.id(), player.name());
    }

    @Override
    @JsonProperty("id")
    public long id()
    {
        return id;
    }

    @Override
    @JsonProperty("name")
    public String name()
    {
        return name;
    }

    public void name(String name) {
        this.name = name;
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
