package hse.hw.bst;

import org.jetbrains.annotations.NotNull;

import java.util.AbstractSet;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

import static java.lang.Math.max;

/**
 * BST - data structures that store elements in memory. This implementation is AVL tree (one of the self-balancing binary search tree)
 * @param <E> type of element to store
 */
public class BST<E> extends AbstractSet<E> implements MyTreeSet<E> {

    /**
     * Node of the Binary Search Tree
     */
    private class TreeNode {
        /**
         * left child of node
         */
        private TreeNode left;

        /**
         * right child of node
         */
        private TreeNode right;

        /**
         * parent of node
         */
        private TreeNode parent;

        /**
         * stored value
         */
        private E value;

        /**
         * maximum distance from node to the bottom of the tree
         */
        private int height = 1;

        private TreeNode() { }

        private TreeNode(E value, TreeNode parent) {
            this.value = value;
            this.parent = parent;
        }


        /**
         * In an AVL tree, the heights of the two child subtrees of any node differ by at most one.
         * If at any time they differ by more than one, rebalancing(rotations) is done to restore this property.
         * This method rotate node if it has wrong balance
         * @return node with write balance
         */
        private TreeNode rebalance(TreeNode node) {
            if (getBalanceFactor(node) >= 2) {
                if (getBalanceFactor(node.left) < 0) {
                    node = rotateBigRight(node);
                } else {
                    node = rotateRight(node);
                }
            } else if (getBalanceFactor(node) <= -2) {
                if (getBalanceFactor(node.right) > 0) {
                    node = rotateBigLeft(node);
                } else {
                    node = rotateLeft(node);
                }
            }
            return node;
        }

        /**
         * Rotate nodes as it is shown in the scheme:
         * (this type of rotation implements, when B.height - X.height >= 2, and  Y.height <= Z.height
         *        A                  B
         *       / \                / \
         *      X  B       =>      A  Z
         *        / \             / \
         *       Y  Z            X  Y
         * @param node node A on scheme
         * @return node B on scheme - node with write balance
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
         * Rotate nodes as it is shown in the scheme:
         * (this type of rotation implements, when B.height - Z.height >= 2, and  Y.height <= X.height
         *
         *        A                  B
         *       / \                / \
         *      B  Z       =>      X  A
         *     / \                   / \
         *    X  Y                  Y  Z
         *
         * @param node node A on scheme
         * @return node B on scheme - node with write balance
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
         * Rotate nodes as it is shown in the scheme:
         * (this type of rotation implements, when B.height - Z.height >= 2, and  C.height > K.height
         *
         *         A                  C
         *       /  \               /  \
         *      X   B              A   B
         *         / \     =>    / \  / \
         *        C  K          X  Y Z  K
         *       / \
         *      Y  Z
         *
         * @param node node A on scheme
         * @return node C on scheme - node with write balance
         */
        private TreeNode rotateBigLeft(TreeNode node) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        /**
         * Rotate nodes as it is shown in the scheme:
         * (this type of rotation implements, when B.height - K.height >= 2, and  C.height > X.height
         *
         *         A                  C
         *       /  \               /  \
         *      B   K              B   A
         *     / \        =>     / \  / \
         *    X  C              X  Y Z  K
         *      / \
         *     Y  Z
         * @param node node A on scheme
         * @return node C on scheme - node with write balance
         */
        private TreeNode rotateBigRight(TreeNode node) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        /**
         * Calculate difference in height of left and right child (balance of node)
         * @param node node to get balance
         * @return balance
         */
        private int getBalanceFactor(TreeNode node) {
            if (node == null) {
                return 0;
            }
            int leftHeight = (node.left == null) ? 0 : node.left.height;
            int rightHeight = (node.right == null) ? 0 : node.right.height;
            return leftHeight - rightHeight;
        }

        /**
         * recalculate height of node
         */
        private void updateHeight(TreeNode node) {
            int leftHeight = (node.left == null) ? 0 : node.left.height;
            int rightHeight = (node.right == null) ? 0 : node.right.height;
            node.height = max(leftHeight, rightHeight) + 1;
        }

        private TreeNode updateNode() {
            updateHeight(this);
            return rebalance(this);
        }
    }

    /**
     * root of AVL tree
     */
    private TreeNode root;

    /**
     * number of stored elements in AVL tree
     */
    private int size;

    /**
     *comparator for stored elements
     */
    private Comparator<E> comparator;

    public BST() {
        root = null;
    }

    public BST(Comparator<E> comparator) {
        this();
        this.comparator = comparator;
    }


    /**
     * Recursively add given value to tree
     * @param value value to add
     * @param node currant node, in which subtree given value is adding
     * @param parent parent of  currant node
     * @return modified current node(with added value to subtree)
     */
    private TreeNode addBST(E value, TreeNode node, TreeNode parent) {
        if (node == null) {
            return new TreeNode(value, parent);
        }
        if (compareE(value, node.value) < 0) {
            node.left = addBST(value, node.left, node);
        } else {
            node.right = addBST(value, node.right, node);
        }
        return node.updateNode();
    }

