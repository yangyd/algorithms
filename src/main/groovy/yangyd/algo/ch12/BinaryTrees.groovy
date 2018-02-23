package yangyd.algo.ch12

import yangyd.algo.datastructure.BinaryTreeNode

import java.util.concurrent.atomic.AtomicInteger
import java.util.function.BiConsumer

class BinaryTrees {
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
