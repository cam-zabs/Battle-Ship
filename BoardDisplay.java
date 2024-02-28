package cs3500.pa04.view;

import cs3500.pa04.model.Coord;
import java.util.List;

/**
 * The BoardDisplay class provides methods for displaying and changing game boards.
 */
public class BoardDisplay {

  /**
   * Prints the provided boards side by side.
   *
   * @param board  The first board to print.
   * @param board2 The second board to print.
   */
  public void printBoards(String[][] board, String[][] board2) {
    for (int i = 0; i < board2.length; i++) {
      for (int j = 0; j < board2[i].length; j++) {
        System.out.print((board2[i][j]) + " ");
      }
      System.out.println();
    }
    System.out.println("- ".repeat(board.length));
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        System.out.print(board[i][j] + " ");
      }
      System.out.println();
    }
  }

  /**
   * Converts a 2D integer board to a 2D string board.
   *
   * @param board The 2D integer board to convert.
   * @return The converted 2D string board.
   */
  public String[][] boardDisplay(int[][] board) {
    int rows = board.length;
    int cols = board[0].length;

    String[][] display = new String[rows][cols];

    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        display[i][j] = String.valueOf(board[i][j]);
      }
    }

    return display;
  }

  /**
   * Updates the provided board with the given attacks and returns the updated board.
   *
   * @param board   The original board.
   * @param attacks The list of attacks to apply.
   * @return The updated board.
   */
  public String[][] updateBoardDisplay(String[][] board, List<Coord> attacks) {
    int rows = board.length;
    int cols = board[0].length;

    String[][] updatedBoard = new String[rows][cols];

    for (int i = 0; i < rows; i++) {
      System.arraycopy(board[i], 0, updatedBoard[i], 0, cols);
    }

    for (Coord attack : attacks) {
      int row = attack.getCoordY();
      int col = attack.getX();

      if (row >= 0 && row < rows && col >= 0 && col < cols) {
        String value = board[row][col];

        if (!value.equals("M") && !value.equals("H")) {
          try {
            int numericValue = Integer.parseInt(value);
            if (numericValue == 0) {
              updatedBoard[row][col] = "M";
            } else if (numericValue > 0) {
              updatedBoard[row][col] = "H";
            }
          } catch (NumberFormatException e) {
            // do nothing
          }
        } else {
          updatedBoard[row][col] = value;
        }
      }
    }

    return updatedBoard;
  }


  /**
   * Changes all non "M" and non "H" values in the provided array to "0".
   *
   * @param array The array to modify.
   */
  public void changeValuesToZero(String[][] array) {
    for (int i = 0; i < array.length; i++) {
      for (int j = 0; j < array[i].length; j++) {
        if (!array[i][j].equals("M") && !array[i][j].equals("H") && !array[i][j].contains("-")) {
          array[i][j] = "0";
        }
      }
    }
  }

  /**
   * Changes all negative values represented as strings in the provided array to "H".
   *
   * @param array The array to modify.
   */
  public void changeNegativeValuesToString(String[][] array) {
    for (int i = 0; i < array.length; i++) {
      for (int j = 0; j < array[i].length; j++) {
        String value = array[i][j];
        try {
          int numericValue = Integer.parseInt(value);
          if (numericValue < 0) {
            array[i][j] = "H";
          }
        } catch (NumberFormatException e) {
          // do nothing
        }
      }
    }
  }

}
