package yangyd.algo.datastructure

class BinaryTreeNode<T> {
  T key
  BinaryTreeNode<T> parent
  BinaryTreeNode<T> left
  BinaryTreeNode<T> right
  boolean black

  void makeRed() {
    black = false
  }

  void makeBlack() {
    black = true
  }

  BinaryTreeNode(T key) {
    this.key = key
  }

  static boolean nil(BinaryTreeNode<?> node) {
    node == null || node.key == null
  }

  void copyFrom(BinaryTreeNode<T> other) {
    if (other == null) {
      throw new IllegalArgumentException("can't copy from null node")
    }
    key = other.key
  }

  /**
   * Create a new left child with the given key. Will fail if the node already has a left child.
   * @param childKey
   * @return newly created left child node.
   */
  BinaryTreeNode<T> newLeftChild(T childKey) {
    if (this.left != null && this.left.key != null) { // only allow override of RB tree sentinel node
      throw new IllegalStateException("left child already exists")
    }
    def child = new BinaryTreeNode<T>(childKey)
    child.parent = this
    this.left = child
    child
  }

  /**
   * Create a new right child with the given key. Will fail if the node already has a right child.
   * @param childKey
   * @return newly created right child node.
   */
  BinaryTreeNode<T> newRightChild(T childKey) {
    if (this.right != null && this.right.key != null) { // only allow override of RB tree sentinel node
      throw new IllegalStateException("right child already exists")
    }
    def child = new BinaryTreeNode<T>(childKey)
    child.parent = this
    this.right = child
    child
  }

  @Override
  String toString() {
    if (key == null) {
      "Node[Sentinel]"
    } else {
      "Node[$key]" + (black ? "(black)" : "()") + "<${this.left?.key}, ${this.right?.key}>"
    }
  }
}
