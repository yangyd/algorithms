package yangyd.algo.datastructure

class BinaryTreeNode<T> {
  T key
  BinaryTreeNode<T> parent
  BinaryTreeNode<T> left
  BinaryTreeNode<T> right

  BinaryTreeNode(T key) {
    this.key = key
  }

  @Override
  String toString() {
    return "BinaryTreeNode{" +
        "key='" + key + '\'' +
        '}'
  }
}
