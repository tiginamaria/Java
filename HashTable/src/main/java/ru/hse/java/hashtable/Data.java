package ru.hse.java.hashtable;

/**
 * Data - a pair (key, value) - one element of hashtable
 * key - index in hashtable, from which the desired value can be found.
 * value - value to store in hashtable
 */
public class Data {
    private String key;
    private String value;

    /**
     * Constructor for Data
     * @param key key
     * @param value value
     */
    public Data(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

