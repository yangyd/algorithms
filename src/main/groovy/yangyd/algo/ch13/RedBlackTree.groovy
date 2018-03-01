package yangyd.algo.ch13

import yangyd.algo.ch12.BinarySearchTrees
import yangyd.algo.ch12.BinaryTrees
import yangyd.algo.datastructure.BinaryTreeNode

import java.util.concurrent.atomic.AtomicReference

import static yangyd.algo.datastructure.RedBlack.black
import static yangyd.algo.datastructure.RedBlack.red

class RedBlackTree<T> {
  private BinaryTreeNode<T> root
  private final BinaryTreeNode<T> sentinel = blackNode(null)// common leaf of all nodes

  void insert(T key) {
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

  int height() {
    root == null ? 0 : BinaryTrees.depth(root)
  }

  List<T> collect() {
    root == null ? Collections.emptyList() : BinarySearchTrees.collectSorted(root)
  }

  void print() {
    if (root != null) {
      BinaryTrees.preOrderWalk(root, {
        node, depth -> if (!BinaryTrees.nil(node)) {
          println(node)
        }
      })
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

  /**
   * Check if this property holds for all nodes:
   *   4. If a node is red, then both its children are black. (Section 13.1) Or, red node always has black parent
   *
   * @return one node that violates this property, or null if all nodes are good. If multiple nodes violate, only one of them is returned.
   */
  BinaryTreeNode<T> checkRedBlack() {
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
      return violation.get()
    } else {
      return null
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
