package yangyd.algo.ch12

import yangyd.algo.datastructure.BinaryTreeNode

import java.util.concurrent.atomic.AtomicInteger
import java.util.function.BiConsumer

import static yangyd.algo.datastructure.BinaryTreeNode.nil

/**
 * This class contains general operations on a Binary Tree.
 */
class BinaryTrees {

  /**
   * find the sibling of a node in an binary tree
   * @param node
   * @return
   */
  static <T> Optional<BinaryTreeNode<T>> sibling(BinaryTreeNode<T> node) {
    if (node == null || node.parent == null) {
      return Optional.empty()
    }
    if (node.parent.left == node) {
      return Optional.ofNullable(node.parent.right)
    } else if (node.parent.right == node) {
      return Optional.ofNullable(node.parent.left)
    } else {
      return Optional.empty()
    }
  }

  /**
   * Right rotation operations on a binary tree. (Figure 13.2)
   * @param subRoot the sub-tree root on which the rotation is performed
   * @return the new node in the place previously held by subRoot. Its parent is set to the same parent of subRoot before the rotation.
   */
  static <T> BinaryTreeNode<T> rightRotate(BinaryTreeNode<T> subRoot) {
    if (subRoot == null || subRoot.left == null) {
      throw new IllegalArgumentException("subRoot and its left child must not be null")
    }
    final parent = subRoot.parent
    final y = subRoot
    final x = subRoot.left
    final b = x.right

    y.parent = x
    x.right = y

    y.left = b
    if (b != null) {
      b.parent = y
    }

    fixRotate(parent, y, x)
    return x
  }

  /**
   * Left rotation operations on a binary tree. (Figure 13.2)
   * @param subRoot the sub-tree root on which the rotation is performed
   * @return the new node in the place previously held by subRoot. Its parent is set to the same parent of subRoot before the rotation.
   */
  static <T> BinaryTreeNode<T> leftRotate(BinaryTreeNode<T> subRoot) {
    if (subRoot == null || subRoot.right == null) {
      throw new IllegalArgumentException("subRoot and its right child must not be null")
    }
    final parent = subRoot.parent
    final x = subRoot
    final y = subRoot.right
    final b = y.left

    x.parent = y
    y.left = x

    x.right = b
    if (b != null) {
      b.parent = x
    }

    fixRotate(parent, x, y)
    return  y
  }

  private static void fixRotate(BinaryTreeNode<?> parent, BinaryTreeNode<?> subRoot, BinaryTreeNode<?> newSubRoot) {
    newSubRoot.parent = parent
    if (!nil(parent)) { // don't set if parent is RB tree sentinel
      if (parent.left == subRoot) {
        parent.left = newSubRoot
      } else if (parent.right == subRoot) {
        parent.right = newSubRoot
      }
    }
  }

  /**
   * <p>
   *   in the Binary tree rooted at {@code root}, remove the node {@code toDelete}, then put the subtree rooted at {@code replacement} in its place.
   *   the {@code replacement} can be null, in which case the node {@code toDelete} is simply removed.
   *   Note that the subtree of toDelete is removed as well, it will not be transplanted to the replacement node.
   * </p>
   * <p>
   *   if {@code toDelete} is {@code root}, its child pointers will be set to null after this method.
   *   Otherwise, it is just detached from the tree.
   * </p>
   * @param root the root of the binary tree. may not be null.
   * @param toDelete the node to delete. may not be null, may be same as {@code root}
   *
   * @return the new root of the tree. it may be nothing if the only node (which is root) in the tree is deleted.
   */
  static <T> Optional<BinaryTreeNode<T>> replaceSubTree(BinaryTreeNode<T> root,
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
      replaceSubTree0(toDelete, replacement)
      return Optional.of(root)
    }
  }


  /**
   * replace the sub-tree rooted at the {@code oldNode} with the one at {@code newNode}.
   * if {@code newNode} is null, the sub tree is removed.
   * do nothing if the {@code oldNode} is not a <i>sub tree</i> root.
   */
  private static void replaceSubTree0(final BinaryTreeNode<?> oldNode, final BinaryTreeNode<?> newNode) {
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
    if (!nil(newNode)) { // in case of RB tree sentinel node, don't modify the parent
      newNode.parent = parent
    }
  }

  static int depth(BinaryTreeNode<?> root) {
    final counter = new AtomicInteger(0)
    postOrderWalk(root, { n, d ->
      if (!nil(n) && d > counter.get()) { // don't count RB tree sentinel
        counter.set(d)
      }
    })
    return counter.get()
  }

  static <T> List<T> collectInOrder(BinaryTreeNode<T> root) {
    def list = new LinkedList<T>()
    inOrderWalk(root, { node, depth -> if (!nil(node)) list.add(node.key) })
    Collections.unmodifiableList(list)
  }

  static <T> List<T> collectPreOrder(BinaryTreeNode<T> root) {
    def list = new LinkedList<T>()
    preOrderWalk(root, { node, depth -> if (!nil(node)) list.add(node.key) })
    Collections.unmodifiableList(list)
  }

  static <T> List<T> collectPostOrder(BinaryTreeNode<T> root) {
    def list = new LinkedList<T>()
    postOrderWalk(root, { node, depth -> if (!nil(node)) list.add(node.key) })
    Collections.unmodifiableList(list)
  }

  static <T> void inOrderWalk(BinaryTreeNode<T> root,
                                BiConsumer<BinaryTreeNode<T>, Integer> visitor) {
    try {
      inW(root, 1, visitor) // root node at depth 1
    } catch (StopWalkException e) {
      // this is fine
    }
  }


  static <T> void preOrderWalk(BinaryTreeNode<T> root,
                                BiConsumer<BinaryTreeNode<T>, Integer> visitor) {
    try {
      preW(root, 1, visitor)
    } catch (StopWalkException e) {
      // this is fine
    }
  }

  static <T> void postOrderWalk(BinaryTreeNode<T> root,
                                BiConsumer<BinaryTreeNode<T>, Integer> visitor) {
    try {
      postW(root, 1, visitor)
    } catch (StopWalkException e) {
      // this is fine
    }
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

  static class StopWalkException extends RuntimeException {
  }
}
