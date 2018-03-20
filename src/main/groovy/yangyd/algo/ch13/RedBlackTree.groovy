package yangyd.algo.ch13

import yangyd.algo.ch12.BinarySearchTrees
import yangyd.algo.ch12.BinaryTrees
import yangyd.algo.datastructure.BinaryTreeNode

import java.util.concurrent.atomic.AtomicReference

import static yangyd.algo.datastructure.BinaryTreeNode.nil

class RedBlackTree<T extends Comparable<T>> {
  private BinaryTreeNode<T> root
  private final BinaryTreeNode<T> sentinel = blackNode(null)// common leaf of all nodes

  int height() {
    root == null ? 0 : BinaryTrees.depth(root)
  }

  List<T> collect() {
    root == null ? Collections.emptyList() : BinarySearchTrees.collectSorted(root)
  }

  void print() {
    if (root != null) {
      BinaryTrees.preOrderWalk(root, {
        node, depth ->
          if (!nil(node)) {
            println(node)
          }
      })
    }
  }

  void insert(T key) {
    if (key == null) {
      throw new IllegalArgumentException("Null key is not allowed")
    }

    if (root == null) {
      root = blackNode(key)
      root.parent = root.left = root.right = sentinel
    } else {
      def node = BinarySearchTrees.insert(root, key)
      node.left = sentinel
      node.right = sentinel
      node.makeRed()
      insertionFix(node)
    }
  }

  void delete(T key) {
    if (root == null) {
      throw new NoSuchElementException(String.valueOf(key))
    }
    final o = BinarySearchTrees.search(root, key)
    if (!o.present) {
      throw new NoSuchElementException(String.valueOf(key))
    }
//    def k = o.get().key
//    println("----------------------------------------------")
//    println("--> about to delete: ${o.get()}")
    deleteByCases(o.get())
//    println("---------------- ${k} deleted -------------------")
//    print()
  }

  private void deleteByCases(BinaryTreeNode<T> node) {
    // An improved deletion procedure utilizing BinaryTreeNode.copyFrom().
    // It is simpler than what's implemented in BinarySearchTrees.delete().

    final noleft = nil(node.left)
    final noright = nil(node.right)

    if (noleft && noright) {
      if (node == root) { // the only node
        root = null
        sentinel.left = sentinel.right = sentinel.parent = null // paranoia
      } else {
        final parent = node.parent
        delete0(node, parent)
        if (node.black) { // deleting a dangling black node is the only case where we need to re-balance
          deletionFix(sentinel, parent) // node is replaced by a sentinel, start re-balance from here
        }
      }

    } else if (noleft) {
      checkDelete1(node, node.right) // we can assert here that node is black and its child is red
      final child = node.right
      deleteWithSingleChild(node, child)
      child.makeBlack()// we replaced a black node with its red child, so simply painting it black can preserve RB tree properties

    } else if (noright) {
      checkDelete1(node, node.left)
      final child = node.left
      deleteWithSingleChild(node, child)
      child.makeBlack()// we replaced a black node with its red child, so simply painting it black can preserve RB tree properties

    } else {
      // to delete node with two children, we find its successor which has at most 1 child
      final elected = successor(node)
      node.copyFrom(elected) // copy its content to node
      deleteByCases(elected) // now simply delete the successor
    }
  }

  private static void checkDelete1(BinaryTreeNode<?> toDelete, BinaryTreeNode<?> child) {
    if (!toDelete.black) {
      throw new AssertionError("Violation: red node can not has just one non-sentinel child")
    }
    if (child.black) {
      throw new AssertionError("Violation: if a black node has exactly one non-sentinel black child, it must be red")
    }
  }

  private void deleteWithSingleChild(BinaryTreeNode<T> toDelete, BinaryTreeNode<T> child) {
    final p = toDelete.parent
    if (toDelete == root) {
      root = child
    } else {
      if (p.left == toDelete) {
        p.left = child
      } else {
        p.right = child
      }
    }
    child.parent = p
    toDelete.parent = null
    toDelete.right = null
  }

  /**
   * simplified procedure of finding successor for deletion
   */
  private static BinaryTreeNode<T> successor(BinaryTreeNode<T> node) {
    def pp = null
    def p = node.right
    while (!nil(p)) {
      pp = p
      p = p.left
    }
    pp
  }

