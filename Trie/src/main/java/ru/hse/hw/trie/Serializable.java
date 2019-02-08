package ru.hse.hw.trie;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/** Translating data structure Trie state into a format:
 * 1.(int)[size of Trie]
 * 2.(int)[terminal state for TrieNode (true = 1| false = 0)]
 * 3.(int)[number of next TrieNodes]
 * 4.for every next TrieNode:
 *      a) [character on edge]
 *      b) 2. 3. 4. for this next TrieNode
*/
public interface Serializable {
    /**
     * Translating data structure Trie into given format
     * @param out output stream
     * @throws IOException throws an exception when reading from an input stream fails
     */
    void serialize(OutputStream out) throws IOException;

    /**
     * Builds Trie from description in given format
     * @param in input stream
     * @throws IOException throws an exception when writing to a output stream fails
     */
    void deserialize(InputStream in) throws IOException;
}
