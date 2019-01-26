package ru.hse.hw.trie;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;

/**
 * Trie - a data structure for storing strings, which is a suspended tree with symbols on the edges.
 * size - number of stored strings
 * root - root or tree, where elements are stored(associated with the empty string)
 */
public class Trie implements Serializable {
    private int size;
    /**
     * TrieNode - vertices of Trie which contains:
     * isTerminal - boolean flag, which is true when TrieNode which represents a complete string
     * prefixCounter - number of strings, which have currant prefix = path from root to this TrieNode
     * next - associative array, which maps from character(possible next characters of string) to TrieNode
     */
    private static class TrieNode {
        private boolean isTerminal;
        private int prefixCounter;
        private HashMap<Character, TrieNode> next;

        public TrieNode() {
            next = new HashMap<>();
        }

        public boolean hasNext(Character character) {
            return next.containsKey(character);
        }

        public TrieNode getNext(Character character) {
            return next.get(character);
        }

        public TrieNode addNext(Character character, TrieNode node) {
            return next.put(character, node);
        }

        public TrieNode removeNext(Character character) {
            return next.remove(character);
        }

        public boolean getTerminalState() {
            return isTerminal;
        }

        public void setTerminalState(boolean state) {
            isTerminal = state;
        }

        public boolean isUniquePrefix() {
            return prefixCounter == 1;
        }

        public void incPrefixCounter() {
            prefixCounter++;
        }
        public void decPrefixCounter() {
            prefixCounter--;
        }
        public int getPrefixCounter() {
            return prefixCounter;
        }
    }
    TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    public int size() {
        return size;
    }

    /**
     * Add string to Trie.
     * @param element - string to add
     * @return true - if element is not stored in Trie, otherwise false
     * @throws IllegalArgumentException - throws exception if element is null
     */
    public boolean add(String element) throws IllegalArgumentException {
        if (element == null) {
            throw new IllegalArgumentException("element can not be null");
        }
        if (contains(element)) {
            return false;
        }
        size++;
        var currentNode = root;
        root.incPrefixCounter();
        for (char character : element.toCharArray()) {
            if (!currentNode.hasNext(character)) {
                currentNode.addNext(character, new TrieNode());
            }
            currentNode = currentNode.getNext(character);
            currentNode.incPrefixCounter();
        }
        currentNode.setTerminalState(true);
        return true;
    }

    /**
     * Check if Trie contains required string
     * @param element - string to check
     * @return true - if element is already stored in Trie, otherwise false
     * @throws IllegalArgumentException - throws exception if element is null
     */
    public boolean contains(String element) throws IllegalArgumentException {
        if (element == null) {
            throw new IllegalArgumentException("element can not be null");
        }
        var currentNode = root;
        for (char character : element.toCharArray()) {
            if (!currentNode.hasNext(character)) {
                return false;
            }
            currentNode = currentNode.getNext(character);
        }
        return currentNode.getTerminalState();
    }

    /**
     * Remove string to Trie.
     * @param element - string to remove
     * @return true - if element is already stored in Trie, otherwise false
     * @throws IllegalArgumentException - throws exception if element is null
     */
    public boolean remove(String element) throws IllegalArgumentException {
        if (element == null) {
            throw new IllegalArgumentException("element can not be null");
        }
        if (!contains(element)) {
            return false;
        }
        size--;
        var currentNode = root;
        root.decPrefixCounter();
        for (char character : element.toCharArray()) {
            var nextNode = currentNode.getNext(character);
            if (nextNode.isUniquePrefix()) {
                currentNode.removeNext(character);
                return true;
            }
            currentNode = nextNode;
            currentNode.decPrefixCounter();
        }
        currentNode.setTerminalState(false);
        return true;
    }

    /**
     * Count how many stored strings starts with given prefix
     * @param prefix - prefix of sting to count
     * @return number of stored strings starts with given prefix
     * @throws IllegalArgumentException - throws exception if prefix is null
     */
    public int howManyStartsWithPrefix(String prefix) throws IllegalArgumentException {
        if (prefix == null) {
            throw new IllegalArgumentException("element can not be null");
        }
        var currentNode = root;
        for (char character : prefix.toCharArray()) {
            if (!currentNode.hasNext(character)) {
                return 0;
            }
            currentNode = currentNode.getNext(character);
        }
        return currentNode.getPrefixCounter();
    }

    public void recursiveSerialize(TrieNode currentNode, OutputStream out) throws IOException {
        out.write(currentNode.getTerminalState() ? 1 : 0);
        out.write(currentNode.next.size());
        for (char character : currentNode.next.keySet()) {
            out.write((int) character);
            recursiveSerialize(currentNode.getNext(character), out);
        }
    }

    public void serialize(OutputStream out) throws IOException {
        out.write(size);
        recursiveSerialize(root, out);
    }

    public void recursiveDeserialize(TrieNode currentNode, InputStream in) throws IOException {
        currentNode.setTerminalState(in.read() == 1);
        int nextSize = in.read();
        for (int i = 0; i < nextSize; i++) {
            char character = (char)in.read();
            var nextNode = new TrieNode();
            currentNode.addNext(character, nextNode);
            recursiveDeserialize(nextNode, in);
        }
    }

    public void deserialize(InputStream in) throws IOException {
        var newRoot = new TrieNode();
        int newSize = in.read();
        recursiveDeserialize(newRoot, in);
        root = newRoot;
        size = newSize;
        calcPrefixes(root);
    }

    public int calcPrefixes(TrieNode currentNode) {
        var ends = 0;
        for(var nextNode : currentNode.next.values()) {
            ends += calcPrefixes(nextNode);
        }
        return ends + (currentNode.getTerminalState() ?  1 : 0);
    }
}
