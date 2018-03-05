package yangyd.algo.ch13

import yangyd.algo.ch12.BinarySearchTrees
import yangyd.algo.ch12.BinaryTrees
import yangyd.algo.datastructure.BinaryTreeNode

import java.util.concurrent.atomic.AtomicReference

import static yangyd.algo.datastructure.BinaryTreeNode.nil
import static yangyd.algo.datastructure.RedBlack.black
import static yangyd.algo.datastructure.RedBlack.red

class RedBlackTree<T> {
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
        node, depth -> if (!nil(node)) {
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
      node.color = red
      fixAfterInsert(node)
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
    deleteByCase(o.get())
  }

  private void deleteByCase(BinaryTreeNode<T> node) {
    // An improved deletion procedure utilizing BinaryTreeNode.copyFrom().
    // It is simpler than what's implemented in BinarySearchTrees.delete().

    final noleft = nil(node.left)
    final noright = nil(node.right)

    if (noleft && noright) {
      if (node == root) { // the only node
        root = null
        sentinel.left = sentinel.right = sentinel.parent = null // paranoia
      } else {
        delete0(node) // deleting a dangling node is surprisingly the most complex case
      }

    } else if (noleft) {
      checkDelete1(node, node.right)
      final c = deleteWithOnlyRightChild(node)
      c.color = black // we replaced a black node with its red child, so simply painting it black can preserve RB tree properties

    } else if (noright) {
      checkDelete1(node, node.left)
      final c = deleteWithOnlyLeftChild(node)
      c.color = black

    } else {
      // to delete node with two children, we find its successor which has at most 1 child
      final elected = successor(node)
      node.copyFrom(elected) // copy its content to node
      deleteByCase(elected) // now simply delete the successor
    }
  }

  private static void checkDelete1(BinaryTreeNode<?> toDelete, BinaryTreeNode<?> child) {
    if (toDelete.color != black) {
      throw new AssertionError("Violation: red node can not has just one non-sentinel child")
    }
    if (child.color != red) {
      throw new AssertionError("Violation: if a black node has exactly one non-sentinel black child, it must be red")
    }
  }

  private BinaryTreeNode<T> deleteWithOnlyLeftChild(BinaryTreeNode<T> toDelete) {
    final l = toDelete.left
    final p = toDelete.parent
    if (toDelete == root) {
      root = l
    } else {
      p.left = l
    }
    l.parent = p // p is sentinel in case of root
    toDelete.parent = null
    toDelete.left = null
    return l
  }

  private BinaryTreeNode<T> deleteWithOnlyRightChild(BinaryTreeNode<T> toDelete) {
    final r = toDelete.right
    final p = toDelete.parent
    if (toDelete == root) {
      root = r
    } else {
      p.right = r
    }
    r.parent = p
    toDelete.parent = null
    toDelete.right = null
    return r
  }

  /**
   * simplified procedure of finding successor for deletion
   */
  private static BinaryTreeNode<T> successor(BinaryTreeNode<T> node) {
    def pp = null
    def p = node.right
    while (p != null) {
      pp = p
      p = p.left
    }
    pp
  }

  private void delete0(BinaryTreeNode<T> node) {
    // node is not root
    final p = node.parent
    if (p.left == node) {
      final s = p.right // sibling of node, must be either dangling or red
      node.parent = null
      p.left = sentinel

      if (s.color == red) {
        BinaryTrees.leftRotate(p)
        // now s is in p's old place
        // case 4
      } else {
        // s is dangling
        // case 3
      }



    } else if (p.right == node) {
      final s = p.left


    } else {
      throw new IllegalStateException("broken tree structure") // impossible
    }
  }

  private void fixAfterInsert(BinaryTreeNode<T> node) {
    while (true) {
      if (node == root && node.color == red) {
        node.color = black // simply make it black

      } else if (node.parent.color == red) {
        // 'node' to be fixed is always red
        // and since parent is red, grand parent must exist
        def uncle = BinaryTrees.sibling(node.parent)
        if (!uncle.present) {
          throw new AssertionError("non-root node should always have sibling in a RB tree")
        }

        if (node.parent == node.parent.parent.left) {
          node = fix_internal(node, uncle.get(), LR.left)
        } else {
          node = fix_internal(node, uncle.get(), LR.right)
        }

      } else {
        break // stop
      }
    }
  }

  private BinaryTreeNode<T> fix_internal(final BinaryTreeNode<T> node, final BinaryTreeNode<T> uncle, LR side) {
    switch (uncle.color) {
      case red: // case 1 - parent and uncle are both red
        node.parent.color = black
        uncle.color = black
        node.parent.parent.color = red // it can be red now because both parent and uncle are black
        return node.parent.parent // next loop

      case black: // case 2 or case 3 - uncle is black
        switch (side) {
          case LR.left:
            return fix_LL(node, node.parent)
          case LR.right:
            return fix_RR(node, node.parent)
        }
        break
    } // end switch uncle's color
    throw new AssertionError("should not reach here")
  }

  private BinaryTreeNode<T> fix_LL(final BinaryTreeNode<T> node,
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
      pp.color = red // is now the child of parent
      parent.color = black // now is the new sub root, and is still the parent of 'node'
      return node // not creating any new violation, thus terminating the loop
    }
  }

  private BinaryTreeNode<T> fix_RR(final BinaryTreeNode<T> node,
                                   final BinaryTreeNode<T> parent) {
    // The mirror image of fix_LL
    if (parent.left == node) {
      BinaryTrees.rightRotate(parent)
      return parent

    } else {
      final pp = parent.parent
      final newPP = BinaryTrees.leftRotate(pp)
      if (pp == root) {
        root = newPP
      }
      pp.color = red
      parent.color = black
      return node
    }
  }

  void checkRoot() {
    if (root != null && root.color != black) {
      throw new IllegalStateException("Violation: root node is black")
    }
  }

  void checkBlackHeight() {
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
  }

  private static int blackDepth(BinaryTreeNode<?> node) {
    int count = 0
    while (!nil(node)) { // don't count the sentinel as root's parent
      if (node.color == black) {
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
  void checkRedBlack() {
    def violation = new AtomicReference<BinaryTreeNode<T>>()
    BinaryTrees.postOrderWalk(root, { node, depth ->
      if (node.color == red) {
        // we have null child pointer pointing to the sentinel, which is black
        if (node.left.color == red || node.right.color == red) {
          violation.set(node)
          throw new BinaryTrees.StopWalkException()
        }
      }
    })
    if (violation.get() != null) {
      throw new IllegalStateException("Violation: red node can't have red parent")
    }
  }

  private static BinaryTreeNode<T> blackNode(T key) {
    def node = new BinaryTreeNode(key)
    node.color = black
    node
  }

  private static enum LR {
    left, right
  }

//  private static void flip(BinaryTreeNode<T> node) {
//    if (node.color == red) {
//      node.color = black
//    } else if (node.color == black) {
//      node.color = red
//    }
//  }
}
