package org.ethelred.games.util;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;

public final class Util {
    private Util() {}

    public static <K, V> Map<K, V> merge(Map<K, V> a, Map<K, V> b) {
        var r = new HashMap<K, V>();
        r.putAll(a);
        r.putAll(b);
        return Map.copyOf(r);
    }

    public static <E> Set<E> merge(Set<E> a, Set<E> b) {
        var r = new TreeSet<>(a);
        r.addAll(b);
        return r;
    }

    public static <T extends Comparable<T>> int compareSets(Set<T> a, Set<T> b) {
        if (a.equals(b)) {
            return 0;
        }
        int cmp = Integer.compare(a.size(), b.size());
        if (cmp != 0) {
            return cmp;
        }
        var orderA = new ArrayList<>(a);
        orderA.sort(Comparator.naturalOrder());
        var orderB = new ArrayList<>(b);
        orderB.sort(Comparator.naturalOrder());
        var itA = orderA.iterator();
        var itB = orderB.iterator();
        while (cmp == 0 && itA.hasNext() && itB.hasNext()) {
            cmp = itA.next().compareTo(itB.next());
        }
        return cmp;
    }

    public static Path findParent(Path start, Predicate<Path> predicate) {
        for (var path = start; path.getParent() != null; path = path.getParent()) {
            if (predicate.test(path)) {
                return path;
            }
        }
        return null;
    }
}
