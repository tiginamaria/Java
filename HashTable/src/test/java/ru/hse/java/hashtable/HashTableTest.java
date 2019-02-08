package ru.hse.java.hashtable;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HashTableTest {

    private HashTable hashtable;

    @BeforeEach
    void initHashTable() {
        hashtable = new HashTable();
    }

    @Test
    void putGetHashTableTest() {
        hashtable.put("a", "b");
        hashtable.put("a", "c");
        assertEquals(hashtable.get("a"), "c");
        hashtable.put("d", "e");
        hashtable.put("e", "f");
        assertEquals(hashtable.get("e"), "f");
    }

    @Test
    void putGetRemoveHashTableTest() {
        hashtable.put("a", "b");
        hashtable.put("a", "c");
        assertEquals(hashtable.get("a"), "c");
        assertEquals(hashtable.remove("a"), "c");
        assertNull(hashtable.remove("a"), "c");
        hashtable.put("d", "e");
        hashtable.put("a", "d");
        hashtable.put("e", "f");
        assertEquals(hashtable.get("a"), "d");
        assertEquals(hashtable.remove("e"), "f");
        assertNotNull(hashtable.remove("d"));
    }

    @Test
    void putNullKeyThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> hashtable.put(null, "b"));
    }

    @Test
    void putNullValueThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> hashtable.put("a", null));
    }

    @Test
    void getNullKeyThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> hashtable.get(null));
    }

    @Test
    void removeNullKeyThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> hashtable.remove(null));
    }

    @Test
    void getSize() {
        hashtable.put("a", "b");
        hashtable.put("a", "c");
        hashtable.put("b", "c");
        hashtable.put("d", "e");
        hashtable.remove("b");
        assertEquals(hashtable.getSize(), 2);
    }

    @Test
    void contains() {
        hashtable.put("a", "b");
        hashtable.put("a", "c");
        assertTrue(hashtable.contains("a"));
        hashtable.remove("a");
        assertFalse(hashtable.contains("a"));
        hashtable.put("d", "e");
        hashtable.put("a", "d");
        hashtable.put("e", "f");
        assertTrue(hashtable.contains("a"));
        assertFalse(hashtable.contains("b"));
    }

    @Test
    void containsNullKeyThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> hashtable.contains(null));
    }

    @Test
    void clear() {
        hashtable.put("a", "b");
        hashtable.put("a", "c");
        hashtable.put("d", "e");
        hashtable.put("a", "d");
        hashtable.put("e", "f");
        hashtable.clear();
        assertFalse(hashtable.contains("a"));
        assertFalse(hashtable.contains("b"));
    }
}