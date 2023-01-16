package org.ethelred.games.core;

public class StringAction extends Action
{
    public StringAction(String name, String argument)
    {
        super(name);
        this.argument = argument;
    }

    private final String argument;

    @Override
    public String argumentAsString()
    {
        return argument;
    }

    @Override
    public boolean argumentAsBoolean()
    {
        return Boolean.parseBoolean(argument);
    }
}
