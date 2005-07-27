package de.lmu.ifi.dbs.utilities.heap;

import java.util.Arrays;

/**
 * This heap orders elements according to their natural order (see Comparable).
 *
 * @author Elke Achtert (<a href="mailto:achtert@dbs.ifi.lmu.de">achtert@dbs.ifi.lmu.de</a>)
 */
public class Heap {
  /**
   * Indicates the index null.
   */
  final static int nullIndex = -1;

  /**
   * Array holding the heap.
   */
  private HeapNode[] array;

  /**
   * Points to the last node in this heap.
   */
  protected int lastHeap = nullIndex;

  /**
   * Indicates if this heap is organised in ascending or descending order.
   */
  private boolean ascending = true;

  /**
   * The length of this heap.
   */
  private int len;

  /**
   * Constructor: initialize with empty heap of length 10
   */
  public Heap() {
    this(10, true);
  }

  /**
   * Constructor: initialize with empty heap of length len
   *
   * @param len the length of the heap
   */
  public Heap(final int len) {
    this(len, true);
  }

  /**
   * Constructor: initialize with empty heap of length len
   * and the specified oder
   *
   * @param len       the length of the heap
   * @param ascending if true, the heap is organised in ascending order, otherwise
   *                  the heap is organised in descending other
   */
  public Heap(final int len, boolean ascending) {
    array = new HeapNode[len];
    this.ascending = ascending;
    this.len = len;
  }

  /**
   * Adds a node to the heap.
   *
   * @param n The node to be added.
   */
  public void addNode(final HeapNode n) {
    if (lastHeap == array.length - 1) increaseArray();
    array[++lastHeap] = n;
    n.setIndex(lastHeap);
    flowUp(lastHeap);
  }

  /**
   * Gets and removes the minimum node of tis heap.
   * If the heap is empty, null will be returned.
   *
   * @return The minimum node of tis heap, null in case of emptyness.
   */
  public HeapNode getMinNode() {
    if (isEmpty()) return null;
    final HeapNode minNode = removeMin();
    minNode.setIndex(-1);
    return minNode;
  }

  /**
   * Indicates wether this heap ist empty.
   *
   * @return true if this heap is empty, false otherwise.
   */
  public final boolean isEmpty() {
    return lastHeap == nullIndex;
  }

  /**
   * Clears thie heap.
   */
  public final void clear() {
    array = new HeapNode[array.length];
    lastHeap = nullIndex;
  }

  /**
   * Returns the node at index i.
   *
   * @param i The index of the node to be returned.
   * @return The node at index i.
   */
  public final HeapNode getNodeAt(final int i) {
    return array[i];
  }

  /**
   * Moves up a node at index i until it satisfies the heaporder.
   *
   * @param i The index of the node to be moved up.
   */
  protected final void flowUp(int i) {
    // swap the key at i with its parents along the path to the root
    // until it finds the place, where the heaporder is fulfilled.
    while (parent(i) != nullIndex && isLowerThan(i, parent(i))) {
      swap(parent(i), i);
      i = parent(i);
    }
  }

  /**
   * Moves down a node at index i until it satisfies the heaporder.
   *
   * @param i The index of the node to be moved down.
   */
  protected final void flowDown(int i) {
    // swap the key at i with its parents along the path to the root
    // until it finds the place, where the heaporder is fulfilled.

    while (minChild(i) != nullIndex && isGreaterThan(i, minChild(i))) {
      int minChild = minChild(i);
      swap(minChild(i), i);
      i = minChild;
    }
  }

  /**
   * Removes and returns the minimum node from this heap and restores the heap.
   * Returns The minimum node of this heap.
   *
   * @return
   */
  protected final HeapNode removeMin() {
    // move minimum node to the end
    swap(0, lastHeap);
    HeapNode result = array[lastHeap];
    array[lastHeap] = null;
    // heap is now one node smaller
    lastHeap--;
    // restore the heap from the root on
    heapify(0);
//    return array[lastHeap + 1];
    return result;
  }

  /**
   * Swaps the nodes at the indices i1 and i2 in the array.
   *
   * @param i1
   * @param i2
   */
  protected final void swap(final int i1, final int i2) {
    final HeapNode t = array[i1];
    array[i1] = array[i2];
    array[i1].setIndex(i1);

    array[i2] = t;
    array[i2].setIndex(i2);
  }