  private void delete0(BinaryTreeNode<T> node, BinaryTreeNode<T> parent) {
    // node is a black node with two sentinel children
    // node is not root
    if (parent.left == node) {
      parent.left = sentinel
    } else if (parent.right == node) {
      parent.right = sentinel
    } else {
      throw new IllegalStateException("broken tree structure") // impossible
    }
    node.parent = null
  }

  /**
   * rebalance the RB tree after the black depth of the sub-tree rooted at n is reduced by 1.
   * Its possible of n being a leaf sentinel.
   * @param n
   * @param p parent of n. this is necessary because n may be sentinel that doesn't has a parent pointer
   */
  private void deletionFix(final BinaryTreeNode<T> n, final BinaryTreeNode<T> p) {
    if (n == root) {
      return // if we are at root, the tree is balanced
    }

    final s = p.left == n ? p.right : p.left
    if (s == sentinel) {
      throw new IllegalStateException("sibling of a deleted node should not be sentinel")
    }

    if (!s.black) { // s is red (thus p is black), case 2
      deletionFixCase2(n, p, s)
    } else { // s is black, case 3~6
      // is it safe to turn s red to match the black depth of its sibling (which is 1 less)?
      if (s.left.black && s.right.black) { // yes
        deletionFixCase3Case4(p, s) // may involve recursive call
      } else { // at least one child is red
        deletionFixCase5Case6(n, p, s)
      }
    }
  }

  private void deletionFixCase2(final BinaryTreeNode<T> n, final BinaryTreeNode<T> p, final BinaryTreeNode<T> s) {
    // s is red, thus p must be black

    // rotate towards n's side to make s occupy p's old place
    if (p.left == n) {
      BinaryTrees.leftRotate(p)
    } else {
      BinaryTrees.rightRotate(p)
    }
    if (root == p) { // in case we rotated at the root
      root = s
    }

    s.makeBlack() // s is in p's old place now, and we keep it black for this place,
    p.makeRed() // and paint p red, so it converts to case 4~6
    deletionFix(n, p) // after rotation, n is still child of p
  }

  private void deletionFixCase3Case4(final BinaryTreeNode<T> p, final BinaryTreeNode<T> s) {
    // s's both children are black, which means we can make s red
    s.makeRed()

    if (p.black) { // case 3
      deletionFix(p, p.parent) // descendant of p is balanced but has 1 less black depth now, so go up and repeat the process
    } else { // p is red, case 4
      p.makeBlack() // balanced, we are done
    }
  }

  private void deletionFixCase5Case6(final BinaryTreeNode<T> n, final BinaryTreeNode<T> p, final BinaryTreeNode<T> s) {
    // s is black, and at least one of its children is red
    // case 5: n's different-side nephew is black (that is, s.right if n is left child, s.left if n is right child)
    // case 6: n's different-side nephew is red
    // if not in case 6, convert to case 6 and do the re balance there
    if (p.left == n && s.right.black) {
      final sl = BinaryTrees.rightRotate(s)
      sl.makeBlack()
      s.makeRed()
      deletionFixCase6(n, p, sl)
    } else if (p.right == n && s.left.black) {
      final sr = BinaryTrees.leftRotate(s)
      sr.makeBlack()
      s.makeRed()
      deletionFixCase6(n, p, sr)
    } else { // we are already in case 6
      deletionFixCase6(n, p, s)
    }
  }

  private void deletionFixCase6(final BinaryTreeNode<T> n, final BinaryTreeNode<T> p, final BinaryTreeNode<T> s) {
    if (p.left == n) {
      BinaryTrees.leftRotate(p)
      s.right.makeBlack() // it was red
    } else {
      BinaryTrees.rightRotate(p)
      s.left.makeBlack()
    }
    if (root == p) {
      root = s
    }

    s.black = p.black
    p.makeBlack()
  }

