package ru.hse.java.hashtable;

/**
 *  HashTable is a data structure that implements an associative array, that can map keys(string type) to values(string type).
 *  size - current number of elements in HashTable
 *  capacity - maximum number of elements, which can be stored in HashTable
 *  table - array of lists, where the elements are stored
 */
public class HashTable {
    private int size;
    private int capacity;
    private List[] table;

    /**
     * Constructor for HashTable with default capacity = 1
     */
    public HashTable() {
        this(1);
    }

    /**
     * Constructor for HashTable with given capacity
     * @param capacity - capacity
     */
    public HashTable(int capacity) {
        this.capacity = capacity;
        table = new List[capacity];
        for (int i = 0; i < capacity; i++) {
            table[i] = new List();
        }
    }

    public int getSize() {
        return size;
    }

    /**
     * Double the capacity of HashTable
     */
    private void extend() {
        int newCapacity = capacity * 2;
        var newTable = new List[newCapacity];
        for (int i = 0; i < newCapacity; i++) {
            newTable[i] = new List();
        }

        for (int i = 0; i != capacity; i++) {
            while (!table[i].empty()) {
                Data elem = table[i].removeFromHead();
                newTable[getHash(elem.getKey(), newCapacity)].put(elem.getKey(), elem.getValue());
            }
        }
        capacity = newCapacity;
        table = newTable;
    }

    /**
     * Calculate hash for given key
     * @param key - given key of element
     * @param mod - maximum hash for key
     * @return hash for given key
     */
    private int getHash(String key, int mod) {
        return key.hashCode() % mod;
    }

    /**
     * Check if there is an element with given key in HashTable
     * @param key - key to check
     * @return true - if found an element with given key, false - if not
     */
    public boolean contains(String key) {
        return (get(key) != null);
    }

    /**
     * Get the value of element with given key from HashTable if it exists
     * @param key - key of element, from which the desired value can be got
     * @return value of the element with given key or null if it does not exist
     */
    public String get(String key) {
        return table[getHash(key, capacity)].get(key);
    }

    /**
     * Change the value of element with given key if it exists or create element with given key and value
     * @param key - key of element where to change the value
     * @param value - new value
     * @return Previous value of the element with given key or null if it does not exist
     */
    public String put(String key, String value) {
        String elem = table[getHash(key, capacity)].put(key, value);
        if (elem == null) {
            size++;
        }
        if (size >= capacity) {
            extend();
        }
        return elem;
    }

    /**
     * Remove element with given key from HashTable if it exists
     * @param key - key to remove
     * @return Previous value of the element with given key or null if it does not exist
     */
    public String remove(String key) {
        String elem = table[getHash(key, capacity)].remove(key);
        if (elem != null)
            size--;
        return elem;
    }

    /**
     * Remove all elements from HashTable
     */
    public void clear() {
        for (int i = 0; i < capacity; i++) {
            table[i].clear();
        }
        size = 0;
    }
}
