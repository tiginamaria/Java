package ru.hse.test3;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;

public class LinkedHashMap<K, V> extends AbstractMap<K, V> implements Map<K, V> {

    private class ListNode {
        private ListNode nextNode;
        private ListNode prevNode;
        private Data<K, V> data;

        ListNode(Data data) {
            this.data = data;
        }
    }

    private ListNode tail;

    private ListNode head;

    private int size;
    private int capacity;
    private LinkedList<K, ListNode>[] table;


    private LinkedList<K, ListNode> listOrder;


    /**
     * Constructor for LinkedHashTable with default capacity = 1
     */
    public LinkedHashMap() {
        this(1);
    }


    /**
     * Constructor for LinkedHashTable with given capacity
     * @param capacity capacity
     */
    public LinkedHashMap(int capacity) {
        this.capacity = capacity;
        table = (LinkedList<K, ListNode>[]) new LinkedList[capacity];
        for (int i = 0; i < capacity; i++) {
            table[i] = new LinkedList<>();
        }
        listOrder = new LinkedList<>();
    }

    @Override
    public boolean containsValue(Object value) {
        return super.containsValue(value);
    }

    @Override
    public boolean containsKey(Object key) {
        return super.containsKey(key);
    }

    public int getSize() {
        return size;
    }

    /**
     * Double the capacity of LinkedHashTable
     */
    private void extend() {
        int newCapacity = capacity * 2;
        var newTable = new LinkedList[newCapacity];
        for (int i = 0; i < newCapacity; i++) {
            newTable[i] = new LinkedList();
        }

        for (int i = 0; i < capacity; i++) {
            while (!table[i].empty()) {
                Data<K, V> elem = table[i].removeFromHead();
                newTable[getHash(elem.getKey(), newCapacity)].put(elem.getKey(), elem.getValue());
            }
        }
        capacity = newCapacity;
        table = newTable;
    }

    /**
     * Calculate hash for given key
     * @param key given key of element
     * @param mod maximum hash for key
     * @return hash for given key
     * @throws IllegalArgumentException - throws exception then given key is null
     */
    private int getHash(K key, int mod) throws IllegalArgumentException {
        if (key == null) {
            throw new IllegalArgumentException("key can not be null!");
        }
        int hash = key.hashCode() % mod;
        if (hash < 0) {
            hash += mod;
        }
        return hash;
    }

    /**
     * Check if there is an element with given key in LinkedHashTable
     * @param key key to check
     * @return true - if found an element with given key, false - if not
     * @throws IllegalArgumentException - throws exception then given key is null
     */
    public boolean contains(K key) throws IllegalArgumentException {
        return get(key) != null;
    }

    /**
     * Get the value of element with given key from LinkedHashTable if it exists
     * @param key key of element, from which the desired value can be got
     * @return value of the element with given key or null if it does not exist
     * @throws IllegalArgumentException - throws exception then given key is null
     */
    public V get( key) throws IllegalArgumentException {
        if (key == null) {
            throw new IllegalArgumentException("key can not be null!");
        }

        return table[getHash(key, capacity)].get(key);
    }

    @Override
    /**
     * Change the value of element with given key if it exists or create element with given key and value
     * @param key key of element where to change the value
     * @param value new value
     * @return Previous value of the element with given key or null if it does not exist
     * @throws IllegalArgumentException throws exception then given key is or value is null
     */
    public Data<K, V> put(K key, V value) throws IllegalArgumentException {
        if (key == null) {
            throw new IllegalArgumentException("key can not be null!");
        }
        if (value == null) {
            throw new IllegalArgumentException("value can not be null!");
        }

        var oldData = table[getHash(key, capacity)].put(key, value);
        if (oldData == null) {
            size++;
        }
        if (size >= capacity) {
            extend();
        }
        return oldData;
    }

    /**
     * Remove element with given key from LinkedHashTable if it exists
     * @param key key to remove
     * @return Previous value of the element with given key or null if it does not exist
     * @throws IllegalArgumentException throws exception then given key is null
     */
    public V remove(Object key) throws IllegalArgumentException {
        if (key == null) {
            throw new IllegalArgumentException("key can not be null!");
        }

        var oldData = table[getHash(key, capacity)].remove(key);
        if (oldData != null) {
            size--;
        }
        return oldData;
    }

    /**
     * Remove all elements from LinkedHashTable and shorten capacity to default
     */
    public void clear() {
        size = 0;
        capacity = 1;
        table = new LinkedList[capacity];
        for (int i = 0; i < capacity; i++) {
            table[i] = new LinkedList<>();
        }
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }

}
