package yangyd.algo.ch10

import spock.lang.Specification
import yangyd.algo.fixture.Trees

class Section10_4_Spec extends Specification {

  def "HashMap accepts null key"() {
    when:
    def list = Section10_4.exercise10_4_5(Trees.sample_bst)

    then:
    list == [2,4,3,9,13,7,6,17,20,18,15]
  }
}
