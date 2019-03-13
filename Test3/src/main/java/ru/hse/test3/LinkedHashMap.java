package ru.hse.test3;

import java.util.*;

public class LinkedHashMap<K, V> extends AbstractMap<K, V> implements Map<K, V> {

    private int size;
    private int capacity;
    private LinkedList<K, V>[] table;


    private LinkedList<K, V> listOrder;


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
        table = (LinkedList<K, V>[]) new LinkedList[capacity];
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

    @Override
    public int size() {
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
    private int getHash(Object key, int mod) throws IllegalArgumentException {
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

    @Override
    /**
     * Get the value of element with given key from LinkedHashTable if it exists
     * @param key key of element, from which the desired value can be got
     * @return value of the element with given key or null if it does not exist
     * @throws IllegalArgumentException - throws exception then given key is null
     */
    public V get(Object key) throws IllegalArgumentException {
        if (key == null) {
            throw new IllegalArgumentException("key can not be null!");
        }

        var data = table[getHash(key, capacity)].get(key);
        if (data == null) {
            return null;
        }
        return data.getValue();
    }

    @Override
    /**
     * Change the value of element with given key if it exists or create element with given key and value
     * @param key key of element where to change the value
     * @param value new value
     * @return Previous value of the element with given key or null if it does not exist
     * @throws IllegalArgumentException throws exception then given key is or value is null
     */
    public V put(K key, V value) throws IllegalArgumentException {
        if (key == null) {
            throw new IllegalArgumentException("key can not be null!");
        }
        if (value == null) {
            throw new IllegalArgumentException("value can not be null!");
        }

        var oldData = table[getHash(key, capacity)].put(key, value);
        if (oldData == null) {
            size++;
            listOrder.put(key, value);
            return null;
        } else {
            listOrder.remove(oldData.getKey());
            listOrder.put(key, value);
        }
        if (size >= capacity) {
            extend();
        }
        return oldData.getValue();
    }

    @Override
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
            return oldData.getValue();
        }
        return null;
    }

    /**
     * Remove all elements from LinkedHashTable and shorten capacity to default
     */
    public void clear() {
        size = 0;
        capacity = 1;
        table = (LinkedList<K, V>[]) new LinkedList[capacity];
        for (int i = 0; i < capacity; i++) {
            table[i] = new LinkedList<>();
        }
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }

    private class LinkedHashMapInerator implements Iterator<Entry<K, V>> {

        Iterator<Data<K, V>> currentPointer = listOrder.iterator();

        @Override
        public boolean hasNext() {
            return currentPointer.hasNext();
        }

        @Override
        public Entry<K, V> next() {
            var next = currentPointer.next();
            return (Entry<K, V>) next;
        }
    }
}
