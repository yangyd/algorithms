package yangyd.algo.fixture

import yangyd.algo.datastructure.BinaryTreeNode

import java.security.SecureRandom

class Trees {
  static final sample_bst = figure_12_2()

  static List<Integer> randomList(int n) {
    SecureRandom random = new SecureRandom()
    if (n < 5 || n > 99) {
      n = random.nextInt(89) + 10
    }

    int[] numbers = range(n)
    for (int i = 0; i < n; i++) {
      int j = random.nextInt(n)
      swap(numbers, i, j)
    }

    List<Integer> list = new LinkedList<>()
    for (int i = 0; i < n; i++) {
      list.add(numbers[i])
    }
    list
  }

  private static void swap(int[] arr, int i, int j) {
    int tmp = arr[i]
    arr[i] = arr[j]
    arr[j] = tmp
  }

  private static int[] range(int n) {
    int[] arr = new int[n]
    for (int i = 0; i < n; i += 1) {
      arr[i] = i + 1
    }
    return arr
  }

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
