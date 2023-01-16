package org.ethelred.games.core;

public class BooleanAction extends Action
{
    public BooleanAction(String name, boolean argument)
    {
        super(name);
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
