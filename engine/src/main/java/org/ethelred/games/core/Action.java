package org.ethelred.games.core;

public abstract class Action
{
    private final Player player;
    private final String name;

    public Action(Player player, String name)
    {
        this.player = player;
        this.name = name;
    }

    public String name()
    {
        return name;
    }
    public Player player()
    {
        return player;
    }

    public abstract String argumentAsString();
    public abstract boolean argumentAsBoolean();
}
