package org.testcash;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Least recently used (LRU) implementation of cache replacement policy by LinkedHashMap.
 * based on https://www.interviewcake.com/concept/java/lru-cache
 * @param <Key>
 * @param <Value>
 */
public class LRUCacheMap<Key, Value> implements Cache<Key, Value> {
    CashLinkedHashMap<Key, Value> list;

    public LRUCacheMap(int capacity) {
        assert capacity > 0;
        list = new CashLinkedHashMap(capacity);
    }

    @Override
    public Optional<Value> put(Key key, Value value) {
        assert key != null && value != null;
        return Optional.ofNullable(list.put(key, value));
    }

    @Override
    public Optional<Value> get(Key key) {
        assert key != null;
        return Optional.ofNullable(list.get(key));
    }

    @Override
    public void clear() {
        list.clear();
    }

    private static class CashLinkedHashMap<Key, Value> extends LinkedHashMap<Key, Value> {
        int capacity;

        CashLinkedHashMap(int capacity) {
            super(capacity, 0.75f, true);
            this.capacity = capacity;
        }

        protected boolean removeEldestEntry(Map.Entry<Key, Value> eldest) {
            return size() > capacity;
        }
    }

}
