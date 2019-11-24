package org.testcash;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class LFUCacheTest {
    @Test
    @DisplayName("LFUCache zero capacity")
    void testZeroCapacity() {
        Assertions.assertThrows(AssertionError.class, () -> new LFUCache<>(0));
    }

    @Test
    @DisplayName("LFUCache put null key")
    void testPutNullKey() {
        LFUCache<Integer, Integer> cache = new LFUCache<>(10);

        Assertions.assertThrows(AssertionError.class, () -> cache.put(null, 1));

    }

    @Test
    @DisplayName("LFUCache put null value")
    void testPutNullValue() {
        LFUCache<Integer, Integer> cache = new LFUCache<>(10);

        Assertions.assertThrows(AssertionError.class, () -> cache.put(1, null));
    }

    @Test
    @DisplayName("LFUCache get null value")
    void testGetNullValue() {
        LFUCache<Integer, Integer> cache = new LFUCache<>(10);

        Assertions.assertThrows(AssertionError.class, () -> cache.get(null));
    }

    @Test
    @DisplayName("LFUCache add test")
    void testAdd() {
        LFUCache<Integer, Integer> cache = new LFUCache<>(10);

        Optional res = cache.put(1, 1);

        assertThat(res).isNotPresent();
    }

    @Test
    @DisplayName("LFUCache hit test")
    void testHit() {
        LFUCache<Integer, Integer> cache = new LFUCache<>(10);
        cache.put(1, 1);

        Optional res = cache.get(1);

        assertThat(res).isPresent().get().isEqualTo(1);
    }

    @Test
    @DisplayName("LFUCache miss test")
    void testMiss() {
        LFUCache<Integer, Integer> cache = new LFUCache<>(10);
        cache.put(1, 1);

        Optional res = cache.get(2);

        assertThat(res).isNotPresent();
    }

    @Test
    @DisplayName("LFUCache capacity test")
    void testCapacity() {
        LFUCache<Integer, Integer> cache = new LFUCache<>(10);
        for (int i = 0; i < 11; i++) {
            cache.put(i,i);
        }

        Optional res = cache.get(0);

        assertThat(res).isNotPresent();
    }

    @Test
    @DisplayName("LFUCache clean test")
    void testClean() {
        LFUCache<Integer, Integer> cache = new LFUCache<>(10);
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
    @DisplayName("LFUCache policy test")
    void testPolicy() {
        LFUCache<Integer, Integer> cache = new LFUCache<>(10);

        for (int i = 0; i < 10; i++) {
            cache.put(i, i);
        }
        for (int i = 1; i < 10; i++) {
            cache.get(i);
        }
        for (int i = 2; i < 10; i++) {
            cache.get(i);
        }
        cache.put(10, 10);

        Optional zero = cache.get(0);
        assertThat(zero).isNotPresent();
        Optional ten = cache.get(10);
        assertThat(ten).isPresent().get().isEqualTo(10);
        Optional one = cache.get(1);
        assertThat(one).isPresent().get().isEqualTo(1);
    }

    @Test
    @DisplayName("LFUCache policy test")
    void testGetOldValue() {
        LFUCache<Integer, Integer> cache = new LFUCache<>(10);

        cache.put(1,1);
        Optional old = cache.put(1, 11);
        Optional curr = cache.get(1);

        assertThat(old).isPresent().get().isEqualTo(1);
        assertThat(curr).isPresent().get().isEqualTo(11);
    }

}
