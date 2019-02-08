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
     * @param capacity capacity
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

        for (int i = 0; i < capacity; i++) {
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
     * @param key given key of element
     * @param mod maximum hash for key
     * @return hash for given key
     * @throws IllegalArgumentException - throws exception then given key is null
     */
    private int getHash(String key, int mod) throws IllegalArgumentException {
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
     * Check if there is an element with given key in HashTable
     * @param key key to check
     * @return true - if found an element with given key, false - if not
     * @throws IllegalArgumentException - throws exception then given key is null
     */
    public boolean contains(String key) throws IllegalArgumentException {
        return get(key) != null;
    }

    /**
     * Get the value of element with given key from HashTable if it exists
     * @param key key of element, from which the desired value can be got
     * @return value of the element with given key or null if it does not exist
     * @throws IllegalArgumentException - throws exception then given key is null
     */
    public String get(String key) throws IllegalArgumentException {
        if (key == null) {
            throw new IllegalArgumentException("key can not be null!");
        }

        return table[getHash(key, capacity)].get(key);
    }

    /**
     * Change the value of element with given key if it exists or create element with given key and value
     * @param key key of element where to change the value
     * @param value new value
     * @return Previous value of the element with given key or null if it does not exist
     * @throws IllegalArgumentException throws exception then given key is or value is null
     */
    public String put(String key, String value) throws IllegalArgumentException {
        if (key == null) {
            throw new IllegalArgumentException("key can not be null!");
        }
        if (value == null) {
            throw new IllegalArgumentException("value can not be null!");
        }

        String oldData = table[getHash(key, capacity)].put(key, value);
        if (oldData == null) {
            size++;
        }
        if (size >= capacity) {
            extend();
        }
        return oldData;
    }

    /**
     * Remove element with given key from HashTable if it exists
     * @param key key to remove
     * @return Previous value of the element with given key or null if it does not exist
     * @throws IllegalArgumentException throws exception then given key is null
     */
    public String remove(String key) throws IllegalArgumentException {
        if (key == null) {
            throw new IllegalArgumentException("key can not be null!");
        }

        String oldData = table[getHash(key, capacity)].remove(key);
        if (oldData != null) {
            size--;
        }
        return oldData;
    }

    /**
     * Remove all elements from HashTable and shorten capacity to default
     */
    public void clear() {
        size = 0;
        capacity = 1;
        table = new List[capacity];
        for (int i = 0; i < capacity; i++) {
            table[i] = new List();
        }
    }
}
