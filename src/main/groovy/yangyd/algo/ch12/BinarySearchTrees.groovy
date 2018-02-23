package yangyd.algo.ch12

import yangyd.algo.datastructure.BinaryTreeNode
/**
 * for simplicity, assume root is always not null
 */
class BinarySearchTrees {
  static <T extends Comparable<T>> Optional<BinaryTreeNode<T>> search(BinaryTreeNode<T> root, T target) {
    if (target == null) {
      throw new IllegalArgumentException("target must not be null")
    }

    def node = root
    while (node != null) {
      def c = target <=> node.key
      if (c > 0) {
        node = node.right
      } else if (c < 0) {
        node = node.left
      } else {
        return Optional.of(node)
      }
    }
    return Optional.empty()
  }

  static <T> List<T> collect(BinaryTreeNode<T> root) {
    def list = new LinkedList<T>()
    BinaryTrees.inOrderWalk(root, {
      node, depth -> list.add(node.key)
    })
    Collections.unmodifiableList(list)
  }

  static <T> T maximum(BinaryTreeNode<T> root) {
    def node = root
    while (node.right != null) {
      node = node.right
    }
    node.key
  }

  static <T> T minimum(BinaryTreeNode<T> root) {
    def node = root
    while (node.left != null) {
      node = node.left
    }
    node.key
  }

  static <T> Optional<T> successor(BinaryTreeNode<T> root, T key) {
    def node = findNode(root, key)

    // minimum of right sub-tree, or one of its ancestor

    if (node.right != null) { // if has right sub-tree, search it
      return Optional.ofNullable(minimum(node.right))

    } else { // could also be one of the parent
      // point here is when going up, stop immediately when moved once from left side
      def parent = node.parent
      while (parent != null && node == parent.right) {
        node = parent
        parent = node.parent
      }
      return Optional.ofNullable(parent?.key) // parent could be null here (means we hit the top of tree)
    }
  }

  static <T> Optional<T> predecessor(BinaryTreeNode<T> root, T key) {
    def node = findNode(root, key)

    // maximum of left sub-tree, or one of its ancestor

    if (node.left != null) {
      return Optional.ofNullable(maximum(node.left))
    } else {
      def parent = node.parent
      while (parent != null && node == parent.left) {
        node = parent
        parent = node.parent
      }
      return Optional.ofNullable(parent?.key)
    }
  }

  static <T extends Comparable<T>> void insert(BinaryTreeNode<T> root, T key) {
    def node = root
    def parent = null
    def left = false
    while (node != null) {
      def c = key <=> node.key
      if (c > 0) {
        parent = node
        node = node.right
        left = false
      } else if (c < 0) {
        parent = node
        node = node.left
        left = true
      } else {
        throw new IllegalStateException("already exists")
      }
    }

    if (parent != null) {
      if (left) {
        BinaryTreeNode.newLeftChild(parent, key)
      } else {
        BinaryTreeNode.newRightChild(parent, key)
      }
    } else {
      throw new AssertionError("root should not be null")
    }
  }

  private static <T> BinaryTreeNode<T> findNode(BinaryTreeNode<T> root, T key) {
    final nodeOption = search(root, key)
    if (!nodeOption.present) {
      throw new NoSuchElementException("key must be present in the tree")
    }
    final node = nodeOption.get()
    node
  }

}
