package yangyd.algo.datastructure

class BinaryTreeNode<T> {
  T key
  BinaryTreeNode<T> parent
  BinaryTreeNode<T> left
  BinaryTreeNode<T> right

  BinaryTreeNode(T key) {
    this.key = key
  }

  static <T> BinaryTreeNode newLeftChild(BinaryTreeNode parent, T childKey) {
    if (parent == null || parent.left != null) {
      throw new IllegalArgumentException("bad parent or child already exists")
    }
    def child = new BinaryTreeNode(childKey)
    child.parent = parent
    parent.left = child
    child
  }

  static <T> BinaryTreeNode newRightChild(BinaryTreeNode parent, T childKey) {
    if (parent == null || parent.right != null) {
      throw new IllegalArgumentException("bad parent or child already exists")
    }
    def child = new BinaryTreeNode(childKey)
    child.parent = parent
    parent.right = child
    child
  }

  @Override
  public String toString() {
    return "BinaryTreeNode{" +
        "key='" + key + '\'' +
        '}';
  }
}
