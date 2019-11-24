package org.testcash;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class LRUCacheMapTest {
    @Test
    @DisplayName("LRUCacheMap zero capacity")
    void testZeroCapacity() {
        Assertions.assertThrows(AssertionError.class, () -> new LRUCacheMap<>(0));
    }

    @Test
    @DisplayName("LRUCacheMap put null key")
    void testPutNullKey() {
        LRUCacheMap<Integer, Integer> cache = new LRUCacheMap<>(10);

        Assertions.assertThrows(AssertionError.class, () -> cache.put(null, 1));
    }

    @Test
    @DisplayName("LRUCacheMap put null value")
    void testPutNullValue() {
        LRUCacheMap<Integer, Integer> cache = new LRUCacheMap<>(10);

        Assertions.assertThrows(AssertionError.class, () -> cache.put(1, null));
    }

    @Test
    @DisplayName("LRUCacheMap get null value")
    void testGetNullValue() {
        LRUCacheMap<Integer, Integer> cache = new LRUCacheMap<>(10);

        Assertions.assertThrows(AssertionError.class, () -> cache.get(null));
    }

    @Test
    @DisplayName("LRUCache add test")
    void testAdd() {
        LRUCacheMap<Integer, Integer> cache = new LRUCacheMap<>(10);
        Integer key = 1;
        Integer value = 1;

        Optional res = cache.put(key, value);

        assertThat(res).isNotPresent();
    }

    @Test
    @DisplayName("LRUCache hit test")
    void testHit() {
        LRUCacheMap<Integer, Integer> cache = new LRUCacheMap<>(10);
        Integer key = 1;
        Integer value = 1;
        cache.put(key, value);

        Optional res = cache.get(1);

        assertThat(res).isPresent().get().isEqualTo(1);
    }

    @Test
    @DisplayName("LRUCache miss test")
    void testMiss() {
        LRUCacheMap<Integer, Integer> cache = new LRUCacheMap<>(10);
        Integer key = 1;
        Integer value = 1;
        cache.put(key, value);

        Optional res = cache.get(2);

        assertThat(res).isNotPresent();
    }

    @Test
    @DisplayName("LRUCache capacity test")
    void testCapacity() {
        LRUCacheMap<Integer, Integer> cache = new LRUCacheMap<>(10);
        for (int i = 0; i < 11; i++) {
            cache.put(i,i);
        }

        Optional res = cache.get(0);

        assertThat(res).isNotPresent();
    }

    @Test
    @DisplayName("LRUCache clean test")
    void testClean() {
        LRUCacheMap<Integer, Integer> cache = new LRUCacheMap<>(10);
        for (int i = 0; i < 10; i++) {
            cache.put(i,i);
        }

        cache.clear();

        for (int i = 0; i < 10; i++) {
            Optional res = cache.get(i);
            assertThat(res).isNotPresent();
        }
    }

    @Test
    @DisplayName("LRUCache policy test")
    void testPolicy() {
        LRUCacheMap<Integer, Integer> cache = new LRUCacheMap<>(10);
        for (int i = 0; i < 10; i++) {
            cache.put(i,i);
        }

        cache.get(0);
        cache.put(10, 10);

        Optional one = cache.get(1);
        assertThat(one).isNotPresent();
        Optional ten = cache.get(10);
        assertThat(ten).isPresent().get().isEqualTo(10);
        Optional zero = cache.get(0);
        assertThat(zero).isPresent().get().isEqualTo(0);
    }
}
