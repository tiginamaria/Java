package ru.hse.hw.trie;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

/**
 * Trie - a data structure for storing strings, which is a suspended tree with symbols on the edges.
 */
public class Trie implements Serializable {
    /**
     * TrieNode - vertices of Trie
     */
    private static class TrieNode {
        /**
         * isTerminal - boolean flag, which is true when TrieNode which represents a complete string
         */
        private boolean isTerminal;
        /**
         * prefixCounter - number of strings, which have currant prefix = path from root to this TrieNode
         */
        private int prefixCounter;
        /**
         * next - associative array, which maps from character(possible next characters of string) to TrieNode
         */
        private HashMap<Character, TrieNode> next;

        private TrieNode() {
            next = new HashMap<>();
        }

        private boolean hasNext(Character character) {
            return next.containsKey(character);
        }

        private TrieNode getNext(Character character) {
            return next.get(character);
        }

        private TrieNode addNext(Character character, TrieNode node) {
            return next.put(character, node);
        }

        private TrieNode removeNext(Character character) {
            return next.remove(character);
        }

        private boolean getTerminalState() {
            return isTerminal;
        }

        private void setTerminalState(boolean state) {
            isTerminal = state;
        }

        private boolean isUniquePrefix() {
            return prefixCounter == 1;
        }

        private void incPrefixCounter() {
            prefixCounter++;
        }

        private void decPrefixCounter() {
            prefixCounter--;
        }

        private int getPrefixCounter() {
            return prefixCounter;
        }

        private void setPrefixCounter(int counter) {
            prefixCounter = counter;
        }
    }

    /**
     * size - number of stored strings
     */
    private int size;

    /**
     * root - root of tree, where elements are stored
     */
    private TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    public int size() {
        return size;
    }

    /**
     * Add string to Trie.
     * @param element string to add
     * @return true - if element is not stored in Trie, otherwise false
     * @throws IllegalArgumentException throws exception if element is null
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
     * @param element string to check
     * @return true - if element is already stored in Trie, otherwise false
     * @throws IllegalArgumentException throws exception if element is null
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
     * Remove string from Trie.
     * @param element string to remove
     * @return true - if element is already stored in Trie, otherwise false
     * @throws IllegalArgumentException throws exception if element is null
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
     * @param prefix prefix of sting to count
     * @return number of stored strings starts with given prefix
     * @throws IllegalArgumentException throws exception if prefix is null
     */
    public int howManyStartsWithPrefix(String prefix) throws IllegalArgumentException {
        if (prefix == null) {
            throw new IllegalArgumentException("prefix can not be null");
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

    /**
     * Recursively serialize Trie
     * @param currentNode TrieNode, which is now in process of translating
     * @param out output stream
     * @throws IOException throws an exception when writing to a output stream fails
     */
    private void recursiveSerialize(TrieNode currentNode, OutputStream out) throws IOException {
        out.write(currentNode.getTerminalState() ? 1 : 0);
        out.write(currentNode.next.size());
        for (char character : currentNode.next.keySet()) {
            out.write((int) character);
            recursiveSerialize(currentNode.getNext(character), out);
        }
    }

    /**
     * Translating data structure Trie into given format in Serializable.java
     * @param out output stream
     * @throws IOException throws an exception when reading from an input stream fails
     */
    @Override
    public void serialize(OutputStream out) throws IOException {
        out.write(size);
        recursiveSerialize(root, out);
    }

    /**
     * Recursively deserialize Trie
     * @param currentNode TrieNode, which is now in process of reading and building
     * @param in input stream
     * @throws IOException throws an exception when reading from an input stream fails
     */
    private void recursiveDeserialize(TrieNode currentNode, InputStream in) throws IOException {
        currentNode.setTerminalState(in.read() == 1);
        int nextSize = in.read();
        for (int i = 0; i < nextSize; i++) {
            char character = (char)in.read();
            var nextNode = new TrieNode();
            currentNode.addNext(character, nextNode);
            recursiveDeserialize(nextNode, in);
        }
    }

    /**
     * Builds Trie from description in given format in Serializable.java
     * @param in input stream
     * @throws IOException throws an exception when writing to a output stream fails
     */
    @Override
    public void deserialize(InputStream in) throws IOException {
        var newRoot = new TrieNode();
        int newSize = in.read();
        recursiveDeserialize(newRoot, in);
        root = newRoot;
        size = newSize;
        calcPrefixes(root);
    }

    /**
     * Recursively count how many stored strings starts with each prefix
     * @param currentNode prefix to calculate
     * @return number of stored strings starts with each prefix
     */
    private int calcPrefixes(TrieNode currentNode) {
        var counter = 0;
        for (var nextNode : currentNode.next.values()) {
            counter += calcPrefixes(nextNode);
        }
        counter += currentNode.getTerminalState() ?  1 : 0;
        currentNode.setPrefixCounter(counter);
        return counter;
    }
}
