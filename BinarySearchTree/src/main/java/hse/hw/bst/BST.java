package hse.hw.bst;

import org.jetbrains.annotations.NotNull;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.TreeSet;

import static java.lang.Math.max;

/**
 * BST - data structures that store elements in memory. This implementation is AVL tree (one of the self-balancing binary search tree)
 * @param <E> type of element to store
 */
public class BST<E extends Comparable<? super E>> extends AbstractSet<E> implements MyTreeSet<E> {

    /**
     * Node of the Binary Search Tree
     */
    private class TreeNode {
        private TreeNode left;
        private TreeNode right;
        private TreeNode parent;
        private E value;
        private int height = 1;

        private TreeNode() { }

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


        /**
         *        A                  B
         *       / \                / \
         *      X  B       =>      A  Z
         *        / \             / \
         *       Y  Z            X  Y
         */
        private TreeNode rotateLeft(TreeNode node) {
            TreeNode nodeA = node;
            TreeNode nodeB = node.right;

            nodeB.parent = nodeA.parent;
            nodeA.parent = nodeB;
            if (nodeB.left!= null) {
                nodeB.left.parent = nodeA;
            }

            nodeA.right = nodeB.left;
            nodeB.left = nodeA;

            updateHeight(nodeA);
            updateHeight(nodeB);
            return nodeB;
        }

        /**
         *        A                  B
         *       / \                / \
         *      B  Z       =>      X  A
         *     / \                   / \
         *    X  Y                  Y  Z
         */
        private TreeNode rotateRight(TreeNode node) {
            TreeNode nodeA = node;
            TreeNode nodeB = node.left;

            nodeB.parent = nodeA.parent;
            nodeA.parent = nodeB;
            if (nodeB.right!= null) {
                nodeB.right.parent = nodeA;
            }

            nodeA.left = nodeB.right;
            nodeB.right = nodeA;

            updateHeight(nodeA);
            updateHeight(nodeB);
            return nodeB;
        }


        /**
         *         A                  C
         *       /  \               /  \
         *      X   B              A   B
         *         / \     =>    / \  / \
         *        C  K          X  Y Z  K
         *       / \
         *      Y  Z
         */
        private TreeNode rotateBigLeft(TreeNode node) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        /**
         *         A                  C
         *       /  \               /  \
         *      B   K              B   A
         *     / \        =>     / \  / \
         *    X  C              X  Y Z  K
         *      / \
         *     Y  Z
         */
        private TreeNode rotateBigRight(TreeNode node) {
            node.left = rotateRight(node.left);
            return rotateLeft(node);
        }

        private TreeNode rebalance() {
            TreeNode node = this;
            if (getBalanceFactor(node) > 1) {
                if (getBalanceFactor(node.left) > 0) {
                    node = rotateRight(node);
                } else {
                    node = rotateBigRight(node);
                }
            } else if (getBalanceFactor(node) < -1) {
                if (getBalanceFactor(node.right) > 0) {
                    node = rotateBigRight(node);
                } else {
                    node = rotateRight(node);
                }
            }
            return node;
        }

        private void updateHeight(TreeNode node) {
            node.height = max(node.left.height, node.right.height);
        }

        private int getBalanceFactor(TreeNode node) {
            return node.left.height - node.right.height;
        }
    }

    private TreeNode root;

    public BST() {
        root = new TreeNode();
    }

    /**
     * Find given value in tree
     * @param value value to find
     * @param node currant node, in which subtree given value is finding
     * @return node which stores required value, if there is one, null otherwise
     */
    private TreeNode findBST(E value, TreeNode node) {
        if (node == null) {
            return null;
        }
        if (value.compareTo(node.value) < 0) {
            return findBST(value, node.left);
        }
        else if (value.compareTo(node.value) > 0) {
            return findBST(value, node.right);
        }
        return node;
    }

    private TreeNode addBST(E value, TreeNode node, TreeNode parent) {
        if (node == null) {
            return new TreeNode(value, parent);
        }
        if (value.compareTo(node.value) < 0) {
            node.left = addBST(value, node.left, node);
        } else {
            node.right = addBST(value, node.right, node);
        }
        return node.rebalance();
    }

    private TreeNode downLeft(TreeNode node) {
        while(node.left != null) {
            node = node.left;
        }
        return node;
    }

    private TreeNode downRight(TreeNode node) {
        while(node.right != null) {
            node = node.right;
        }
        return node;
    }

    @Override
    public boolean add(@NotNull E value) {
        if (findBST(value, root) != null) {
            return false;
        }
        root = addBST(value, root, null);
        return true;
    }

    /*
    @Override
    public boolean contains(@NotNull Object value) {
        return findBST(value, root) != null;
    }
    */

    /**
     * Find minimum in set
     * @return if set is not empty - least element, otherwise null
     */
    @Override
    public E first() {
        return downLeft(root).value;
    }

    @Override
    public E last() {
        return downRight(root).value;
    }

    /**
     * Find greatest element in Tree, which is less then given element
     * @param e given element
     * @return required element, if there is one, null otherwise
     */
    @Override
    public E lower(E e) {
        return null;
    }

    /**
     * Find least element in Tree, which is greater then given element
     * @param e given element
     * @return required element, if there is one, null otherwise
     */
    @Override
    public E higher(E e) {
        return null;
    }

    /**
     * Find least element in Tree, which is not less then given element
     * @param e given element
     * @return required element, if there is one, null otherwise
     */
    @Override
    public E floor(E e) {
        return null;
    }

    /**
     * Find least element in Tree, which is not greater then given element
     * @param e given element
     * @return required element, if there is one, null otherwise
     */
    @Override
    public E ceiling(E e) {
        return null;
    }

    private boolean comparator(E firstValue, E secondValue) {
        return true;
    }

    private TreeNode upLeft(TreeNode node) {
        while(!comparator(node.value, node.parent.left.value)) {
            node = node.left;
        }
        return node;
    }

    private TreeNode nextBST(@NotNull TreeNode node) {
        if (node.right!= null) {
            return downLeft(node.right);
        }
        return upLeft(node);
    }

    private class TreeIterator<E> implements Iterator<E> {
        private TreeNode nextNode;
        TreeIterator() {
            nextNode = null;  //TODO
        }


        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public E next() {
            return null;
        }

        @Override
        public void remove() {

        }
    }
    @Override
    public Iterator<E> iterator() {
        return new TreeIterator<E>();
    }

    @Override
    public Iterator<E> descendingIterator() {
        return null;
    }

    @Override
    public TreeSet<E> descendingSet() {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

}
