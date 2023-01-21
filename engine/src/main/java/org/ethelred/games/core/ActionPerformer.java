package org.ethelred.games.core;

import org.jetbrains.annotations.NotNull;

public interface ActionPerformer<G extends Game> extends Comparable<ActionPerformer<?>>
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

    @Override
    default int compareTo(@NotNull ActionPerformer<?> o) {
        return getClass().getName().compareTo(o.getClass().getName());
    }
}
