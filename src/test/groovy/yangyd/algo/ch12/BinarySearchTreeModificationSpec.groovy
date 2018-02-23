package yangyd.algo.ch12

import spock.lang.Specification
import yangyd.algo.datastructure.BinaryTreeNode

class BinarySearchTreeModificationSpec extends Specification {
  static BinaryTreeNode<Integer> base
  def setupSpec() {
    base = new BinaryTreeNode<>(12)
  }

  def "insert 5,18"() {
    when:
    BinarySearchTrees.insert(base, 5)
    BinarySearchTrees.insert(base, 18)
    then:
    BinarySearchTrees.collectSorted(base) == [5, 12, 18]
  }

  def "insert 2,9,15,19"() {
    when:
    BinarySearchTrees.insert(base, 2)
    BinarySearchTrees.insert(base, 9)
    BinarySearchTrees.insert(base, 15)
    BinarySearchTrees.insert(base, 19)
    then:
    BinarySearchTrees.collectSorted(base) == [2, 5, 9, 12, 15, 18, 19]
  }

  def "insert 17,13"() {
    when:
    BinarySearchTrees.insert(base, 17)
    BinarySearchTrees.insert(base, 13)

    then:
    BinarySearchTrees.collectSorted(base) == [2, 5, 9, 12, 13, 15, 17, 18, 19]
  }

  def "check the shape of BST"() {
    when:
    def post = new LinkedList<Integer>()
    def pre = new LinkedList<Integer>()
    BinaryTrees.preOrderWalk(base, {
      node, depth -> pre.add(node.key)
    })
    BinaryTrees.postOrderWalk(base, {
      node, depth -> post.add(node.key)
    })

    then:
    pre == [12,5,2,9,18,15,13,17,19]
    post == [2,9,5,13,17,15,19,18,12]
  }

  def "delete 2,13"() {
    when:
    def noop = null

    then:
    BinarySearchTrees.collectSorted(base) == [5, 9, 12, 15, 17, 18, 19]
  }

}
