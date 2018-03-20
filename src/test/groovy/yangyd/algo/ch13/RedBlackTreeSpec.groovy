package yangyd.algo.ch13

import spock.lang.Shared
import spock.lang.Specification
import yangyd.algo.fixture.Trees

class RedBlackTreeSpec extends Specification {

  @Shared
  List<Integer> input
  @Shared
  RedBlackTree<Integer> tree
  @Shared
  int current

  def setupSpec() {
//    input = [4, 7, 8, 6, 9, 2, 1, 5, 3]
    input = Trees.randomList(27)
    tree = new RedBlackTree<>()
    current = 0
  }

  def "build an red-black tree"() {
    when:
    input.forEach { tree.insert(it) }
    tree.checkRedBlack()
    tree.checkBlackHeight()
    tree.checkRoot()
    println("input: $input")
    tree.print()

    then:
    notThrown IllegalStateException
    tree.collect() == input.toSorted()
    tree.height() <= 2 * log2(input.size() + 1) // Lemma 13.1: A red-black tree with n internal nodes has height at most 2lg(n+1)
  }

  def "build an red-black tree with reversely ordered input"() {
    when:
    def tree = new RedBlackTree()
    def input = Trees.randomList(83).toSorted()
    input.reverse().forEach { tree.insert(it) }
    tree.checkRedBlack()
    tree.checkBlackHeight()
    tree.checkRoot()

    then:
    notThrown IllegalStateException
    tree.collect() == input.toSorted()
    tree.height() <= 2 * log2(input.size() + 1)
  }

  def "delete 9 elements from the tree"() {
    expect:
    tree.checkRedBlack()
    tree.checkBlackHeight()
    tree.checkRoot()
    deleteFromInputAndTree(a) == input.toSorted()

    where:
    a | _
    1 | _
    2 | _
    3 | _
    4 | _
    5 | _
    6 | _
    7 | _
    8 | _
    9 | _
  }

  def "delete another 9 elements from the tree"() {
    expect:
    tree.checkRedBlack()
    tree.checkBlackHeight()
    tree.checkRoot()
    deleteFromInputAndTree(a) == input.toSorted()

    where:
    a | _
    1 | _
    2 | _
    3 | _
    4 | _
    5 | _
    6 | _
    7 | _
    8 | _
    9 | _
  }

  def "delete the last 9 elements from the tree"() {
    expect:
    tree.checkRedBlack()
    tree.checkBlackHeight()
    tree.checkRoot()
    deleteFromInputAndTree(a) == input.toSorted()
    input.empty ? tree.height() == 0 : true

    where:
    a | _
    1 | _
    2 | _
    3 | _
    4 | _
    5 | _
    6 | _
    7 | _
    8 | _
    9 | _
  }

  private List<Integer> deleteFromInputAndTree(int round) {
    int toDelete = input.remove(0)
    tree.delete(toDelete)
    tree.collect()
  }

  double log2(double x) {
    Math.log(x) / Math.log(2)
  }
}
