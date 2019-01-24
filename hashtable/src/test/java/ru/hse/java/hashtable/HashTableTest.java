package ru.hse.java.hashtable;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HashTableTest {

    private HashTable t;

    @BeforeEach
    void initHashTable() {
        t = new HashTable();
    }

    @Test
    void putGetHashTableTest() {
        t.put("a", "b");
        t.put("a", "c");
        assertEquals(t.get("a"), "c");
        t.put("d", "e");
        t.put("e", "f");
        assertEquals(t.get("e"), "f");
    }

    @Test
    void putGetRemoveHashTableTest() {
        t.put("a", "b");
        t.put("a", "c");
        assertEquals(t.get("a"), "c");
        assertEquals(t.remove("a"), "c");
        assertNull(t.remove("a"), "c");
        t.put("d", "e");
        t.put("a", "d");
        t.put("e", "f");
        assertEquals(t.get("a"), "d");
        assertEquals(t.remove("e"), "f");
        assertNotNull(t.remove("d"));
    }

    @Test
    void putNullKeyThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> t.put(null, "b"));
    }

    @Test
    void putNullValueThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> t.put("a", null));
    }

    @Test
    void getNullKeyThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> t.get(null));
    }

    @Test
    void removeNullKeyThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> t.remove(null));
    }

    @Test
    void getSize() {
        t.put("a", "b");
        t.put("a", "c");
        t.put("b", "c");
        t.put("d", "e");
        t.remove("b");
        assertEquals(t.getSize(), 2);
    }

    @Test
    void contains() {
        t.put("a", "b");
        t.put("a", "c");
        assertTrue(t.contains("a"));
        t.remove("a");
        assertFalse(t.contains("a"));
        t.put("d", "e");
        t.put("a", "d");
        t.put("e", "f");
        assertTrue(t.contains("a"));
        assertFalse(t.contains("b"));
    }

    @Test
    void containsNullKeyThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> t.contains(null));
    }

    @Test
    void clear() {
        t.put("a", "b");
        t.put("a", "c");
        t.put("d", "e");
        t.put("a", "d");
        t.put("e", "f");
        t.clear();
        assertFalse(t.contains("a"));
        assertFalse(t.contains("b"));
    }
}