    /**
     * Recursively find given value in tree
     * @param value value to find
     * @param node currant node, in which subtree given value is finding
     * @return node which stores required value, if there is one, null otherwise
     */
    private TreeNode findBST(E value, TreeNode node) {
        if (node == null) {
            return null;
        }
        if (compareE(value, node.value) < 0) {
            return findBST(value, node.left);
        }
        else if (compareE(value, node.value) > 0) {
            return findBST(value, node.right);
        }
        return node;
    }

    private TreeNode lowerBoundBST(E value, TreeNode node) {
        if (node == null) {
            return null;
        }

        int compare = compareE(value, node.value);
        if (compare == 0) {
            return node;
        } else if (compare < 0) {
            return lowerBoundBST(value, node.left);
        } else {
            TreeNode rightNode = lowerBoundBST(value, node.right);
            if (rightNode != null) {
                return rightNode;
            } else {
                return node;
            }
        }
    }

    private TreeNode upperBoundBST(E value, TreeNode node) {
        if (node == null) {
            return null;
        }

        int compare = compareE(value, node.value);
        if (compare == 0) {
            return node;
        } else if (compare > 0) {
            return upperBoundBST(value, node.right);
        } else {
            TreeNode leftNode = upperBoundBST(value, node.left);
            if (leftNode != null) {
                return leftNode;
            } else {
                return node;
            }
        }
    }

    /**
     * Compare to elements of class E
     * @param firstValue first element
     * @param secondValue second element
     * @return <0 if firstValue < secondValue, >0 if firstValue > secondValue, 0 otherwise
     */
    private int compareE(E firstValue, E secondValue) {
        if (comparator != null) {
            return comparator.compare(firstValue, secondValue) ;
        }

        Comparable<? super E> fistValueComparable = (Comparable<? super E>) firstValue;
        return fistValueComparable .compareTo(secondValue);
    }

    /**
     * Find the leftest lowest node in subtree of given node
     * @param node given node
     * @return leftest lowest node in subtree of node (include node)
     */
    private TreeNode downLeft(TreeNode node) {
        while(node.left != null) {
            node = node.left;
        }
        return node;
    }

    /**
     * Find the rightest lowest node in subtree of given node
     * @param node given node
     * @return rightest lowest node in subtree of node (include node)
     */
    private TreeNode downRight(TreeNode node) {
        while(node.right != null) {
            node = node.right;
        }
        return node;
    }


    private TreeNode upLeft(TreeNode node) {
        while(compareE(node.value, node.parent.left.value) == 0) {
            node = node.parent;
        }
        return node;
    }

    private TreeNode upRight(TreeNode node) {
        while(compareE(node.value, node.parent.right.value) == 0) {
            node = node.parent;
        }
        return node;
    }

    private TreeNode nextBST(@NotNull TreeNode node) {
        if (node.right != null) {
            return downLeft(node.right);
        }
        return upLeft(node);
    }

    private TreeNode prevBST(@NotNull TreeNode node) {
        if (node.left != null) {
            return downRight(node.left);
        }
        return upRight(node);
    }

    @Override
    public boolean add(@NotNull E value) {
        if (findBST(value, root) != null) {
            return false;
        }
        root = addBST(value, root, null);
        size++;
        return true;
    }

    @Override
    public boolean contains(@NotNull Object value) {
        return findBST((E)value, root) != null;
    }

    /**
     * Find minimum in set
     * @return if set is not empty - least element, otherwise null
     */
    @Override
    public E first() {
        return downLeft(root).value;
    }

    /**
     * Find maximum in set
     * @return if set is not empty - greatest element, otherwise null
     */
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
        TreeNode lowerEqualNode = lowerBoundBST(e, root);
        if (lowerEqualNode == null) {
            return null;
        } else if (compareE(lowerEqualNode.value, e) < 0) {
            return lowerEqualNode.value;
        }
        TreeNode lowerNode = prevBST(lowerEqualNode);
        return (lowerNode == null) ? null : lowerNode.value;
    }

    /**
     * Find least element in Tree, which is greater then given element
     * @param e given element
     * @return required element, if there is one, null otherwise
     */
    @Override
    public E higher(E e) {
        TreeNode upperEqualNode = upperBoundBST(e, root);
        if (upperEqualNode == null) {
            return null;
        } else if (compareE(upperEqualNode.value, e) > 0) {
            return upperEqualNode.value;
        }
        TreeNode upperNode = nextBST(upperEqualNode);
        return (upperNode == null) ? null : upperNode.value;
    }

    /**
     * Find least element in Tree, which is not less then given element
     * @param e given element
     * @return required element, if there is one, null otherwise
     */
    @Override
    public E floor(E e) {
        TreeNode lowerEqualNode = lowerBoundBST(e, root);
        return (lowerEqualNode == null) ? null : lowerEqualNode.value;
    }

    /**
     * Find least element in Tree, which is not greater then given element
     * @param e given element
     * @return required element, if there is one, null otherwise
     */
    @Override
    public E ceiling(E e) {
        TreeNode upperEqualNode = upperBoundBST(e, root);
        return (upperEqualNode == null) ? null : upperEqualNode.value;
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
