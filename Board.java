package cs3500.pa04.model;

import java.util.List;

/**
 * The Board interface represents a game board.
 */
public interface Board {
  /**
   * Returns the height of the board.
   *
   * @return the height of the board
   */
  int getHeight();

  /**
   * Returns the width of the board.
   *
   * @return the width of the board
   */
  int getWidth();


  /**
   * Places the given list of ships on the board.
   * Each ship is assigned a unique positive identifier.
   *
   * @param ships the list of ships to be placed on the board
   */
  void placeShips(List<Ship> ships, int boardHeight, int boardWidth);

  /**
   * Updates the board based on the list of hit coordinates.
   * Marks the corresponding cells as negative values to indicate hits.
   *
   * @param hits the list of coordinates representing hits
   */
  void updateBoard(List<Coord> hits);

  /**
   * Checks if the game is over by examining the board.
   * Returns true if all ships have been sunk, false otherwise.
   *
   * @return true if the game is over, false otherwise
   */
  boolean isGameOver();

  int[][] getBoard();

}
