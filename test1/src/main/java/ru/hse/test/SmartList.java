package ru.hse.test;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 *Modifiable list, optimized to effectively store a small and large number of items
 */
public class SmartList<E> extends AbstractList implements List {
    /**
     * number of elements
     */
    private int size = 0;

    /**
     * pointer to data where elements are stored
     */
    private Object data;

    public SmartList() {}

    public SmartList(Collection<? extends E> collection) {
        size = collection.size();
        if (size == 1) {
            data = collection.iterator().next();
        } else if (size < 6) {
            data = collection.toArray();
        } else {
            data = new ArrayList<E>(collection);
        }
    }

    E getData(int index) {
        return null;
    }

    public E get(int index) {
        return null;
    }

    private void addElement(@NotNull E e) {
        data = e;
    }

    private void addArray(@NotNull E  e) {
        var array = new Object[5];
        array[size - 1] = e;
        data = array;
    }

    private void addList(@NotNull E  e) {
        ((List<Object>)data).add((Object)e);
    }

    private void removeElement(@NotNull Object e) {
        data = null;
    }

    private void removeArray(@NotNull Object  e) {

    }

    private void removeList(@NotNull Object  e) {
        ((List<Object>)data).remove((Object)e);
    }

    private boolean containsElement(@NotNull Object e) {
        return data == null;
    }

    private boolean containsArray(@NotNull Object e) {
        var array = ((Object[]) data);
        for (var item : array) {
            if (item == e) {
                return true;
            }
        }
        return false;
    }

    private boolean containsList(@NotNull Object e) {
        return ((List<Object>)data).contains((Object)e);
    }


    private void expandData() {
        if (size == 2) {
            var element = (E)data;
            var array = (E[])data;
            array[0] = element;
            data = element;
        }
        if (size == 6) {
            var list = Arrays.asList((E[])data);
            data = list;
        }
    }

    @Override
    public boolean add(Object e) {
        if (contains(e)) {
            return false;
        }
        size++;
        expandData();
        if (size == 1) {
            addElement((E) e);
        } else if (size < 6) {
            addArray((E) e);
        } else {
            addList((E) e);
        }
        return true;
    }

    @Override
    public boolean contains(Object e) {
        if (size < 2) {
            return containsElement(e);
        } else if (size < 6) {
            return containsArray(e);
        } else {
            return containsList(e);
        }
    }

    @Override
    public boolean remove(Object e) {
        if (!contains(e)) {
            return false;
        }
        if (size < 2) {
            removeElement(e);
        } else if (size < 6) {
            removeArray(e);
        } else {
            removeList(e);
        }
        size--;
        //updateData();
        return true;
    }

    public int size() {
        return size;
    }
}