  /**
   * Heapifies the subtree located at i.
   * Precondition: i's both childtrees are heapordered
   *
   * @param i
   */
  protected final void heapify(int i) {
    // move the key down the tree till we're done.
    while (i != nullIndex) {
      i = heapifyLocally(i);
    }
  }

  /**
   * Heap order node at index i with respect to its both children.
   * If keys had to be swapped return the new index of the node formerly located at i, nullIndex otherwise
   *
   * @param i The index of the node to be heapified.
   * @return The new index of the node formerly located at i if keys had to be swapped, nullIndex otherwise
   */
  protected final int heapifyLocally(final int i) {
    final int min = minChild(i);
    if (min == nullIndex) {
      return nullIndex;
    } // i is leaf. we're done.

    // if max child has bigger key then swap
    if (isLowerThan(min, i)) {
      swap(i, min);
      return min;
    }
    else {
      return nullIndex;
    }
  }

  /**
   * Returns the index of the smaller child of the node at index i. Returns nullIndex if the node is a leaf.
   *
   * @param i The index of the node
   * @return nullIndex if the node is a leaf, the index of the smaller child otherwise.
   */
  protected final int minChild(final int i) {
    final int right = rightChild(i);
    final int left = leftChild(i);

    if (right == nullIndex) {
      return left;
    }
    else { // because heap is complete there must be a left child
      return isLowerThan(left, right) ? left : right;
    }
  }

  /**
   * Return the index of the left child of of the node at index i, nullIndex if there is no such child.
   *
   * @param i The index of the node.
   * @return The index of the left child of of the node at index i, nullIndex if there is no such child.
   */
  protected final int leftChild(final int i) {
    return at(2 * i + 1); // Because root is at 0. Root at 1 gives 2*i.
  }

  /**
   * Return the index of the right child of of the node at index i, nullIndex if there is no such child.
   *
   * @param i The index of the node.
   * @return The index of the right child of of the node at index i, nullIndex if there is no such child.
   */
  protected final int rightChild(final int i) {
    return at(2 * i + 2); // Because root is at 0. Root at 1 gives 2*i +1.
  }

  /**
   * Returns the parent of the node at index k, nullIndex if k is the root.
   *
   * @param k The index of the node.
   * @return The parent of the node at index k, nullIndex if k is the root
   */
  protected final int parent(final int k) {
    return k == 0 ? nullIndex : at((k - 1) / 2); // Because root is at 0. Root at 1 gives k/2.
    // parent(root) is now root. This makes ?: necessary
  }

  /**
   * Return the index of the node at index i in this heap if i is in heap, otherwise nullIndex.
   *
   * @param i The index of the node.
   * @return i if the index is in heap, otherwise nullIndex
   */
  protected final int at(final int i) {
    return i >= 0 && i <= lastHeap ? i : nullIndex;
  }

  /**
   * Return true if the node at index i1 is lower than the node at index i2.
   *
   * @param i1 The index of the first node to be tested.
   * @param i2 The index of the second node to be tested.
   * @return True if the node at index i1 is lower than the node at index i2, false otherwise.
   */
  protected final boolean isLowerThan(final int i1, final int i2) {
    if (ascending)
      return array[i1].compareTo(array[i2]) < 0;
    else
      return array[i1].compareTo(array[i2]) > 0;
  }

  /**
   * Return true if the node at index i1 is lower than the node at index i2.
   *
   * @param i1 The index of the first node to be tested.
   * @param i2 The index of the second node to be tested.
   * @return True if the node at index i1 is lower than the node at index i2, false otherwise.
   */
  protected final boolean isGreaterThan(final int i1, final int i2) {
    if (ascending)
      return array[i1].compareTo(array[i2]) > 0;
    else
      return array[i1].compareTo(array[i2]) < 0;
  }

  /**
   * Increases the underlying array.
   */
  private void increaseArray() {
    HeapNode[] tmp = new HeapNode[array.length + len];
    System.arraycopy(array, 0, tmp, 0, array.length);
    array = tmp;
  }

  /**
   * Returns a string representation of this heap.
   *
   * @return
   */
  public String toString() {
    return "" + Arrays.asList(array);
  }

}
