package yangyd.algo.fixture

import yangyd.algo.datastructure.BinaryTreeNode
import static yangyd.algo.datastructure.BinaryTreeNode.*

class Trees {
  static final sample_bst = figure_12_2()
  static BinaryTreeNode<Integer> figure_12_2() {
    def n15 = new BinaryTreeNode(15)
    def n6 = newLeftChild(n15, 6)
    def n18 = newRightChild(n15, 18)

    def n3 = newLeftChild(n6, 3)
    def n7 = newRightChild(n6, 7)

    def n17 = newLeftChild(n18, 17)
    def n20 = newRightChild(n18, 20)

    newLeftChild(n3, 2)
    newRightChild(n3, 4)

    def n13 = newRightChild(n7, 13)
    newLeftChild(n13, 9)

    n15
  }

}
