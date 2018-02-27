package yangyd.algo.datastructure

class RedBlackTreeNode<T> extends BinaryTreeNode<T> {
  Color color

  static <T> RedBlackTreeNode<T> red(T key) {
    return new RedBlackTreeNode<T>(key, Color.red)
  }

  static <T> RedBlackTreeNode<T> black(T key) {
    return new RedBlackTreeNode<T>(key, Color.black)
  }

  private RedBlackTreeNode(T key, Color color) {
    super(key)
    this.color = color
  }

  static enum Color {
    red, black
  }
}
