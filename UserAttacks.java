package cs3500.pa04.view;

import cs3500.pa04.model.Coord;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * The UserAttacks class provides methods for obtaining user attacks and related information.
 */
public class UserAttacks {

  public static int numOfShips;
  static Scanner asker = new Scanner(System.in);

  public UserAttacks(Scanner scanner) {
    this.asker = scanner;
  }

  /**
   * Calculates the number of ships on the board.
   *
   * @param board the game board
   * @return the number of ships on the board
   */
  public static int getNumOfShips(int[][] board) {
    Set<Integer> uniqueIntegers = new HashSet<>();

    for (int[] row : board) {
      for (int num : row) {
        if (num > 0) {
          uniqueIntegers.add(num);
        }
      }
    }
    numOfShips = uniqueIntegers.size();
    return numOfShips;
  }

  /**
   * Prompts the user to enter a list of coordinates for ship attacks.
   *
   * @param board the game board
   * @return a list of coordinates representing the ship attacks
   */
  public static List<Coord> listOfShots(int[][] board) {
    int numOfShips = getNumOfShips(board);
    List<Coord> shipCoords = new ArrayList<>();

    boolean isValidInput = false;
    while (!isValidInput) {
      isValidInput = true;
      System.out.println("Time to Attack! Choose " + numOfShips + " coordinates");
      for (int i = 1; i <= numOfShips; i++) {
        System.out.print("Enter coordinates for Ship " + i + " ");
        String input = asker.nextLine();
        String[] coordinates = input.split(" ");

        if (coordinates.length != 2) {
          System.out.println("Invalid input. Please provide coordinates in the format '0 0'.");
          isValidInput = false;
          break;
        }

        int x;
        int y;
        try {
          x = Integer.parseInt(coordinates[0]);
          y = Integer.parseInt(coordinates[1]);
        } catch (NumberFormatException e) {
          System.out.println("Invalid input. Please provide valid integer coordinates.");
          isValidInput = false;
          break;
        }

        if (!isValidCoordinate(x, y) || x >= board[0].length || y >= board.length) {
          System.out.println("Invalid coordinates. Please try again.");
          isValidInput = false;
          break;
        }

        Coord coord = new Coord(x, y);
        shipCoords.add(coord);
      }

      if (!isValidInput) {
        shipCoords.clear();
      }
    }

    return shipCoords;
  }

  /**
   * Checks if the given coordinates are valid integer values.
   *
   * @param x the x-coordinate
   * @param y the y-coordinate
   * @return true if the coordinates are valid, false otherwise
   */
  static boolean isValidCoordinate(int x, int y) {
    try {
      Integer.parseInt(String.valueOf(x));
      Integer.parseInt(String.valueOf(y));
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  public void setScanner(Scanner scanner) {
    this.asker = scanner;
  }
}




