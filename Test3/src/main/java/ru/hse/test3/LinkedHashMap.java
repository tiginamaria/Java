package ru.hse.test3;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LinkedHashMap<K, V> extends AbstractMap<K, V> implements Map<K, V> {

    private class ListNode {
        private ListNode nextNode;
        private ListNode prevNode;
        private Data<K, V> data;

        ListNode(Data data) {
            this.data = data;
        }
    }

    private HashTable<K, ListNode>  hashTable;

    private LinkedList<K, ListNode> listOrder;

    private ListNode tail;

    private ListNode head;

    LinkedHashMap() {
        hashTable = new HashTable<K, >();
    }

    @Override
    public int size() {
        return hashTable.getSize();
    }

    @Override
    public boolean containsValue(Object value) {
        return super.containsValue(value);
    }

    @Override
    public boolean containsKey(Object key) {
        return super.containsKey(key);
    }

    @Override
    public V get(Object key) {
        return super.get(key);
    }

    private addListOrder(newNode)

    @Override
    public V put(K key, V value) {
        var newNode = new ListNode(key, value);
        if (head == null) {
            head = newNode;
            tail = head;
        }
        listOrder.put(key, newNode);
        hashTable.put(key, newNode);
    }

    @Override
    public V remove(Object key) {
        return super.remove(key);
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }

}
