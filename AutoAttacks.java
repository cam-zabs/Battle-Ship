package cs3500.pa04.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The AutoAttacks class provides methods for generating and validating automated attacks
 */
public class AutoAttacks {
  public static int numOfShips;
  private List<Coord> shotTracker = new ArrayList<>();
  private List<Coord> successfulShots = new ArrayList<>();

  public static int totalShots;
  int tilesAttacked = 0;

  /**
   * Sets successful shots to the given list of shots
   *
   * @param shots list of shots to set
   */
  public void setSuccessfulShots(List<Coord> shots) {
    this.successfulShots = shots;
  }

  /**
   * Returns the number of unique ships present on the board.
   *
   * @param board the game board represented as a 2D integer array
   * @return the number of unique ships
   */
  public static int getNumOfShips(int[][] board) {
    totalShots = 0;
    Set<Integer> uniqueIntegers = new HashSet<>();
    int rowCounter = 0;
    int colCounter = 0;
    for (int[] row : board) {
      rowCounter = board.length;
      for (int num : row) {
        colCounter = row.length;
        if (num > 0) {
          uniqueIntegers.add(num);
        }
      }
    }
    totalShots = rowCounter * colCounter;
    numOfShips = uniqueIntegers.size();
    return numOfShips;
  }

  /**
   * Generates a list of coordinates for automated attacks.
   * The number of generated coordinates is equal to the number of ships on the board.
   *
   * @param board the game board represented as a 2D integer array
   * @return a list of coordinates representing automated attacks
   */
  public List<Coord> listOfShots(int[][] board, Randomable randomable) {
    ArrayList<Coord> attacks = new ArrayList<>();
    int randomShots;
    int shots;
    int shotsLeft = totalShots - tilesAttacked;
    int shipsLeft = numOfShips;
    if (shotsLeft < shipsLeft) {
      shots = shotsLeft;
    } else {
      shots = shipsLeft;
    }
    if (successfulShots == null) {
      randomShots = shots;
    } else {
      randomShots = shots - successfulShots.size();
      // another loop for successful shots
      for (Coord coord : successfulShots) {
        boolean validShot = false;
        while (!validShot) {
          if (hasValidAdjacent(board, coord)) {
            Coord adjacentCoord = generateAdjacentShot(board, coord);
            if (contains(adjacentCoord)) {
              Coord randomCoord = generateRandomShot(board, randomable);
              if (contains(randomCoord)) {
                // do nothing
              } else {
                attacks.add(randomCoord);
                shotTracker.add(randomCoord);
                validShot = true;
              }
            } else {
              attacks.add(adjacentCoord);
              shotTracker.add(adjacentCoord);
              validShot = true;
            }
          } else {
            Coord randomCoord = generateRandomShot(board, randomable);
            if (contains(randomCoord)) {
              // do nothing
            } else {
              attacks.add(randomCoord);
              shotTracker.add(randomCoord);
              validShot = true;
            }
          }
        }
      }
    }
    for (int i = 0; i < randomShots; i++) {
      boolean validShot = false;
      while (!validShot) {
        Coord randomCoord = generateRandomShot(board, randomable);
        if (contains(randomCoord)) {
          // do nothing
        } else {
          attacks.add(randomCoord);
          shotTracker.add(randomCoord);
          validShot = true;
        }
      }
    }
    for (Coord attack : attacks) {
      this.shotTracker.add(attack);
    }
    tilesAttacked = tilesAttacked + attacks.size();
    return attacks;
  }

