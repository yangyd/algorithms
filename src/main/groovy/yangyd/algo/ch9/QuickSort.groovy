package yangyd.algo.ch9

class QuickSort {

  static void quicksort(List<Integer> list) {
    _quickSort(list, 0, list.size() - 1)
  }

  static void _quickSort(List<Integer> list, int left, int right) {
    println(list.subList(left, right + 1))
    if (left < right) {
      int pivot = partition(list, left, right)
      // pivot could be == right, when pivot is the biggest element (which is at [right]), means no move is made
      // in this case recursive will never make progress if we include pivot in left hand
      _quickSort(list, left, pivot - 1)

      // but if pivot == left, it means
      //   1) left == right, will not incur further recursion
      //   2) pivot happens to be the smallest element, it used to be at [right], but is now moved to [left]. next round things will change.
      // so its safe to include pivot in the right side.

      // but best is that we don't include pivot in either side.
      _quickSort(list, pivot + 1, right)
    }
  }

  static int partition(List<Integer> list, int left, int right) {
    int pivot = list.get(right)
    int i = left
    int j = left

    while (j < right) {
      if (list.get(j) < pivot) {
        swap(list, i, j)
        i += 1
      }
      j += 1
    }
    swap(list, i, right)
    return i
  }

  private static void swap(List<Integer> list, int a, int b) {
    if (a != b) {
      int tmp = list.get(a)
      list.set(a, list.get(b))
      list.set(b, tmp)
    }
  }

  /**
   * find out the minimum and maximum of the input
   * @param input
   * @return a tuple of (min, max)
   */
  static int[] minmax(List<Integer> input) {
    final len = input.size()
    if (len < 2) {
      throw new IllegalArgumentException("input should have at lease 2 elements")
    }
    def min, max, p, tmp
    if (len % 2 == 0) {
      min = input.head()
      tmp = input.get(1)
      if (min > tmp) {
        max = min
        min = tmp
      } else {
        max = tmp
      }
      p = 2
    } else {
      min = input.head()
      max = input.head()
      p = 1
    }

    while (p < len) {
      def a = input.get(p)
      def b = input.get(p + 1)

      if (a > b) {
        if (max < a) {
          max = a
        }
        if (min > b) {
          min = b
        }
      } else {
        if (max < b) {
          max = b
        }
        if (min > a) {
          min = a
        }
      }
      p += 2
    }

    [min, max]
  }

}
