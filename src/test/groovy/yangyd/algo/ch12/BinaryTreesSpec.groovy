package yangyd.algo.ch12

import spock.lang.Specification
import yangyd.algo.fixture.Trees

class BinaryTreesSpec extends Specification {

  def "in-order tree walk access elements from left to right"() {
    expect:
    BinaryTrees.collectInOrder(Trees.sample_bst) == [2, 3, 4, 6, 7, 9, 13, 15, 17, 18, 20]
  }

  def "elements can also be traversed pre-order or post-order"() {
    expect:
    BinaryTrees.collectPreOrder(Trees.sample_bst) == [15, 6, 3, 2, 4, 7, 13, 9, 18, 17, 20]
    BinaryTrees.collectPostOrder(Trees.sample_bst) == [2, 4, 3, 9, 13, 7, 6, 17, 20, 18, 15]
  }

  def "calculate the depth of binary tree"() {
    when:
    def d = BinaryTrees.depth(Trees.sample_bst)

    then:
    d == 5
  }

  def "right rotate, before"() {
    when:
    def list_in = BinaryTrees.collectInOrder(Trees.rotate_example_left())
    def list_pre = BinaryTrees.collectPreOrder(Trees.rotate_example_left())
    then:
    String.join("", list_in) == "axbyr"
    String.join("", list_pre) == "yxabr"
  }

  def "right rotate, after"() {
    when:
    def original = Trees.rotate_example_left()
    def rotated = BinaryTrees.rightRotate(original)
    def list_in = BinaryTrees.collectInOrder(rotated)
    def list_pre = BinaryTrees.collectPreOrder(rotated)

    then:
    String.join("", list_in) == "axbyr"
    String.join("", list_pre) == "xaybr"
  }


  def "left rotate, before"() {
    when:
    def list_in = BinaryTrees.collectInOrder(Trees.rotate_example_right())
    def list_pre = BinaryTrees.collectPreOrder(Trees.rotate_example_right())
    then:
    String.join("", list_in) == "axbyr"
    String.join("", list_pre) == "xaybr"
  }

  def "left rotate, after"() {
    when:
    def original = Trees.rotate_example_right()
    def rotated = BinaryTrees.leftRotate(original)
    def list_in = BinaryTrees.collectInOrder(rotated)
    def list_pre = BinaryTrees.collectPreOrder(rotated)

    then:
    String.join("", list_in) == "axbyr"
    String.join("", list_pre) == "yxabr"
  }
}