  /**
   * Generates a coordinate based on the given coord
   * Only runs after checking if coord has valid adjacent
   *
   * @param board the game board represented as a 2D integer array
   * @param coord the given coord from a successful shot
   * @return an adjacent coord to the given coord
   */
  public Coord generateAdjacentShot(int[][] board, Coord coord) {
    Coord adjacentCoord = null;
    boolean validShot = false;
    Coord rightCoord;
    Coord bottomCoord;
    Coord leftCoord;
    Coord topCoord;
    while (!validShot) {
      if (coord.getX() != 0) {
        leftCoord = new Coord(coord.getX() - 1, coord.getCoordY());
        if (!contains(leftCoord)) {
          adjacentCoord = leftCoord;
          validShot = true;
        }
      }
      if (coord.getX() != board.length - 1) {
        rightCoord = new Coord(coord.getX() + 1, coord.getCoordY());
        if (!contains(rightCoord)) {
          adjacentCoord = rightCoord;
          validShot = true;
        }
      }
      if (coord.getCoordY() != 0) {
        topCoord = new Coord(coord.getX(), coord.getCoordY() - 1);
        if (!contains(topCoord)) {
          adjacentCoord = topCoord;
          validShot = true;
        }
      }
      if (coord.getCoordY() != board.length - 1) {
        bottomCoord = new Coord(coord.getX(), coord.getCoordY() + 1);
        if (!contains(bottomCoord)) {
          adjacentCoord = bottomCoord;
          validShot = true;
        }
      }
    }
    return adjacentCoord;
  }

  /**
   * Generates a random coordinate on the board given a board
   *
   * @param board the game board represented as a 2D integer array
   * @return returns a random coordinate on the board
   */
  public Coord generateRandomShot(int[][] board, Randomable randomable) {
    Coord coord = null;
    for (int row = 0; row < board.length; row++) {
      for (int col = 0; col < board[row].length; col++) {
        int randX = randomable.nextInt(board[row].length);
        int randY = randomable.nextInt(board.length);
        coord = new Coord(randX, randY);
      }
    }
    return coord;
  }

  /**
   * Determines whether the given coord is contained within the list of coords in shotTracker
   *
   * @param coord the given coord to check against the list of coords in shotTracker
   * @return returns a boolean
   */
  public boolean contains(Coord coord) {
    boolean containsCoord = false;
    boolean checkedAll = false;
    while (!checkedAll) {
      for (Coord c : this.shotTracker) {
        if (coord.getX() == c.getX() && coord.getCoordY() == c.getCoordY()) {
          containsCoord = true;
        }
      }
      checkedAll = true;
    }
    return containsCoord;
  }

  public void setShotTracker(List<Coord> shots) {
    this.shotTracker = shots;
  }

  /**
   * Determines whether the given coord has an adjacent coord that has not already been shot
   *
   * @param board the game board represented as a 2D integer array
   * @param coord a successful shot
   * @return returns a boolean depending on if the given coord has an adjacent
   */
  public boolean hasValidAdjacent(int[][] board, Coord coord) {
    Coord rightCoord;
    Coord bottomCoord;
    Coord leftCoord;
    Coord topCoord;
    Coord invalidCoord = new Coord(20, 20);
    boolean validAdjacent;
    if (coord.getX() != 0) {
      leftCoord = new Coord(coord.getX() - 1, coord.getCoordY());
    } else {
      leftCoord = invalidCoord;
    }
    if (coord.getX() != board.length - 1) {
      rightCoord = new Coord(coord.getX() + 1, coord.getCoordY());
    } else {
      rightCoord = invalidCoord;
    }
    if (coord.getCoordY() != 0) {
      topCoord = new Coord(coord.getX(), coord.getCoordY() - 1);
    } else {
      topCoord = invalidCoord;
    }
    if (coord.getCoordY() != board.length - 1) {
      bottomCoord = new Coord(coord.getX(), coord.getCoordY() + 1);
    } else {
      bottomCoord = invalidCoord;
    }
    if ((contains(leftCoord) || leftCoord.equals(invalidCoord))
        && (contains(rightCoord) || rightCoord.equals(invalidCoord))
        && (contains(topCoord) || topCoord.equals(invalidCoord))
        && (contains(bottomCoord) || bottomCoord.equals(invalidCoord))) {
      validAdjacent = false;
    } else {
      validAdjacent = true;
    }
    return validAdjacent;
  }
}
