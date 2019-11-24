package org.testcash;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Optional;

/**
 * Least recently used (LRU) implementation of cache replacement policy by LinkedList.
 * based on https://www.interviewcake.com/concept/java/lru-cache
 * @param <Key>
 * @param <Value>
 */
public class LRUCacheList<Key, Value> implements Cache<Key, Value> {
    private LinkedList<Node<Key, Value>> list;
    private int capacity;

    public LRUCacheList(int capacity) {
        assert capacity > 0;
        this.list = new LinkedList<>();
        this.capacity = capacity;
    }

    @Override
    public Optional<Value> put(Key key, Value value) {
        assert key != null && value != null;
        Value result = null;
        ListIterator it = list.listIterator();
        Node<Key, Value> node = null;
        while(it.hasNext()){
            Node cur = (Node)it.next();
            if(cur.k.equals(key)){
                it.remove();
                node = cur;
                result = node.v;
                break;
            }
        }
        if(node == null)
            node = new Node<Key, Value>(key, value);
        checkCapasity();
        list.addFirst(node);
        return Optional.ofNullable(result);
    }

    @Override
    public Optional<Value> get(Key key) {
        assert key != null;
        Value result = null;
        ListIterator<Node<Key, Value>> it = list.listIterator();
        while(it.hasNext()){
            Node<Key, Value> cur =  it.next();
            if(cur.k.equals(key)){
                it.remove();
                list.addFirst(cur);
                result = cur.v;
                break;
            }
        }
        return Optional.ofNullable(result);
    }

    @Override
    public void clear() {
        list.clear();
    }

    private void checkCapasity() {
        int size = list.size();
        if(size == capacity)
            list.removeLast();
    }

    private static class Node<Key, Value> {
        final Key k;
        final Value v;
        public Node(Key k, Value v) {
            this.k = k;
            this.v = v;
        }
    }

}
