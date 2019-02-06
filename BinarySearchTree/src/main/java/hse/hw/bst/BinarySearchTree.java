package hse.hw.bst;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.AbstractSet;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

import static java.lang.Math.max;

/**
 * BST - data structure that store elements in memory. This implementation is AVL tree (one of the self-balancing binary search tree)
 * @param <E> type of element to store
 */
public class BinarySearchTree<E> extends AbstractSet<E> implements MyTreeSet<E> {

    /**
     * Node of the Binary Search Tree
     */
    private static class TreeNode<E> {

        /**
         * left child of node
         */
        private TreeNode<E> left;

        /**
         * right child of node
         */
        private TreeNode<E> right;

        /**
         * parent of node
         */
        private TreeNode<E> parent;

        /**
         * stored value
         */
        private E value;

        /**
         * maximum distance from node to the bottom of the tree
         */
        private int height = 1;

        private TreeNode (E value, TreeNode<E> parent) {
            this.value = value;
            this.parent = parent;
        }

        /**
         *       C                  C
         *       |                  |
         *       A        =>        B
         *       |                  |
         *       B                  A
         * safety swap parents of given nodeA and one of its child nodeB
         * @param nodeA given node
         * @param nodeB son of nodeA
         * @param isLeft true means to set nodeA as .left of nodeB, false - as .right
         */
        private void changeParents(TreeNode<E> nodeA, TreeNode<E> nodeB, boolean isLeft) {
            TreeNode<E> nodeC = nodeA.parent;
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
         *safety set given parent to given node
         * @param parent given parent
         * @param node given node to set parent to
         * @param isLeft true means to set node as .left of given parent, false - as .right child
         */
        private void setParent(TreeNode<E> parent, TreeNode<E> node, boolean isLeft) {
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
         *
         * this method rotate node if it has wrong balance
         * @return node with write balance
         */
        private TreeNode<E> rebalance(TreeNode<E> node) {
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
         * rotate nodes as it is shown in the scheme:
         * (this type of rotation implements, when B.height - X.height >= 2, and  Y.height <= Z.height
         *        A                  B
         *       / \                / \
         *      X  B       =>      A  Z
         *        / \             / \
         *       Y  Z            X  Y
         * @param node node A on scheme
         * @return node B on scheme - node with write balance
         */
        private TreeNode<E> rotateLeft(TreeNode<E> node) {
            TreeNode<E> nodeA = node;
            TreeNode<E> nodeB = node.right;


            setParent(nodeA, nodeB.left, false);
            changeParents(nodeA, nodeB, true);

            updateHeight(nodeA);
            updateHeight(nodeB);
            return nodeB;
        }

        /**
         * rotate nodes as it is shown in the scheme:
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
        private TreeNode<E> rotateRight(TreeNode<E> node) {
            TreeNode<E> nodeA = node;
            TreeNode<E> nodeB = node.left;

            setParent(nodeA, nodeB.right, true);
            changeParents(nodeA, nodeB, false);

            updateHeight(nodeA);
            updateHeight(nodeB);
            return nodeB;
        }

        /**
         * rotate nodes as it is shown in the scheme:
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
        private TreeNode<E> rotateBigLeft(TreeNode<E> node) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        /**
         * rotate nodes as it is shown in the scheme:
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
        private TreeNode<E> rotateBigRight(TreeNode<E> node) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        /**
         * calculate difference in height of left and right child (balance of node)
         * @param node node to get balance
         * @return balance
         */
        private int getBalanceFactor(@NotNull TreeNode<E> node) {
            int leftHeight = (node.left == null) ? 0 : node.left.height;
            int rightHeight = (node.right == null) ? 0 : node.right.height;
            return leftHeight - rightHeight;
        }

        /**
         * recalculate height of node
         */
        private void updateHeight(TreeNode<E> node) {
            int leftHeight = (node.left == null) ? 0 : node.left.height;
            int rightHeight = (node.right == null) ? 0 : node.right.height;
            node.height = max(leftHeight, rightHeight) + 1;
        }

        /**
         * update height and rebalance given node
         * @return updated node
         */
        private TreeNode<E> updateNode() {
            updateHeight(this);
            return rebalance(this);
        }

        /**
         * recursively update height and rebalance all node in path from given one to the root
         * @return updated root
         */
        private TreeNode<E> recursiveUpdateNode() {
            updateHeight(this);
            if (this.parent != null) {
                return this.parent.recursiveUpdateNode();
            } else {
                return this;
            }
        }
    }

    /**
     * root of tree
     */
    private TreeNode<E> root;

    /**
     * number of elements stored in tree
     */
    private int size;

    /**
     * number of all modifications (add and remove) since tree was constructed
     */
    private int currantModification;

    /**
     * comparator for elements in tree
     */
    private Comparator<? super E> comparator;

    /**
     * pointer for descending version of tree
     */
    private BinarySearchTree<E> descendingVersion;

    public BinarySearchTree() {
        descendingVersion = new DescendingBinarySearchTree<>(this);
    }

    public BinarySearchTree(Comparator<? super E>  comparator) {
        this();
        this.comparator = comparator;
    }

    /**
     * safety replace given node with another given node
     * @param currantNode node to replace
     * @param newNode new node to put instead currant node
     */
    private TreeNode<E> replaceNode(@NotNull TreeNode<E> currantNode, TreeNode<E> newNode) {
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
     * recursively add given value to tree
     * @param value value to add
     * @param node currant node, in which subtree given value is adding
     * @param parent parent of currant node
     * @return modified current node(with added value to subtree)
     */
    private TreeNode<E> addNode(@NotNull E value, TreeNode<E> node, TreeNode<E> parent) {
        if (node == null) {
            return new TreeNode<E>(value, parent);
        }
        if (compareValue(value, node.value) < 0) {
            node.left = addNode(value, node.left, node);
        } else {
            node.right = addNode(value, node.right, node);
        }
        return node.updateNode();
    }

    /**
     * recursively remove given value from tree
     * @param value value to remove
     * @param node currant node, in which subtree given value is removing
     * @return root for modified tree
     */
    @Nullable
    private TreeNode<E> removeNode(@NotNull E value, @NotNull TreeNode<E> node) {
        if (compareValue(value, node.value) < 0) {
            return removeNode(value, node.left);
        } if (compareValue(value, node.value) > 0) {
            return removeNode(value, node.right);
        } else {
            if (node.left == null) {
                return replaceNode(node, node.right);
            } else if (node.right == null) {
                return replaceNode(node, node.left);
            } else {
                TreeNode<E> nextNode = nextNode(node);
                node.value =  nextNode.value;
                return removeNode(node.value, node.right);
            }
        }
    }

    /**
     * recursively find given value in tree
     * @param value value to find
     * @param node currant node, in which subtree given value is finding
     * @return node which stores required value, if there is one, null otherwise
     */
    @Nullable
    private TreeNode<E> findNode(@NotNull E value, TreeNode<E> node) {
        if (node == null) {
            return null;
        }
        if (compareValue(value, node.value) < 0) {
            return findNode(value, node.left);
        }
        else if (compareValue(value, node.value) > 0) {
            return findNode(value, node.right);
        }
        return node;
    }

    /**
     * recursively find node which stores greatest value not greater then given value
     * @param value value to find lowerbound
     * @param node currant node, in which subtree lowerbound of value can be
     * @return node which stores lowerbound of given value if there is one, null otherwise
     */
    @Nullable
    private TreeNode<E> lowerBoundNode(@NotNull E value, TreeNode<E> node) {
        if (node == null) {
            return null;
        }
        int compare = compareValue(value, node.value);
        if (compare == 0) {
            return node;
        } else if (compare < 0) {
            return lowerBoundNode(value, node.left);
        } else {
            TreeNode<E> rightNode = lowerBoundNode(value, node.right);
            if (rightNode != null) {
                return rightNode;
            } else {
                return node;
            }
        }
    }

    /**
     * recursively find node which stores least value not less then given value
     * @param value value to find upperbound
     * @param node currant node, in which subtree upperbound of value can be
     * @return node which stores upperbound of given value if there is one, null otherwise
     */
    @Nullable
    private TreeNode<E> upperBoundNode(@NotNull E value, TreeNode<E> node) {
        if (node == null) {
            return null;
        }
        int compare = compareValue(value, node.value);
        if (compare == 0) {
            return node;
        } else if (compare > 0) {
            return upperBoundNode(value, node.right);
        } else {
            TreeNode<E> leftNode = upperBoundNode(value, node.left);
            if (leftNode != null) {
                return leftNode;
            } else {
                return node;
            }
        }
    }

    /**
     * compare to elements in tree according to original tree order
     * @param firstValue first element
     * @param secondValue second element
     * @return <0 if firstValue < secondValue, >0 if firstValue > secondValue, 0 otherwise
     */

    private int compareValue(E firstValue, E secondValue) {
        if (comparator != null) {
            return comparator.compare(firstValue, secondValue) ;
        }
        Comparable<? super E> fistValueComparable = (Comparable<? super E>) firstValue;
        return fistValueComparable.compareTo(secondValue);
    }

    /**
     * find the leftest lowest node in subtree of given node
     * @param node given node
     * @return leftest lowest node in subtree of node (include node)
     */
    private TreeNode<E> downLeft(TreeNode<E> node) {
        if (node == null) {
            return null;
        }
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    /**
     * find the rightest lowest node in subtree of given node
     * @param node given node
     * @return rightest lowest node in subtree of node (include node)
     */
    private TreeNode<E> downRight(TreeNode<E> node) {
        if (node == null) {
            return null;
        }
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }

    /**
     * recursively goes upper the pass from given node to root, while current node is right child of it's parent
     * @param node current node in pass
     * @return first parent, for which current node is left child or null for root or null node
     */
    private TreeNode<E> upLeft(TreeNode<E> node) {
        if (node == null || node.parent == null) {
            return null;
        }
        if (node == node.parent.right) {
            return upLeft(node.parent);
        }
        return node.parent;
    }

    /**
     * recursively goes upper the pass from given node to root, while current node is left child of it's parent
     * @param node current node in pass
     * @return first parent, for which current node is right child or null for root or null node
     */
    private TreeNode<E> upRight(TreeNode<E> node) {
        if (node == null || node.parent == null) {
            return null;
        }
        if (node == node.parent.left) {
            return upRight(node.parent);
        }
        return node.parent;
    }

    /**
     * recursively find node which stores value, which goes just after value of given node on sorted order
     * @param node current node to find next
     * @return next node for given one or null if there is no
     */
    private TreeNode<E> nextNode(@NotNull TreeNode<E> node) {
        if (node.right != null) {
            return downLeft(node.right);
        }
        return upLeft(node);
    }

    /**
     * recursively find node which stores value, which goes just before value of given node on sorted order
     * @param node current node to find previous
     * @return previous node for given one or null if there is no
     */
    private TreeNode<E> prevNode(@NotNull TreeNode<E> node) {
        if (node.left != null) {
            return downRight(node.left);
        }
        return upRight(node);
    }

    /**
     * iterator for ascending binary search tree:
     * follows sorted order of elements in tree;
     * become not valid when tree was modified;
     */
    private class BinarySearchTreeIterator implements Iterator {
        private TreeNode<E> currentPointer;
        private int lastModification;
        private boolean isDescending;

        /**
         * to control modifications of tree iterator memorize number of modification of tree in moment of iterator's construction
         */
        BinarySearchTreeIterator(boolean isDescending) {
            this.isDescending = isDescending;
            currentPointer = isDescending ? downRight(root) : downLeft(root);
            lastModification = currantModification;
        }

        /**
         * check if iterator is not valid
         * @return true if tree was modified, true otherwise
         */
        private boolean notValidIterator() {
            return currantModification != lastModification;
        }

        /**
         * check if there is next element in tree
         * @return true if there is next, false if it is the end of the tree
         */
        @Override
        public boolean hasNext() {
            if (notValidIterator()) {
                throw new ConcurrentModificationException();
            }
            return currentPointer != null;
        }

        /**
         * move iterator to next element
         * @return element which iterator point on
         */
        @Override
        public E next() {
            E next = hasNext() ? currentPointer.value : null;
            currentPointer = isDescending ? prevNode(currentPointer) : nextNode(currentPointer);
            return next;
        }
    }

    /**
     * iterator of set runs all elements from lowest to highest in sorted order
     * @return ascending iterator for tree
     */
    @Override
    public Iterator<E> iterator() {
        return new BinarySearchTreeIterator(false);
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * add given value to set
     * @param value value to add
     * @return true - if there is no such value in set, null otherwise
     */
    @Override
    public boolean add(@NotNull E value) {
        if (findNode(value, root
        ) != null) {
            return false;
        }
        currantModification++;
        root = addNode(value, root, null);
        size++;
        return true;
    }

    /**
     * remove given value from set
     * @param value value to remove
     * @return true - if there is no such value in set, null otherwise
     */
    @Override
    public boolean remove(@NotNull Object value) {
        if (findNode((E)value, root
        ) == null) {
            return false;
        }
        currantModification++;
        root = removeNode((E)value, root);
        size--;
        return true;
    }

    /**
     * check if set contains given element
     * @return true if there is one, false otherwise
     */
    @Override
    public boolean contains(@NotNull Object value) {
        return findNode((E)value, root) != null;
    }

    /**
     * find minimum element in set
     * @return if set is not empty - least element, otherwise null
     */
    @Override
    @Nullable
    public E first() {
        TreeNode<E> firstNode = downLeft(root);
        return firstNode == null ? null : firstNode.value;
    }

    /**
     * find maximum element in set
     * @return if set is not empty - greatest element, otherwise null
     */
    @Override
    @Nullable
    public E last() {
        TreeNode<E> lastNode = downRight(root);
        return lastNode == null ? null : lastNode.value;
    }

    /**
     * find greatest element in set, which is less then given element
     * @param e given element
     * @return required element, if there is one, null otherwise
     */
    @Override
    @Nullable
    public E lower(@NotNull E e) {
        TreeNode<E> lowerEqualNode = lowerBoundNode(e, root);
        if (lowerEqualNode == null) {
            return null;
        } else if (compareValue(lowerEqualNode.value, e) < 0) {
            return lowerEqualNode.value;
        }
        TreeNode<E> lowerNode = prevNode(lowerEqualNode);
        return (lowerNode == null) ? null : lowerNode.value;
    }

    /**
     * find least element in set, which is greater then given element
     * @param e given element
     * @return required element, if there is one, null otherwise
     */
    @Override
    @Nullable
    public E higher(@NotNull E e) {
        TreeNode<E> upperEqualNode = upperBoundNode(e, root);
        if (upperEqualNode == null) {
            return null;
        } else if (compareValue(upperEqualNode.value, e) > 0) {
            return upperEqualNode.value;
        }
        TreeNode<E> upperNode = nextNode(upperEqualNode);
        return (upperNode == null) ? null : upperNode.value;
    }

    /**
     * find greatest element in set, which is not less then given element
     * @param e given element
     * @return required element, if there is one, null otherwise
     */
    @Override
    @Nullable
    public E floor(@NotNull E e) {
        TreeNode<E> lowerEqualNode = lowerBoundNode(e, root);
        return (lowerEqualNode == null) ? null : lowerEqualNode.value;
    }

    /**
     * Find least element in set, which is not greater then given element
     * @param e given element
     * @return required element, if there is one, null otherwise
     */
    @Override
    @Nullable
    public E ceiling(@NotNull E e) {
        TreeNode<E> upperEqualNode = upperBoundNode(e, root
        );
        return (upperEqualNode == null) ? null : upperEqualNode.value;
    }

    /**
     * descending binary search tree has same size and root
     * all modifications in ascending and descending version synchronised in adding and removing, but order elements is reversed
     */
    private static class DescendingBinarySearchTree<E> extends BinarySearchTree<E> {
        /**
         * pointer for ascending version of thee
         */
        BinarySearchTree<E> ascendingVersion;

        /**
         * constructor for descending tree for its ascending version
         * @param ascendingTree tree to synchronise with
         */
        private DescendingBinarySearchTree(BinarySearchTree<E> ascendingTree) {
            this.ascendingVersion = ascendingTree;
        }

        /** {@link BinarySearchTree#iterator()} **/
        @Override
        @Nullable
        public Iterator<E> iterator() {
            return new BinarySearchTreeIterator(true);
        }

        /** {@link BinarySearchTree#size()} **/
        @Override
        public int size() {
            return ascendingVersion.size;
        }

        /** {@link BinarySearchTree#add(E value)} **/
        @Override
        public boolean add(@NotNull E value) {
            return ascendingVersion.add(value);
        }

        /** {@link BinarySearchTree#remove(Object value)} **/
        @Override
        public boolean remove(@NotNull Object value) {
            return ascendingVersion.remove(value);
        }

        /** {@link BinarySearchTree#contains(Object value)} **/
        @Override
        public boolean contains(@NotNull Object value) {
            return ascendingVersion.contains(value);
        }

        /** {@link BinarySearchTree#first()} **/
        @Override
        @Nullable
        public E first() {
            return ascendingVersion.last();
        }

        /** {@link BinarySearchTree#last()} **/
        @Override
        @Nullable
        public E last() {
            return ascendingVersion.first();
        }

        /** {@link BinarySearchTree#lower(E e)} **/
        @Override
        @Nullable
        public E lower(@NotNull E e) {
            return ascendingVersion.ceiling(e);
        }

        /** {@link BinarySearchTree#higher(E e)} **/
        @Override
        @Nullable
        public E higher(@NotNull E e) {
            return ascendingVersion.floor(e);
        }

        /** {@link BinarySearchTree#floor(E e)} **/
        @Override
        public E floor(@NotNull E e) {
            return ascendingVersion.higher(e);
        }


        /** {@link BinarySearchTree#ceiling(E e)} **/
        @Override
        public E ceiling(E e) {
            return ascendingVersion.lower(e);
        }

        /** {@link BinarySearchTree#descendingIterator()} **/
        @Override
        public Iterator<E> descendingIterator() {
            return ascendingVersion.iterator();
        }


        /** {@link BinarySearchTree#descendingSet()} **/
        @Override
        public MyTreeSet<E> descendingSet() {
            return ascendingVersion;
        }
    }

    @Override
    public Iterator<E> descendingIterator() {
        return descendingVersion.iterator();
    }

    @Override
    public MyTreeSet<E> descendingSet() {
        return descendingVersion;
    }
}
