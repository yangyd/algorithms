package yangyd.algo.ch12

import spock.lang.Specification
import yangyd.algo.datastructure.BinaryTreeNode
import yangyd.algo.fixture.Trees

class BinarySearchTreeSpec extends Specification {
  static BinaryTreeNode<Integer> root

  def setupSpec() {
    root = Trees.sample_bst
  }

  def "search a binary search tree"() {
    expect:
    exist == BinarySearchTrees.search(root, key).present

    where:
    exist | key
    true  | 4
    false | 5
    true  | 13
    true  | 17
    false | 19
    true  | 20
  }

  def "find the minimum element in a binary search tree"() {
    expect:
    min == BinarySearchTrees.minimum(root)

    where:
    min | _
    2   | _
  }


  def "find the maximum element in a binary search tree"() {
    expect:
    max == BinarySearchTrees.maximum(root)

    where:
    max | _
    20  | _
  }

  def "find the successor of an element in a BST"() {
    expect:
    s == BinarySearchTrees.successor(root, k).orElseGet({null})

    where:
    s    | k
    9    | 7
    7    | 6
    17   | 15
    15   | 13
    18   | 17
    null | 20
  }

  def "find the predecessor of an element in a BST"() {
    expect:
    p == BinarySearchTrees.predecessor(root, k).orElseGet({null})

    where:
    p  | k
    4  | 6
    6  | 7
    7  | 9
    9  | 13
    13 | 15
    15 | 17
    17 | 18
    18 | 20
  }
}