  private void insertionFix(BinaryTreeNode<T> node) {
    while (true) {
      if (node == root && !node.black) {
        node.makeBlack() // simple make it black

      } else if (!node.parent.black) {
        // 'node' to be fixed is always red
        // and since parent is red, grand parent must exist
        def uncle = BinaryTrees.sibling(node.parent)
        if (!uncle.present) {
          throw new AssertionError("non-root node should always have sibling in a RB tree")
        }

        if (node.parent == node.parent.parent.left) {
          node = insertionFixInternal(node, uncle.get(), LR.left)
        } else {
          node = insertionFixInternal(node, uncle.get(), LR.right)
        }

      } else {
        break // stop
      }
    }
  }

  private BinaryTreeNode<T> insertionFixInternal(final BinaryTreeNode<T> node, final BinaryTreeNode<T> uncle, LR side) {
    if (uncle.black) { // case 2 or case 3 - uncle is black
      switch (side) {
        case LR.left:
          return insertionFixLL(node, node.parent)
        case LR.right:
          return insertionFixRR(node, node.parent)
      }
    } else { // case 1 - parent and uncle are both red
      node.parent.makeBlack()
      uncle.makeBlack()
      node.parent.parent.makeRed() // it can be red now because both parent and uncle are black
      return node.parent.parent // next loop
    }
    throw new AssertionError("should not reach here")
  }

  private BinaryTreeNode<T> insertionFixLL(final BinaryTreeNode<T> node,
                                           final BinaryTreeNode<T> parent) {
    if (parent.right == node) { // case 2 - node is right child
      BinaryTrees.leftRotate(parent) // this actually transform into case 3
      // parent-child relationship is reversed after rotation
      return parent

    } else { // case 3 - node is left child
      final pp = parent.parent
      final newPP = BinaryTrees.rightRotate(pp)
      if (pp == root) { // this rotation may modify the root of tree
        root = newPP
      }
      pp.makeRed() // is now the child of parent
      parent.makeBlack() // now is the new sub root, and is still the parent of 'node'
      return node // not creating any new violation, thus terminating the loop
    }
  }

  private BinaryTreeNode<T> insertionFixRR(final BinaryTreeNode<T> node,
                                           final BinaryTreeNode<T> parent) {
    // The mirror image of insertionFixLL
    if (parent.left == node) {
      BinaryTrees.rightRotate(parent)
      return parent

    } else {
      final pp = parent.parent
      final newPP = BinaryTrees.leftRotate(pp)
      if (pp == root) {
        root = newPP
      }
      pp.makeRed()
      parent.makeBlack()
      return node
    }
  }

  boolean checkRoot() {
    if (root != null && !root.black) {
      throw new IllegalStateException("Violation: root node is black")
    }
    true
  }

  boolean checkBlackHeight() {
    Set<Integer> bhs = new HashSet<>()
    BinaryTrees.postOrderWalk(root, {
      node, depth ->
        if (node == sentinel) {
          bhs.add(blackDepth(node.parent) + 1) // include the sentinel node as leaf
        }
    })
    if (bhs.size() != 1) {
      throw new IllegalStateException("Violation: Every path must contain the same number of black nodes")
    }
    true
  }

  private static int blackDepth(BinaryTreeNode<?> node) {
    int count = 0
    while (!nil(node)) { // don't count the sentinel as root's parent
      if (node.black) {
        count += 1
      }
      node = node.parent
    }
    return count
  }

  /**
   * Check if this property holds for all nodes:
   *   4. If a node is red, then both its children are black. (Section 13.1) Or, red node always has black parent
   *
   * @return one node that violates this property, or null if all nodes are good. If multiple nodes violate, only one of them is returned.
   */
  boolean checkRedBlack() {
    def violation = new AtomicReference<BinaryTreeNode<T>>()
    BinaryTrees.postOrderWalk(root, { node, depth ->
      if (!node.black) {
        // we have null child pointer pointing to the sentinel, which is black
        if (!(node.left.black && node.right.black)) {
          violation.set(node)
          throw new BinaryTrees.StopWalkException()
        }
      }

      if (node.key == null && !node.black) {
        throw new IllegalStateException("Violation: sentinel node should be black")
      }
    })
    if (violation.get() != null) {
      throw new IllegalStateException("Violation: red node can't have red parent")
    }
    true
  }

  private static BinaryTreeNode<T> blackNode(T key) {
    def node = new BinaryTreeNode(key)
    node.makeBlack()
    node
  }

  private static enum LR {
    left, right
  }
}
