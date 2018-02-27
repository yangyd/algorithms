package yangyd.algo.fixture

import yangyd.algo.datastructure.BinaryTreeNode

class Trees {
  static final sample_bst = figure_12_2()

  private static BinaryTreeNode<Integer> figure_12_2() {
    def n15 = new BinaryTreeNode(15)
    def n6 = n15.newLeftChild(6)
    def n18 = n15.newRightChild(18)

    def n3 = n6.newLeftChild(3)
    def n7 = n6.newRightChild(7)

    def n17 = n18.newLeftChild(17)
    def n20 = n18.newRightChild(20)

    n3.newLeftChild(2)
    n3.newRightChild(4)

    def n13 = n7.newRightChild(13)
    n13.newLeftChild(9)
    return n15
  }

  static BinaryTreeNode<String> rotate_example_left() {
    def y = new BinaryTreeNode("y")
    def x = y.newLeftChild("x")
    def a = x.newLeftChild("a")
    def b = x.newRightChild("b")
    def r = y.newRightChild("r")
    return y
  }

  static BinaryTreeNode<String> rotate_example_right() {
    def x = new BinaryTreeNode("x")
    def y = x.newRightChild("y")
    def a = x.newLeftChild("a")
    def b = y.newLeftChild("b")
    def r = y.newRightChild("r")
    return x
  }
}
