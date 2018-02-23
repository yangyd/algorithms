package yangyd.algo.ch12

import yangyd.algo.datastructure.BinaryTreeNode

import java.util.concurrent.atomic.AtomicInteger
import java.util.function.BiConsumer

/**
 * This class contains general operations on a Binary Tree.
 */
class BinaryTrees {

  /**
   * Create a new left child to {@code parent} with the given key. Will fail if the {@code parent} already has left child.
   * @param parent
   * @param childKey
   * @return newly created left child node.
   */
  static <T> BinaryTreeNode<T> newLeftChild(BinaryTreeNode parent, T childKey) {
    if (parent == null || parent.left != null) {
      throw new IllegalArgumentException("bad parent or child already exists")
    }
    def child = new BinaryTreeNode<T>(childKey)
    child.parent = parent
    parent.left = child
    child
  }

  /**
   * Create a new right child to {@code parent} with the given key. Will fail if the {@code parent} already has right child.
   * @param parent
   * @param childKey
   * @return newly created right child node.
   */
  static <T> BinaryTreeNode<T> newRightChild(BinaryTreeNode parent, T childKey) {
    if (parent == null || parent.right != null) {
      throw new IllegalArgumentException("bad parent or child already exists")
    }
    def child = new BinaryTreeNode<T>(childKey)
    child.parent = parent
    parent.right = child
    child
  }


  /**
   * <p>
   * in the Binary tree rooted at {@code root}, remove the node {@code toDelete}, then put the subtree rooted at {@code replacement} in its place.
   * the {@code replacement} can be null, in which case the node {@code toDelete} is simply removed.
   * </p>
   * <p>
   *   if {@code toDelete} is {@code root}, its child pointers will be set to null after this method, otherwise, it is just detached from the tree.
   * </p>
   * @param root the root of the binary tree. may not be null.
   * @param toDelete the node to delete. may not be null, may be same as {@code root}
   *
   * @return the new root of the tree. it may be nothing if the only node (which is root) in the tree is deleted.
   */
  static <T> Optional<BinaryTreeNode<T>> replaceNode(BinaryTreeNode<T> root,
                                                             BinaryTreeNode<T> toDelete,
                                                             BinaryTreeNode<T> replacement)
  {
    if (root == null || toDelete == null) {
      throw new IllegalArgumentException("root and toDelete may not be null")
    }

    if (toDelete == root) {
      toDelete.left = null
      toDelete.right = null
      if (replacement != null) {
        replacement.parent = null
      }
      return Optional.ofNullable(replacement)
    } else {
      replaceSubTree(toDelete, replacement)
      return Optional.of(root)
    }
  }


  /**
   * replace the sub-tree rooted at the {@code oldNode} with the one at {@code newNode}.
   * if {@code newNode} is null, the sub tree is removed.
   * do nothing if the {@code oldNode} is not a <i>sub tree</i> root.
   */
  private static void replaceSubTree(final BinaryTreeNode<?> oldNode, final BinaryTreeNode<?> newNode) {
    if (oldNode == null || oldNode.parent == null) {
      return
    }
    def parent = oldNode.parent

    if (parent.left == oldNode) {
      parent.left = newNode
    } else if (parent.right == oldNode) {
      parent.right = newNode
    } else {
      return
    }

    oldNode.parent = null
    if (newNode != null) {
      newNode.parent = parent
    }
  }

  static int depth(BinaryTreeNode<?> root) {
    final counter = new AtomicInteger(0)
    postOrderWalk(root, { n, d ->
      if (d > counter.get()) {
        counter.set(d)
      }
    })
    return counter.get()
  }

  static <T> void inOrderWalk(BinaryTreeNode<T> root,
                                BiConsumer<BinaryTreeNode<T>, Integer> visitor) {
    inW(root, 1, visitor) // root node at depth 1
  }


  static <T> void preOrderWalk(BinaryTreeNode<T> root,
                                BiConsumer<BinaryTreeNode<T>, Integer> visitor) {
    preW(root, 1, visitor)
  }

  static <T> void postOrderWalk(BinaryTreeNode<T> root,
                                BiConsumer<BinaryTreeNode<T>, Integer> visitor) {
    postW(root, 1, visitor)
  }

  private static <T> void inW(BinaryTreeNode<T> node,
                              int depth,
                              BiConsumer<BinaryTreeNode<T>, Integer> visitor) {
    if (node != null) {
      inW(node.left, depth + 1, visitor)
      visitor.accept(node, depth)
      inW(node.right, depth + 1, visitor)
    }
  }

  private static <T> void preW(BinaryTreeNode<T> node,
                              int depth,
                              BiConsumer<BinaryTreeNode<T>, Integer> visitor) {
    if (node != null) {
      visitor.accept(node, depth)
      preW(node.left, depth + 1, visitor)
      preW(node.right, depth + 1, visitor)
    }
  }

  private static <T> void postW(BinaryTreeNode<T> node,
                              int depth,
                              BiConsumer<BinaryTreeNode<T>, Integer> visitor) {
    if (node != null) {
      postW(node.left, depth + 1, visitor)
      postW(node.right, depth + 1, visitor)
      visitor.accept(node, depth)
    }
  }
}
