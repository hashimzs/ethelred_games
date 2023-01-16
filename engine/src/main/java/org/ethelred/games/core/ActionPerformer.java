package org.ethelred.games.core;

public interface ActionPerformer<G extends Game>
{
    String actionName();

    void perform(G game, Player player, Action action);

    default void validate(boolean condition, String description)
    {
        if (!condition)
        {
            throw new InvalidActionException("Validation failed: " + description);
        }
    }
}
