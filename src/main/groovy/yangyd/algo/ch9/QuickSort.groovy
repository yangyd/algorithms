package yangyd.algo.ch9

class QuickSort {

  static void insertSort(List<Double> list) {
    int sortedTo = 0
    int length = list.size()

    while (true) {
      int next = sortedTo + 1
      double nextVal = list.get(next)

      int i = 0
      while (true) {
        if (list.get(i) < nextVal) {
          i += 1
          if (i > sortedTo) { // nextVal already in correct place
            break
          }
        } else {
          moveForward(list, i, next)
          break
        }
      }

      sortedTo += 1
      if (sortedTo == length - 1) {
        return
      }
    }
  }

  private static void moveForward(List<Double> list, int to, int from) {
    if (to >= from) {
      return
    }
    double value = list.get(from)
    int i = from
    while (i > to) {
      list.set(i, list.get(i - 1))
      i -= 1
    }
    list.set(to, value)
  }

  /**
   * Return the n-th smallest element in the list. The list will be rearranged during the course
   * @param order
   * @param list
   * @return
   */
  static int randomSelect(int order, List<Double> list) {
    return rselect(order, list, 0, list.size() - 1)
  }

  private static int rselect(int order, List<Double> list, int left, int right) {
    if (left > right) {
      throw new RuntimeException("you screwed up")
    } else if (left == right) {
      return list.get(left)
    }
    int pivot = partition(list, left, right)

    int leftSize = pivot - left
    int desiredLeftSize = order - 1

    if (leftSize > desiredLeftSize) {
      // search left
      return rselect(order, list, left, pivot - 1)

    } else if (leftSize < desiredLeftSize) {
      // search right
      int newOrder = order - leftSize - 1
      return rselect(newOrder, list, pivot + 1, right)

    } else {
      return list.get(pivot)
    }
  }


  static void quicksort(List<Double> list) {
    qsort(list, 0, list.size() - 1)
  }

  private static void qsort(List<Double> list, int left, int right) {
//    println(list.subList(left, right + 1))
    if (left < right) {
      int pivot = partition(list, left, right)
      // pivot could be == right, when pivot is the biggest element (which is at [right]), means no move is made
      // in this case recursive will never make progress if we include pivot in left hand
      qsort(list, left, pivot - 1)

      // but if pivot == left, it means
      //   1) left == right, will not incur further recursion
      //   2) pivot happens to be the smallest element, it used to be at [right], but is now moved to [left]. next round things will change.
      // so its safe to include pivot in the right side.

      // but best is that we don't include pivot in either side.
      qsort(list, pivot + 1, right)
    }
  }

  static int partition(List<Double> list, int left, int right) {
    double pivot = list.get(right)
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

  private static void swap(List<Double> list, int a, int b) {
    if (a != b) {
      double tmp = list.get(a)
      list.set(a, list.get(b))
      list.set(b, tmp)
    }
  }

  /**
   * find out the minimum and maximum of the input
   * @param input
   * @return a tuple of (min, max)
   */
  static int[] minmax(List<Double> input) {
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
