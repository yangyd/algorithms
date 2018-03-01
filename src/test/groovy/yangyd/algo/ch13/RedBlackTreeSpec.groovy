package yangyd.algo.ch13

import spock.lang.Specification

class RedBlackTreeSpec extends Specification {

  def "build an red-black tree"() {
    when:
    def tree = new RedBlackTree()
    def input = [1,6,8,11,13,15,17,22,25,27]
    input.forEach({tree.insert(it)})
    then:
    tree.collect() == input
    tree.height() == 5
  }

  def "build an red-black tree in reversed order"() {
    when:
    def tree = new RedBlackTree()
    def input = [1,6,8,11,13,15,17,22,25,27]
    input.reverse().forEach({tree.insert(it)})
    tree.print()
    then:
    tree.collect() == input
    tree.height() == 5
  }

}
