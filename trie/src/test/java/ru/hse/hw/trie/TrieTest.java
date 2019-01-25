package ru.hse.hw.trie;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TrieTest {

    private Trie dictionary;

    @BeforeEach
    void initTrie() {
        dictionary = new Trie();
    }

    @Test
    void containsStringTrie() {
        dictionary.add("abba");
        dictionary.add("aba");
        assertTrue(dictionary.contains("abba"));
        assertTrue(dictionary.contains("aba"));
        assertFalse(dictionary.contains("ab"));
    }

    @Test
    void containsNullStringTrie() {
        assertThrows(IllegalArgumentException.class, () -> dictionary.contains(null));
    }

    @Test
    void addStringToTrie() {
        assertTrue(dictionary.add("abba"));
        assertTrue(dictionary.add("aba"));
        assertFalse(dictionary.add("abba"));
        assertTrue(dictionary.contains("abba"));
        assertTrue(dictionary.contains("abba"));
        assertTrue(dictionary.contains("aba"));
        assertFalse(dictionary.contains("ab"));
        assertFalse(dictionary.contains("b"));
    }

    @Test
    void addNullStringTrie() {
        assertThrows(IllegalArgumentException.class, () -> dictionary.add(null));
    }

    @Test
    void removeStringFromTrie() {
        assertTrue(dictionary.add("abba"));
        assertTrue(dictionary.add("aba"));
        assertFalse(dictionary.remove("ab"));
        assertTrue(dictionary.remove("aba"));
        assertTrue(dictionary.contains("abba"));
        assertFalse(dictionary.contains("aba"));
    }

    @Test
    void removeNullStringTrie() {
        assertThrows(IllegalArgumentException.class, () -> dictionary.remove(null));
    }

    @Test
    void prefixCounterEmptyTrie() {
        assertEquals(0, dictionary.howManyStartsWithPrefix(""));
        assertEquals(0, dictionary.howManyStartsWithPrefix("a"));
    }

    @Test
    void prefixCounterNullStringTrie() {
        assertThrows(IllegalArgumentException.class, () -> dictionary.howManyStartsWithPrefix(null));
    }

    @Test
    void prefixCounterTrie() {
        assertTrue(dictionary.add("abba"));
        assertTrue(dictionary.add("aba"));
        assertEquals(2, dictionary.howManyStartsWithPrefix("ab"));
        assertEquals(2, dictionary.howManyStartsWithPrefix(""));
        assertEquals(1, dictionary.howManyStartsWithPrefix("abb"));
        assertEquals(1, dictionary.howManyStartsWithPrefix("abba"));
        assertFalse(dictionary.remove("ab"));
        assertEquals(2, dictionary.howManyStartsWithPrefix("ab"));
        assertTrue(dictionary.remove("aba"));
        assertEquals(1, dictionary.howManyStartsWithPrefix("ab"));
        assertEquals(1, dictionary.howManyStartsWithPrefix(""));
        assertTrue(dictionary.remove("abba"));
        assertEquals(0, dictionary.howManyStartsWithPrefix(""));
    }

    @Test
    void sizeOfTrie() {
        assertEquals(0, dictionary.size());
        assertTrue(dictionary.add("abba"));
        assertTrue(dictionary.add("aba"));
        assertEquals(2, dictionary.size());
        assertFalse(dictionary.remove("ab"));
        assertEquals(2, dictionary.size());
        assertTrue(dictionary.remove("aba"));
        assertEquals(1, dictionary.size());
    }
}