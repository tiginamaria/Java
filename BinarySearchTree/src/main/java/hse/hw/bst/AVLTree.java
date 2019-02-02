package hse.hw.bst;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

public class AVLTree<E> extends AbstractSet<E> implements MyTreeSet<E> {

    private static class TreeNode<E> {
        private TreeNode left;
        private TreeNode right;
        private TreeNode parent;
        private E value;

        private TreeNode(E value, TreeNode left, TreeNode right, TreeNode parent) {
            this.value = value;
            this.left = left;
            this.right = right;
        }
    }


    @Override
    public Iterator<E> descendingIterator() {
        return null;
    }

    @Override
    public TreeSet<E> descendingSet() {
        return null;
    }

    /**
     * Find minimum in set
     * @return if set is not empty - least element, otherwise null
     */
    @Override
    public E first() {
        return null;
    }

    @Override
    public E last() {
        return null;
    }

    @Override
    public E lower(E e) {
        return null;
    }

    @Override
    public E floor(E e) {
        return null;
    }

    @Override
    public E ceiling(E e) {
        return null;
    }

    @Override
    public E higher(E e) {
        return null;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }
}
