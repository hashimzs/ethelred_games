package org.ethelred.games.util;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.ethelred.games.serialization.MyMultisetSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Stream;

/**
 * quick hack replacement to avoid depending on Guava
 * @param <T> The type of items in the Multiset
 */
@JsonSerialize(using = MyMultisetSerializer.class)
public class Multiset<T extends Comparable<T>> implements Iterable<T> {
    private final Map<T, Integer> map = new TreeMap<>();

    @SafeVarargs
    public static <TT extends Comparable<TT>> Multiset<TT> of(TT... items) {
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

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new MyIterator(map.entrySet().iterator());
    }

    private class MyIterator implements Iterator<T> {
        private final Iterator<Map.Entry<T, Integer>> entryIterator;
        private int index;
        private Map.Entry<T, Integer> current;

        public MyIterator(Iterator<Map.Entry<T, Integer>> entryIterator) {
            this.entryIterator = entryIterator;
        }

        @Override
        public boolean hasNext() {
                return entryIterator.hasNext() || (current != null && index > 1);
        }

        @Override
        public T next() {
            if (current == null || index == 1) {
                current = entryIterator.next();
                index = current.getValue();
            } else {
                index--;
            }
            return current.getKey();
        }
    }
}
