package yangyd.algo.ch13

import spock.lang.Specification
import yangyd.algo.fixture.Trees

class RedBlackTreeSpec extends Specification {

  def "build an red-black tree"() {
    when:
    def tree = new RedBlackTree()
    def input = Trees.randomList(79)
    input.forEach({tree.insert(it)})
    tree.checkRedBlack()
    tree.checkBlackHeight()
    tree.checkRoot()

    then:
    notThrown(IllegalStateException)
    tree.collect() == input.toSorted()
    tree.height() <= 2 * log2(input.size() + 1) // Lemma 13.1: A red-black tree with n internal nodes has height at most 2lg(n+1)
  }

  def "build an red-black tree with reversely ordered input"() {
    when:
    def tree = new RedBlackTree()
    def input = Trees.randomList(83).toSorted()
    input.reverse().forEach({tree.insert(it)})
    tree.checkRedBlack()
    tree.checkBlackHeight()
    tree.checkRoot()

    then:
    notThrown(IllegalStateException)
    tree.collect() == input.toSorted()
    tree.height() <= 2 * log2(input.size() + 1)
  }

  double log2(double x) {
    Math.log(x) / Math.log(2)
  }
}
