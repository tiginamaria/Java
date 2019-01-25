package ru.hse.hw.trie;

import java.util.HashMap;

/**
 * Trie - a data structure for storing strings, which is a suspended tree with symbols on the edges.
 * size - number of stored strings
 * root - root or tree, where elements are stored(associated with the empty string)
 */
public class Trie {
    private int size;
    /**
     * TrieNode - vertices of Trie which contains:
     * next - associative array, which maps from character(possible next characters of string) to TrieNode
     * isTerminal - boolean flag, which is true when TrieNode which represents a complete string
     * prefixCounter - number of strings, which have currant prefix = path from root to this TrieNode
     */
    private static class TrieNode {
        private HashMap<Character, TrieNode> next;
        private boolean isTerminal;
        private int prefixCounter;

        public TrieNode() {
            next = new HashMap<>();
        }

        public boolean hasNext(Character character) {
            return next.containsKey(character);
        }

        public TrieNode getNext(Character character) {
            return next.get(character);
        }

        public TrieNode addNext(Character character) {
            return next.put(character, new TrieNode());
        }

        public TrieNode removeNext(Character character) {
            return next.remove(character);
        }

        public boolean isTerminal() {
            return isTerminal;
        }

        public void setTerminal(boolean state) {
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
    }
    TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    /**
     * Add string to Trie.
     * @param element - string to add
     * @return true - if element is already stored in Trie, otherwise false
     * @throws IllegalArgumentException - throws exception if element is null
     */
    public boolean add(String element) throws IllegalArgumentException {
        if (element == null) {
            throw new IllegalArgumentException("element can not be null");
        }
        if(!contains(element)) {
            return false;
        }
        size++;
        var currentNode = root;
        for (char character : element.toCharArray()) {
            if (!currentNode.hasNext(character)) {
                currentNode.addNext(character);
            }
            currentNode = currentNode.getNext(character);
            currentNode.incPrefixCounter();
        }
        currentNode.setTerminal(true);
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
            currentNode.incPrefixCounter();
        }
        if (!currentNode.isTerminal()) {
            return false;
        }
        return true;
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
        for (char character : element.toCharArray()) {
            currentNode.decPrefixCounter();
            var nextNode = currentNode.getNext(character);
            if (nextNode.isUniquePrefix()) {
                currentNode.removeNext(character);
                return true;
            }
            currentNode = nextNode;
        }
        currentNode.setTerminal(false);
        return true;
    }
}
