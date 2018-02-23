package yangyd.algo.ch10

import yangyd.algo.datastructure.BinaryTreeNode

class Section10_4 {

  static enum NextMove {
    down_left, down_right, up
  }

  static enum PreviousMove {
    down, up_from_left, up_from_right
  }

  private static NextMove nextMove(PreviousMove previousMove, BinaryTreeNode node) {
    switch (previousMove) {
      case PreviousMove.down:
        if (node.left != null) {
          return NextMove.down_left
        } else if (node.right != null) {
          return NextMove.down_right
        } else {
          return NextMove.up
        }

      case PreviousMove.up_from_left:
        if (node.right != null) {
          return NextMove.down_right
        } else {
          return NextMove.up
        }

      case PreviousMove.up_from_right:
        return NextMove.up
    }
  }

  static List<Integer> exercise10_4_5(BinaryTreeNode<Integer> root) {
    BinaryTreeNode<Integer> node = root
    PreviousMove previousMove = PreviousMove.down
    List<Integer> collected = new LinkedList<>()
    while (true) {
      switch (nextMove(previousMove, node)) {
        case NextMove.down_left:
          node = node.left
          previousMove = PreviousMove.down
          break

        case NextMove.down_right:
          node = node.right
          previousMove = PreviousMove.down
          break

        case NextMove.up:
          // access node before moving up
          // this is effectively post-order tree walk
          collected.add(node.key)

          // end of procedure
          if (node.parent == null) {
            return collected
          }

          if (node.parent.left == node) {
            previousMove = PreviousMove.up_from_left
          } else {
            previousMove = PreviousMove.up_from_right
          }
          node = node.parent
          break

        default:
          throw new AssertionError("impossible")
      }
    } // end while
  }
}
