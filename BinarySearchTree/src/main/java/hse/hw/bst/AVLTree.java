package hse.hw.bst;

import org.jetbrains.annotations.NotNull;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

public class AVLTree<E extends Comparable<? super E>> extends AbstractSet<E> implements MyTreeSet<E> {

    private class TreeNode {
        private TreeNode left;
        private TreeNode right;
        private TreeNode parent;
        private E value;
        private int heigth = 1;

        private TreeNode() {

        }

        private TreeNode(E value, TreeNode parent) {
            this.value = value;
            this.parent = parent;
        }

        private TreeNode(E value, TreeNode left, TreeNode right, TreeNode parent) {
            this.value = value;
            this.left = left;
            this.right = right;
            this.parent = parent;
        }

        private E getValue() {
            return value;
        }

        private void setLeft(TreeNode left) {
            this.left = left;
        }

        private void setRight(TreeNode right) {
            this.right = right;
        }

        private TreeNode getLeft() {
            return left;
        }

        private TreeNode getRight() {
            return right;
        }


        /**
         *        A                  B
         *       / \                / \
         *      X  B    =>         A  Z
         *        / \             / \
         *       Y  Z            X  Y
         */
        private void rotateLeft() {
            TreeNode nodeA = this;
            TreeNode nodeB = this.right;
            nodeB.parent = nodeA.parent;
            nodeA.parent = nodeB;

            TreeNode leftSubtreeA = nodeA.left;
            TreeNode leftSubtreeB = nodeB.left;
            TreeNode rightSubtreeB = nodeB.right;

            nodeA.left = leftSubtreeA;
            nodeA.right = leftSubtreeB;
            nodeB.left = nodeA;
            nodeB.right = rightSubtreeB;
        }

        /**
         *        A                  B
         *       / \                / \
         *      B  Z    =>         X  A
         *     / \                   / \
         *    X  Y                  Y  Z
         */
        private void rotateRight() {
            TreeNode nodeA = this;
            TreeNode nodeB = this.left;
            nodeB.parent = nodeA.parent;
            nodeA.parent = nodeB;

            TreeNode rightSubtreeA = nodeA.right;
            TreeNode leftSubtreeB = nodeB.left;
            TreeNode rightSubtreeB = nodeB.right;

            nodeB.left = leftSubtreeB;
            nodeB.right = nodeA;
            nodeA.left = rightSubtreeB;
            nodeA.right = rightSubtreeA;
        }

        private void rotateBigLeft() {

        }

        private void rotateBigRight() {

        }

        private void rotate() {

        }

        private void rebalance() {

        }
    }

    private TreeNode root;

    public AVLTree() {
        root = new TreeNode();
    }

    /**
     * Recursively add given value to tree
     * @param value value to add
     * @param node currant node, in which subtree given value is adding
     * @param parent parent of current node
     * @return modified current node(in which subtree given value has added)
     */
    private TreeNode addAVL(E value, TreeNode node, TreeNode parent) {
        if (node == null) {
            return new TreeNode(value, parent);
        }
        if (value.compareTo(node.getValue()) < 0) {
            node.setLeft(addAVL(value, node.getLeft(), node));
        } else {
            node.setRight(addAVL(value, node.getRight(), node));
        }

        node.rebalance();
        node.rotate();
        return node;
    }

    @Override
    public boolean add(@NotNull E value) {
        root = addAVL(value, root, null);
        return true;
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
