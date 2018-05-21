package yangyd.algo.ch9

class QuickSort {

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
