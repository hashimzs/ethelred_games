package org.ethelred.games.core;

public interface ActionPerformer<G extends Game<?>>
{
    String actionName();

    void perform(G game, Action action);

    default void validate(boolean condition)
    {
        if (!condition)
        {
            throw new InvalidActionException();
        }
    }
}
