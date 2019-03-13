package ru.hse.test3;

import java.util.Map;
import java.util.Map.Entry;

/**
 * Data - a pair (key, value) - one element of hashtable
 * key - index in hashtable, from which the desired value can be found.
 * value - value to store in hashtable
 */
public class Data<K, V> implements Map.Entry<K, V> {
    private K key;
    private V value;

    /**
     * Constructor for Data
     * @param key key
     * @param value value
     */
    public Data(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public V setValue(V value) {
        var oldValue = this.value;
        this.value = value;
        return oldValue;
    }
}

