package yangyd.algo.ch9

import spock.lang.Specification
import yangyd.algo.fixture.Trees

import java.util.stream.Collectors

class Ch9_Spec extends Specification {

  def "insertSort should work"() {
    when:
    List<Double> sample = Trees.randomList(10)
    List<Double> compare = new ArrayList<>(sample)
    Collections.sort(compare)
    QuickSort.insertSort(sample)

    then:
    sample == compare
  }

  def "randomSelect returns the n-th smallest element in the list"() {
    when:
    List<Double> sample = Trees.randomList(30)
    List<Double> compare = new ArrayList<>(sample)
    Collections.sort(compare)
    double select = QuickSort.randomSelect(14, sample)

    then:
    select == compare.get(14 - 1)
  }

  def "quicksort() sort a list in-place"() {
    when:
    List<Double> sample = Trees.randomList(30)
    List<Double> compare = new ArrayList<>(sample)
    QuickSort.quicksort(sample)
    Collections.sort(compare)

    then:
    sample == compare
  }

  def "minmax() finds out min and max"() {
    expect:
    mm == QuickSort.minmax(Trees.randomList(input))

    where:
    mm      | input
    [1, 23] | 23
    [1, 30] | 30
  }

  def "minmax() works for list with length of at least 2"() {
    when:
    def mm = QuickSort.minmax([6, 3])

    then:
    mm == [3, 6]
  }


}
