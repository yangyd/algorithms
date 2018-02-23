package yangyd.algo.ch12

import spock.lang.Specification
import yangyd.algo.fixture.Trees

class BinaryTreesSpec extends Specification {

  def "in-order tree walk access BST elements in ascending order"() {
    when:
    def list = new LinkedList<Integer>()
    BinaryTrees.inOrderWalk(Trees.sample_bst, {
      node, depth -> list.add(node.key)
    })

    then:
    list == [2,3,4,6,7,9,13,15,17,18,20]
  }

  def "elements can also be traversed pre-order or post-order"() {
    when:
    def preList = new LinkedList<Integer>()
    def postList = new LinkedList<Integer>()

    BinaryTrees.preOrderWalk(Trees.sample_bst, {
      node, depth -> preList.add(node.key)
    })
    BinaryTrees.postOrderWalk(Trees.sample_bst, {
      node, depth -> postList.add(node.key)
    })

    then:
    preList == [15,6,3,2,4,7,13,9,18,17,20]
    postList == [2,4,3,9,13,7,6,17,20,18,15]
  }

  def "calculate the depth of binary tree"() {
    when:
    def d = BinaryTrees.depth(Trees.sample_bst)

    then:
    d == 5
  }
}
