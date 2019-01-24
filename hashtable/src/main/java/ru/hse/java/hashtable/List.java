package ru.hse.java.hashtable;

/**
 *  List - sequence of pair structures, which are connected together via links (linked list)
 *  head - first element of list (or null, if list is empty)
 */
public class List {

    /**
     * Node is an element of list, which contains information(pair) and links to next and previous elements of list
     */
    private class Node {
        private Data pair;
        private Node next;
        private Node prev;

        /**
         * Constructor for Node(element of list)
         * @param pair - pair
         */
        public Node(Data pair) {
            this.pair = pair;
        }
    }

    private Node head;

    /**
     * List constructor
     */
    public List() {
        head = null;
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
     * @param key - key to find
     * @return Node  with given key or null if the key has not been found
     */
    public Node find(String key) {
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
     * @param key - key of element to get
     * @return Value of the element with given key or null if it does not exist
     */
    public String get(String key) {
        Node node = find(key);
        if (node != null) {
            return node.pair.getValue();
        }
        return null;
    }

    /**
     * Change the value of element with given key if it exists or create element with given key and value
     * @param key - key of the element to change
     * @param value - new value
     * @return Previous value of the element with given key or null if it does not exist
     */
    public String put(String key, String value) {
        Node node = find(key);
        if (node != null) {
            String v = node.pair.getValue();
            node.pair.setValue(value);
            return v;
        }
        addToHead(key, value);
        return null;
    }

    /**
     * Add new element with given key and value to head of list
     * @param key - key to add
     * @param value - value to add
     */
    public void addToHead(String key, String value) {
        Node oldHead = head;
        head = new Node(new Data(key, value));
        head.next = oldHead;
        if (head.next != null) {
            head.next.prev = head;
        }
    }

    /**
     * Remove element from head
     * @return head's Data
     */
    public Data removeFromHead() {
        if (head == null)
            return null;
        var removedData = head.pair;
        head = head.next;
        return removedData;
    }

    /**
     * Remove element with given key if it exists
     * @param key - key to remove
     * @return Previous value of the element with given key or null if it does not exist
     */
    public String remove(String key) {
        Node node = find(key);
        if (node != null) {
            if (node.prev != null) {
                node.prev.next = node.next;
            }
            if (node.next != null) {
                node.next.prev = node.prev;
            }
            if (node.equals(head)) {
                head = head.next;
            }
            return node.pair.getValue();
        }
        return null;
    }

    /**
     * Remove all elements from list
     */
    public void clear() {
        head = null;
    }
}
