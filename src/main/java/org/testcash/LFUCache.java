package org.testcash;

import java.util.*;

/**
 *
 *
 * based on http://dhruvbird.com/lfu.pdf
 */
public class LFUCache<Key, Value> implements Cache<Key, Value> {
    private final Map<Key, Node<Key, Value>> cache;
    private final LinkedList<Node<Key, Value>>[] frequencyList;
    private int lowestFrequency;
    private int maxFrequency;
    private int capacity;

    public LFUCache(int capacity) {
        assert capacity > 0;
        this.cache = new HashMap<>(capacity);
        this.frequencyList = new LinkedList[capacity];
        this.capacity = capacity;
        this.lowestFrequency = 0;
        this.maxFrequency = capacity - 1;
        for (int i = 0; i <= maxFrequency; i++) {
            frequencyList[i] = new LinkedList<Node<Key, Value>>();
        }
    }

    @Override
    public Optional<Value> put(Key key, Value value) {
        assert key != null && value != null;
        Value oldValue = null;
        Node<Key, Value> currNode = cache.get(key);
        if (currNode == null) {
            if (cache.size() == capacity) {
                evict();
            }
            LinkedList<Node<Key, Value>> nodes = frequencyList[0];
            currNode = new Node(key, value, 0);
            nodes.add(currNode);
            cache.put(key, currNode);
            lowestFrequency = 0;
        } else {
            oldValue = currNode.v;
            currNode.v = value;
        }
        return Optional.ofNullable(oldValue);
    }

    @Override
    public Optional<Value> get(Key key) {
        assert key != null;
        Node<Key, Value> currNode = cache.get(key);
        if (currNode != null) {
            int currFreq = currNode.frequency;
            if (currFreq < maxFrequency) {
                int nextFreq = currFreq + 1;
                LinkedList<Node<Key, Value>> currNodes = frequencyList[currFreq];
                LinkedList<Node<Key, Value>> newNodes = frequencyList[nextFreq];
                currNodes.remove(currNode);
                newNodes.add(currNode);
                currNode.frequency = nextFreq;
                cache.put(key, currNode);
                if (lowestFrequency == currFreq && currNodes.isEmpty()) {
                    lowestFrequency = nextFreq;
                }
            }
            return Optional.of(currNode.v);
        }
        return Optional.empty();
    }

    @Override
    public void clear() {
        for (int i = 0; i <= maxFrequency; i++) {
            frequencyList[i].clear();
        }
        cache.clear();
        lowestFrequency = 0;
    }

    private void evict() {
        LinkedList<Node<Key, Value>> nodes = frequencyList[lowestFrequency];
        if (nodes.isEmpty()) {
            throw new IllegalStateException("Lowest frequency constraint violated!");
        }
        Iterator<Node<Key, Value>> it = nodes.iterator();
        if(it.hasNext()) {
            Node<Key, Value> node = it.next();
            it.remove();
            cache.remove(node.k);
        }
    }

    private static class Node<Key, Value> {
        final Key k;
        Value v;
        int frequency;

        Node(Key k, Value v, int frequency) {
            this.k = k;
            this.v = v;
            this.frequency = frequency;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Node)) return false;
            Node<?, ?> node = (Node<?, ?>) o;
            return k.equals(node.k);
        }

        @Override
        public int hashCode() {
            return Objects.hash(k);
        }
    }
}
