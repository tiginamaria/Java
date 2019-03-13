package ru.hse.test3;


import java.util.Iterator;
import java.util.Map;

/**
 *  List - sequence of pair structures, which are connected together via links (linked list)
 *  head - first element of list (or null, if list is empty)
 */
public class LinkedList<K, V> {

    /**
     * Node is an element of list, which contains information(pair) and links to next and previous elements of list
     */
    private class Node {
        private Data<K, V> pair;
        private Node next;
        private Node prev;

        /**
         * Constructor for Node(element of list)
         * @param pair element of Data class
         */
        public Node(Data<K, V> pair) {
            this.pair = pair;
        }
    }

    private Node head;

    private Node tail;

    /**
     * List constructor
     */
    public LinkedList() {
        head = null;
        tail = null;
    }

    /**
     * Check if list is empty
     * @return true - if empty, false - if not empty
     */
    public boolean empty() {
        return head == null;
    }

    /**
     * Find element with given key
     * @param key key to find
     * @return Node  with given key or null if the key has not been found
     */
    public Node find(Object key) {
        var currentNode = head;
        while (currentNode != null) {
            if (currentNode.pair.getKey().equals(key)) {
                return currentNode;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    /**
     * Get the value of element with given key if it exists
     * @param key key of element to get
     * @return Value of the element with given key or null if it does not exist
     */
    public Data<K, V> get(Object key) {
        Node node = find(key);
        if (node != null) {
            return node.pair;
        }
        return null;
    }

    /**
     * Change the value of element with given key if it exists or create element with given key and value
     * @param key key of the element to change
     * @param value new value
     * @return Previous value of the element with given key or null if it does not exist
     */
    public Data<K, V> put(K key, V value) {
        Node node = find(key);
        if (node != null) {
            node.pair.setValue(value);
            return node.pair;
        }
        addToHead(key, value);
        return null;
    }

    /**
     * Add new element with given key and value to head of list
     * @param key key to add
     * @param value value to add
     */
    public void addToHead(K key, V value) {
        Node oldHead = head;
        head = new Node(new Data<>(key, value));
        head.next = oldHead;
        if (head.next != null) {
            head.next.prev = head;
        } else {
            tail = head;
        }
    }

    /**
     * Remove element from head
     * @return head's Data
     */
    public Data<K, V> removeFromHead() {
        if (head == null) {
            return null;
        }
        var removedData = head.pair;
        head = head.next;
        if (head != null) {
            head.prev = null;
        }
        return removedData;
    }

    /**
     * Remove element with given key if it exists
     * @param key key to remove
     * @return Previous value of the element with given key or null if it does not exist
     */
    public Data<K, V> remove(Object key) {
        Node node = find(key);
        if (node != null) {
            if (node.prev != null) {
                node.prev.next = node.next;
            }
            if (node.next != null) {
                node.next.prev = node.prev;
            }
            if (node.equals(tail)) {
                tail = tail.prev;
            }
            if (node.equals(head)) {
                head = head.next;
            }
            return node.pair;
        }
        return null;
    }

    public Iterator<Data<K, V>> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Data<K, V>> {

        Node currentPointer = tail;

        @Override
        public boolean hasNext() {
            return currentPointer != null;
        }

        @Override
        public Data<K, V> next() {
            var next = hasNext() ? currentPointer.pair : null;
            currentPointer = currentPointer.prev;
            return next;
        }
    }

    /**
     * Remove all elements from list
     */
    public void clear() {
        head = null;
    }
}
