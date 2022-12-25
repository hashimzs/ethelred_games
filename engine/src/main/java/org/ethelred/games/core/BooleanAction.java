package org.ethelred.games.core;

public class BooleanAction extends Action
{
    public BooleanAction(Player player, String name, boolean argument)
    {
        super(player, name);
        this.argument = argument;
    }

    private final boolean argument;

    @Override
    public String argumentAsString()
    {
        return String.valueOf(argument);
    }

    @Override
    public boolean argumentAsBoolean()
    {
        return argument;
    }
}
