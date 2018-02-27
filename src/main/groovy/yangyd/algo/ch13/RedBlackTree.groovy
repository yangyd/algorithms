package yangyd.algo.ch13

import yangyd.algo.datastructure.RedBlackTreeNode

class RedBlackTree<T> {
  private RedBlackTreeNode<T> root
  private RedBlackTreeNode<T> sentinel // common leaf of all nodes

  RedBlackTree(T rootKey) {
    root = RedBlackTreeNode.black(rootKey)
    sentinel = RedBlackTreeNode.black(null)
  }
}
