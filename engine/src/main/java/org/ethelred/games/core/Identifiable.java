package org.ethelred.games.core;

public interface Identifiable<EQ extends Identifiable>
{
    long id();

    default boolean same(EQ other)
    {
        return id() == other.id();
    }
}
