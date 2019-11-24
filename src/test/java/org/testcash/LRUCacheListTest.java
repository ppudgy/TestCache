package org.testcash;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class LRUCacheListTest {

    @Test
    @DisplayName("LRUCacheList zero capacity")
    void testZeroCapacity() {
        Assertions.assertThrows(AssertionError.class, () -> new LRUCacheList<>(0));
    }

    @Test
    @DisplayName("LRUCacheList put null key")
    void testPutNullKey() {
        LRUCacheList<Integer, Integer> cache = new LRUCacheList<>(10);

        Assertions.assertThrows(AssertionError.class, () -> cache.put(null, 1));
    }

    @Test
    @DisplayName("LRUCacheList put null value")
    void testPutNullValue() {
        LRUCacheList<Integer, Integer> cache = new LRUCacheList<>(10);

        Assertions.assertThrows(AssertionError.class, () -> cache.put(1, null));
    }

    @Test
    @DisplayName("LRUCacheList get null value")
    void testGetNullValue() {
        LRUCacheList<Integer, Integer> cache = new LRUCacheList<>(10);

        Assertions.assertThrows(AssertionError.class, () -> cache.get(null));
    }

    @Test
    @DisplayName("LRUCacheList add test")
    void testAdd() {
        LRUCacheList<Integer, Integer> cache = new LRUCacheList<>(10);
        Integer key = 1;
        Integer value = 1;

        Optional res = cache.put(key, value);

        assertThat(res).isNotPresent();
    }

    @Test
    @DisplayName("LRUCacheList hit test")
    void testHit() {
        LRUCacheList<Integer, Integer> cache = new LRUCacheList<>(10);
        Integer key = 1;
        Integer value = 1;
        cache.put(key, value);

        Optional res = cache.get(1);

        assertThat(res).isPresent().get().isEqualTo(1);
    }

    @Test
    @DisplayName("LRUCacheList miss test")
    void testMiss() {
        LRUCacheList<Integer, Integer> cache = new LRUCacheList<>(10);
        Integer key = 1;
        Integer value = 1;
        cache.put(key, value);

        Optional res = cache.get(2);

        assertThat(res).isNotPresent();
    }

    @Test
    @DisplayName("LRUCacheList capacity test")
    void testCapacity() {
        LRUCacheList<Integer, Integer> cache = new LRUCacheList<>(10);
        for (int i = 0; i < 11; i++) {
            cache.put(i,i);
        }

        Optional res = cache.get(0);

        assertThat(res).isNotPresent();
    }

    @Test
    @DisplayName("LRUCacheList clear test")
    void testClear() {
        LRUCacheList<Integer, Integer> cache = new LRUCacheList<>(10);
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
    @DisplayName("LRUCacheList test policy")
    void testPolicy() {
        LRUCacheList<Integer, Integer> cache = new LRUCacheList<>(10);
        for (int i = 0; i < 10; i++) {
            cache.put(i,i);
        }

        Optional z = cache.get(0);
        cache.put(10, 10);

        Optional one = cache.get(1);
        assertThat(one).isNotPresent();
        Optional ten = cache.get(10);
        assertThat(ten).isPresent().get().isEqualTo(10);
        Optional zero = cache.get(0);
        assertThat(zero).isPresent().get().isEqualTo(0);
    }

    @Test
    @DisplayName("LRUCacheList get old value test")
    void testGetOldValue() {
        LRUCacheList<Integer, Integer> cache = new LRUCacheList<>(10);

        cache.put(1,1);
        Optional old = cache.put(1, 11);
        Optional curr = cache.get(1);

        assertThat(old).isPresent().get().isEqualTo(1);
        assertThat(curr).isPresent().get().isEqualTo(11);
    }
}
