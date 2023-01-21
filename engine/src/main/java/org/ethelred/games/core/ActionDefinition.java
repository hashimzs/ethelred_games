package org.ethelred.games.core;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Objects;
import java.util.Set;

import org.ethelred.games.util.Util;

public record ActionDefinition(String name, Set<String> possibleArguments) implements Comparable<ActionDefinition> {
    public ActionDefinition(String name, String... possibleArguments) {
        this(name, Set.of(possibleArguments));
    }

    public ActionDefinition(String name) {
        this(name, Set.of());
    }

    @Override
    public int compareTo(@NotNull ActionDefinition o) {
        if (equals(o)) {
            return 0;
        }

        int cmp = name.compareTo(o.name);
        if (cmp != 0) {
            return cmp;
        }
        return Util.compareSets(possibleArguments, o.possibleArguments);
    }
}
