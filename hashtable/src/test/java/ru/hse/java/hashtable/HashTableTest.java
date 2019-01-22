package ru.hse.java.hashtable;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HashTableTest {

    @Test
    void putGetHashTableTest() {
        HashTable t = new HashTable();
        t.put("a", "b");
        t.put("a", "c");
        assertEquals(t.get("a"), "c");
        t.put("d", "e");
        t.put("e", "f");
        assertEquals(t.get("e"), "f");
    }

    @Test
    void putGetRemoveHashTableTest() {
        HashTable t = new HashTable();
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
    void getSize() {
        HashTable t = new HashTable();
        t.put("a", "b");
        t.put("a", "c");
        t.put("b", "c");
        t.put("d", "e");
        t.remove("b");
        assertEquals(t.getSize(), 2);
    }

    @Test
    void getCapacity() {
        HashTable t = new HashTable();
        t.put("a", "b");
        t.put("a", "c");
        t.put("b", "c");
        t.put("b", "e");
        t.put("d", "e");
        t.put("e", "f");
        t.remove("b");
        assertEquals(t.getCapacity(), 8);
        HashTable p = new HashTable(2);
        p.put("a", "b");
        assertEquals(p.getCapacity(), 2);
        p.put("b", "e");
        p.put("d", "e");
        assertEquals(p.getCapacity(), 4);
}

    @Test
    void contains() {
        HashTable t = new HashTable();
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
    void clear() {
        HashTable t = new HashTable();
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