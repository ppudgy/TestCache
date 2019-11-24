package org.testcash;

import java.util.Optional;

/**
 * Simple cache
 * @param <Key> unique identifier of value, compared by equals()
 * @param <Value> cached value
 */
public interface Cache<Key, Value> {
    /**
     * Put value to cache
     * @param key unique identifier of value, compared by equals()
     * @param value cached value
     * @return maybe old value
     */
    Optional<Value> put(Key key, Value value);

    /**
     * Get value from cache
     * @param key unique identifier of value, compared by equals()
     * @return maybe value
     */
    Optional<Value> get(Key key);

    /**
     *  Clear cache contents
     */
    void clear();
}
