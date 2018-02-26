package yangyd.algo.ch12

import yangyd.algo.datastructure.BinaryTreeNode

import static yangyd.algo.ch12.BinaryTrees.*

/**
 * This class contains operations on a Binary Search Tree.
 * for simplicity, assuming the parameter {@code root} for each method is always not null
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

  /**
   * Collect elements in the BST in ascending order
   * @param root
   * @return list of elements in the BST in ascending order
   */
  static <T> List<T> collectSorted(BinaryTreeNode<T> root) {
    def list = new LinkedList<T>()
    inOrderWalk(root, {
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
        newLeftChild(parent, key)
      } else {
        newRightChild(parent, key)
      }
    } else {
      throw new AssertionError("root should not be null")
    }
  }

  /**
   * delete the given key from the BST. Return the new root after deletion. The new root is different than the old root <i>i.i.f.</i> the old root is deleted,
   * and is nothing if deleted root is the only node in the tree.
   * @param root
   * @param key
   * @return the new root, may be nothing
   */
  static <T extends Comparable<T>> Optional<BinaryTreeNode<T>> delete(BinaryTreeNode<T> root, T key) {
    final toDelete = findNode(root, key)

    if (toDelete.left == null) {
      return replaceNode(root, toDelete, toDelete.right)
    } else if (toDelete.right == null) {
      return replaceNode(root, toDelete, toDelete.left)
    } else {
      deleteWithTwoChildren(root, toDelete)
    }
  }

  /**
   * the deletion when the node has both left and right children.
   * basically we search the right sub-tree of {@code toDelete}, find a node which has no left child,
   * and use that node as the replacement of {@code toDelete}.
   *
   * For detailed explanation, see Section 12.3
   */
  private static <T extends Comparable<T>> Optional<BinaryTreeNode<T>> deleteWithTwoChildren(BinaryTreeNode<T> root,
                                                                                             BinaryTreeNode<T> toDelete)
  {
    final rightSubRoot = toDelete.right
    final leftSubRoot = toDelete.left

    // simple case: rightSubRoot doesn't has left child
    if (rightSubRoot.left == null) {
      rightSubRoot.left = leftSubRoot // move left sub tree down to the empty slot of rightSubRoot
      leftSubRoot.parent = rightSubRoot
      toDelete.left = null
      return replaceNode(root, toDelete, rightSubRoot)
    }

    // an obvious candidate is the successor of toDelete (exercise 12.2-5)
    // we could have called successor() here, if it wasn't returning the key instead of the node
    def elected = rightSubRoot
    while (elected.left != null) {
      elected = elected.left
    }

    // now detach the elected replacement from the tree, replace it with its right sub-tree
    replaceNode(root, elected, elected.right) // root will not change here

    // now the elected node is clean of children.
    // make it the parent of original leftSubRoot and rightSubRoot
    elected.left = leftSubRoot
    leftSubRoot.parent = elected
    toDelete.left = null
    elected.right = rightSubRoot
    rightSubRoot.parent = elected
    toDelete.right = null

    // finally, delete the toDelete node and replace it with elected replacement
    return replaceNode(root, toDelete, elected)
  }


  private static <T> BinaryTreeNode<T> findNode(BinaryTreeNode<T> root, T key) {
    final nodeOption = search(root, key)
    if (nodeOption.present) {
      return nodeOption.get()
    } else {
      throw new NoSuchElementException("key $key doesn't exist in the tree")
    }
  }

}
