package org.ethelred.games.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

/**
 * quick hack replacement to avoid depending on Guava
 * @param <T>
 */
public class Multiset<T> {
    private final Map<T, Integer> map = new HashMap<>();

    @SafeVarargs
    public static <TT> Multiset<TT> of(TT... items) {
        var r = new Multiset<TT>();
        for (var item :
                items) {
            r.add(item);
        }
        return r;
    }

    public Set<T> elementSet() {
        return map.keySet();
    }

    public boolean remove(T item) {
        return map.remove(item, 1) || (map.computeIfPresent(item, (k, a) -> a - 1) != null);
    }

    public void add(T item) {
        map.merge(item, 1, Integer::sum);
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public Stream<T> stream() {
        return map.keySet().stream();
    }

    public int size() {
        return map.values().stream().mapToInt(Integer::intValue).sum();
    }
}
