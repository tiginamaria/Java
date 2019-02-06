package hse.hw.bst;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.AbstractSet;
import java.util.Comparator;
import java.util.Iterator;

import static java.lang.Math.max;

/**
 * BST - data structures that store elements in memory. This implementation is AVL tree (one of the self-balancing binary search tree)
 * @param <E> type of element to store
 */
public class BinarySearchTree<E> extends AbstractSet<E> implements MyTreeSet<E> {

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

        private TreeNode(E value, TreeNode parent) {
            this.value = value;
            this.parent = parent;
        }

        /**
         *       C                  C
         *       |                  |
         *       A        =>        B
         *       |                  |
         *       B                  A
         * Safety change parents of given nodeA and nodeB and change child of nodeC from nodeA to nodeB
         * @param nodeA given node
         * @param nodeB son of nodeA
         * @param isLeft true means to set nodeA as .left of nodeB, false - as .right
         */
        private void changeParents(TreeNode nodeA, TreeNode nodeB, boolean isLeft) {
            TreeNode nodeC = nodeA.parent;
            if (nodeC != null) {
                if (nodeC.left == nodeA) {
                    nodeC.left = nodeB;
                } else {
                    nodeC.right = nodeB;
                }
            }
            nodeB.parent = nodeC;
            nodeA.parent = nodeB;

            if (isLeft) {
                nodeB.left = nodeA;
            } else {
                nodeB.right = nodeA;
            }
        }

