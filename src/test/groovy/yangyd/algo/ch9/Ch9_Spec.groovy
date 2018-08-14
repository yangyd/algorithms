package yangyd.algo.ch9

import spock.lang.Specification
import yangyd.algo.fixture.Trees

class Ch9_Spec extends Specification {

  def "quicksort() sort a list in-place"() {
    when:
    List<Integer> sample = Trees.randomList(30)
    List<Integer> compare = new ArrayList<>(sample)
    QuickSort.quicksort(sample)
    Collections.sort(compare)

    then:
    sample == compare
  }

  def "minmax() finds out min and max"() {
    expect:
    mm == QuickSort.minmax(Trees.randomList(input))

    where:
    mm | input
    [1, 23] | 23
    [1, 30] | 30
  }

  def "minmax() works for list with length of at least 2"() {
    when:
    def mm = QuickSort.minmax([6, 3])

    then:
    mm == [3,6]
  }


}
