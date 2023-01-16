package org.ethelred.games.core;

import org.jetbrains.annotations.NotNull;

public interface Identifiable<EQ extends Identifiable> extends Comparable<EQ>
{
    long id();

    default boolean same(EQ other)
    {
        return id() == other.id();
    }

    @Override
    default int compareTo(@NotNull EQ o) {
        return Long.compare(id(), o.id());
    }
}
