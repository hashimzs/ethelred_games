package org.ethelred.games.core;

public record TodoPlayer(long id) implements Player
{

    @Override
    public String name()
    {
        return "TODO";
    }
}