        /**
         *Safety set given parent to given node
         * @param parent given parent
         * @param node given node to set parent to
         * @param isLeft true means to set node as .left of given parent, false - as .right child
         */
        private void setParent(TreeNode parent, TreeNode node, boolean isLeft) {
            if (node != null) {
                node.parent = parent;
            }
            if (parent != null) {
                if (isLeft) {
                    parent.left = node;
                } else {
                    parent.right = node;
                }
            }
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


            setParent(nodeA, nodeB.left, false);
            changeParents(nodeA, nodeB, true);

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

            setParent(nodeA, nodeB.right, true);
            changeParents(nodeA, nodeB, false);

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
        private int getBalanceFactor(@NotNull TreeNode node) {
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

        private TreeNode recursiveUpdateNode() {
            if (this.parent != null) {
                return this.parent.recursiveUpdateNode();
            } else {
                return this;
            }
        }
    }

    private class InformationHolder {

        /**
         * common root both for ascending and descending versions of tree
         */
        private TreeNode root;

        /**
         * link to tree with ascending order of elements
         */
        private BinarySearchTree<E> ascendingVersion;

        /**
         * link to tree with descending order of elements
         */
        private BinarySearchTree<E> descendingVersion;

        /**
         * original comparator, according to which ascending tree is build
         */
        private Comparator<? super E> treeOrderComparator;

        /**
         * number of modifications made it tree since it was build
         */
        int modificationCounter;

        /**
         * number of elements in tree
         */
        int size;
    }

    /**
     *comparator for stored elements
     */
    private boolean isDescending;

    /**
     * field with all general information about tree, which is must be the same for ascending and descending versions of tree
     */
    private InformationHolder holder;

    /**
     *comparator for elements in tree
     */
    private Comparator<? super E> comparator;

    public BinarySearchTree() {
        holder = new InformationHolder();
        holder.ascendingVersion = this;
    }

    public BinarySearchTree(Comparator<? super E>  comparator) {
        this();
        this.comparator = comparator;
        holder.treeOrderComparator = comparator;
    }

    /**
     * Private constructor to make descending version of tree from ascending;
     * @param ascendingTree tree to make descending version
     */
    private BinarySearchTree(BinarySearchTree<E> ascendingTree) {
        isDescending = true;
        holder = ascendingTree.holder;
        if (ascendingTree.comparator != null) {
            comparator = ascendingTree.comparator.reversed();
        }
    }

    private TreeNode root() {
        return holder.root;
    }

    /**
     * Safety replace given node with another given node
     * @param currantNode node to replace
     * @param newNode new node to put instead currant node
     */
    private TreeNode replaceNode(@NotNull TreeNode currantNode, TreeNode newNode) {
        if (newNode != null) {
            newNode.parent = currantNode.parent;
        }
        if (currantNode.parent != null) {
            if (currantNode.parent.left == currantNode) {
                currantNode.parent.left = newNode;
            } else {
                currantNode.parent.right = newNode;
            }
            return currantNode.parent.recursiveUpdateNode();
        } else {
            return newNode;
        }
    }

    /**
     * Recursively add given value to tree
     * @param value value to add
     * @param node currant node, in which subtree given value is adding
     * @param parent parent of currant node
     * @return modified current node(with added value to subtree)
     */
    private TreeNode addNode(@NotNull E value, TreeNode node, TreeNode parent) {
        if (node == null) {
            return new TreeNode(value, parent);
        }
        if (treeOrderCompare(value, node.value) < 0) {
            node.left = addNode(value, node.left, node);
        } else {
            node.right = addNode(value, node.right, node);
        }
        return node.updateNode();
    }

    /**
     * Recursively remove given value from tree
     * @param value value to remove
     * @param node currant node, in which subtree given value is removing
     * @return root for modified tree
     */
    @Nullable
    private TreeNode removeNode(@NotNull E value, @NotNull TreeNode node) {
        if (treeOrderCompare(value, node.value) < 0) {
            return removeNode(value, node.left);
        } if (treeOrderCompare(value, node.value) > 0) {
            return removeNode(value, node.right);
        } else {
            if (node.left == null) {
                return replaceNode(node, node.right);
            } else if (node.right == null) {
                return replaceNode(node, node.left);
            } else {
                TreeNode nextNode = nextNode(node);
                node.value =  nextNode.value;
                return removeNode(node.value, node.right);
            }
        }
    }

    /**
     * Recursively find given value in tree
     * @param value value to find
     * @param node currant node, in which subtree given value is finding
     * @return node which stores required value, if there is one, null otherwise
     */
    @Nullable
    private TreeNode findNode(@NotNull E value, TreeNode node) {
        if (node == null) {
            return null;
        }
        if (treeOrderCompare(value, node.value) < 0) {
            return findNode(value, node.left);
        }
        else if (treeOrderCompare(value, node.value) > 0) {
            return findNode(value, node.right);
        }
        return node;
    }

    /**
     * Recursively find node which stores greatest value not greater then given value
     * @param value value to find lowerbound
     * @param node currant node, in which subtree lowerbound of value can be
     * @return node which stores lowerbound of given value if there is one, null otherwise
     */
    @Nullable
    private TreeNode lowerBoundNode(@NotNull E value, TreeNode node) {
        if (node == null) {
            return null;
        }
        int compare = compareValue(value, node.value);
        if (compare == 0) {
            return node;
        } else if (compare < 0) {
            return lowerBoundNode(value, node.left);
        } else {
            TreeNode rightNode = lowerBoundNode(value, node.right);
            if (rightNode != null) {
                return rightNode;
            } else {
                return node;
            }
        }
    }

    /**
     * Recursively find node which stores least value not less then given value
     * @param value value to find upperbound
     * @param node currant node, in which subtree upperbound of value can be
     * @return node which stores upperbound of given value if there is one, null otherwise
     */
    @Nullable
    private TreeNode upperBoundNode(@NotNull E value, TreeNode node) {
        if (node == null) {
            return null;
        }
        int compare = compareValue(value, node.value);
        if (compare == 0) {
            return node;
        } else if (compare > 0) {
            return upperBoundNode(value, node.right);
        } else {
            TreeNode leftNode = upperBoundNode(value, node.left);
            if (leftNode != null) {
                return leftNode;
            } else {
                return node;
            }
        }
    }

    /**
     * Compare to elements in tree according to original tree order
     * @param firstValue first element
     * @param secondValue second element
     * @return <0 if firstValue < secondValue, >0 if firstValue > secondValue, 0 otherwise
     */
    private int treeOrderCompare(E firstValue, E secondValue) {
        if (holder.treeOrderComparator != null) {
            return holder.treeOrderComparator.compare(firstValue, secondValue) ;
        }
        Comparable<? super E> fistValueComparable = (Comparable<? super E>) firstValue;
        return fistValueComparable.compareTo(secondValue);
    }

    private int compareValue(E firstValue, E secondValue) {
        if (comparator != null) {
            return comparator.compare(firstValue, secondValue) ;
        }
        Comparable<? super E> fistValueComparable = (Comparable<? super E>) firstValue;
        return fistValueComparable.compareTo(secondValue);
    }



    /**
     * Compare to nodes as elements they store
     * @param nodeA first node
     * @param nodeB second node
     * @return if one of nodes is null return false, else true if elements they store are equal, false otherwise
     */

    /**
     * Find the leftest lowest node in subtree of given node
     * @param node given node
     * @return leftest lowest node in subtree of node (include node)
     */
    private TreeNode downLeft(TreeNode node) {
        if (node == null) {
            return null;
        }
        while (node.left != null) {
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
        if (node == null) {
            return null;
        }
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }

    /**
     * Recursively goes upper the pass from given node to root, while current node is right child of it's parent
     * @param node current node in pass
     * @return first parent, for which current node is left child or null for root or null node
     */
    private TreeNode upLeft(TreeNode node) {
        if (node == null || node.parent == null) {
            return null;
        }
        if (node == node.parent.right) {
            return upLeft(node.parent);
        }
        return node.parent;
    }

    /**
     * Recursively goes upper the pass from given node to root, while current node is left child of it's parent
     * @param node current node in pass
     * @return first parent, for which current node is right child or null for root or null node
     */
    private TreeNode upRight(TreeNode node) {
        if (node == null || node.parent == null) {
            return null;
        }
        if (node == node.parent.left) {
            return upRight(node.parent);
        }
        return node.parent;
    }

    /**
     * Recursively find node which stores value, which goes just after value of given node on sorted order
     * @param node current node to find next
     * @return next node for given one or null if there is no
     */
    private TreeNode nextNode(@NotNull TreeNode node) {
        if (node.right != null) {
            return downLeft(node.right);
        }
        return upLeft(node);
    }

    /**
     * Recursively find node which stores value, which goes just before value of given node on sorted order
     * @param node current node to find previous
     * @return previous node for given one or null if there is no
     */
    private TreeNode prevNode(@NotNull TreeNode node) {
        if (node.left != null) {
            return downRight(node.left);
        }
        return upRight(node);
    }

    private class BinarySearchTreeIterator implements Iterator<E> {
        private TreeNode currentPointer;

        BinarySearchTreeIterator() {
            currentPointer = downLeft(root());
        }

        @Override
        public boolean hasNext() {
            return currentPointer != null;
        }

        @Override
        public E next() {
            E next = hasNext() ? currentPointer.value : null;
            currentPointer = nextNode(currentPointer);
            return next;
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new BinarySearchTreeIterator();
    }

    @Override
    public int size() {
        return holder.size;
    }

    /**
     * Add given value to Set
     * @param value value to add
     * @return true - if there is no such value in set, null otherwise
     */
    @Override
    public boolean add(@NotNull E value) {
        if (findNode(value, root()) != null) {
            return false;
        }
        holder.modificationCounter++;
        holder.root = addNode(value, root(), null);
        holder.size++;
        return true;
    }

    /**
     * Add given value to Set
     * @param value value to add
     * @return true - if there is no such value in set, null otherwise
     */
    @Override
    public boolean remove(@NotNull Object value) {
        if (findNode((E)value, root()) == null) {
            return false;
        }
        holder.modificationCounter++;
        holder.root = removeNode((E)value, root());
        holder.size--;
        return true;
    }

    /**
     * Check if tree contains given element
     * @return true if there is one, false otherwise
     */
    @Override
    public boolean contains(@NotNull Object value) {
        return findNode((E)value, root()) != null;
    }

    /**
     * Find minimum in set
     * @return if set is not empty - least element, otherwise null
     */
    @Override
    @Nullable
    public E first() {
        TreeNode firstNode = downLeft(root());
        return firstNode == null ? null : firstNode.value;
    }

    /**
     * Find maximum in set
     * @return if set is not empty - greatest element, otherwise null
     */
    @Override
    @Nullable
    public E last() {
        TreeNode lastNode = downRight(root());
        return lastNode == null ? null : lastNode.value;
    }

    /**
     * Find greatest element in Tree, which is less then given element
     * @param e given element
     * @return required element, if there is one, null otherwise
     */
    @Override
    @Nullable
    public E lower(@NotNull E e) {
        if (isDescending) {
            return ceiling(e);
        }
        TreeNode lowerEqualNode = lowerBoundNode(e, root());
        if (lowerEqualNode == null) {
            return null;
        } else if (compareValue(lowerEqualNode.value, e) < 0) {
            return lowerEqualNode.value;
        }
        TreeNode lowerNode = prevNode(lowerEqualNode);
        return (lowerNode == null) ? null : lowerNode.value;
    }

    /**
     * Find least element in Tree, which is greater then given element
     * @param e given element
     * @return required element, if there is one, null otherwise
     */
    @Override
    @Nullable
    public E higher(@NotNull E e) {
        if (isDescending) {
            return floor(e);
        }
        TreeNode upperEqualNode = upperBoundNode(e, root());
        if (upperEqualNode == null) {
            return null;
        } else if (compareValue(upperEqualNode.value, e) > 0) {
            return upperEqualNode.value;
        }
        TreeNode upperNode = nextNode(upperEqualNode);
        return (upperNode == null) ? null : upperNode.value;
    }

    /**
     * Find least element in Tree, which is not less then given element
     * @param e given element
     * @return required element, if there is one, null otherwise
     */
    @Override
    public E floor(@NotNull E e) {
        if (isDescending) {
            return higher(e);
        }
        TreeNode lowerEqualNode = lowerBoundNode(e, root());
        return (lowerEqualNode == null) ? null : lowerEqualNode.value;
    }

    /**
     * Find least element in Tree, which is not greater then given element
     * @param e given element
     * @return required element, if there is one, null otherwise
     */
    @Override
    public E ceiling(E e) {
        if (isDescending) {
            return lower(e);
        }
        TreeNode upperEqualNode = upperBoundNode(e, root());
        return (upperEqualNode == null) ? null : upperEqualNode.value;
    }


    @Override
    public Iterator<E> descendingIterator() {
        return null;
    }

    @Override
    public MyTreeSet<E> descendingSet() {
        if (isDescending) {
            return holder.ascendingVersion;
        } else {
            if (holder.descendingVersion == null) {
                holder.descendingVersion = new BinarySearchTree<>(this);
            }
            return holder.descendingVersion;
        }
    }
}
