package cs3500.pa04.model;

import java.util.List;

/**
 * The AutomatedBoard class represents the game board for the automated player.
 * It provides methods to create and update the board, as well as check if the game is over.
 */
public class AutomatedBoard implements Board {
  private int[][] board;

  /**
   * Creates a new game board with the specified dimensions, initialized with zeroes.
   *
   * @param height the height of the board
   * @param width  the width of the board
   */
  public void makeBoard(int height, int width) {
    board = new int[height][width];
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        board[i][j] = 0;
      }
    }
  }

  /**
   * Places the given list of ships on the board based on their positions and orientations.
   * Each ship is assigned a unique positive identifier.
   *
   * @param ships the list of ships to be placed on the board
   */
  public void placeShips(List<Ship> ships, int boardHeight, int boardWidth) {
    int currentShipId = 1;

    for (Ship ship : ships) {
      Coord startCoord = ship.getCoord();
      int shipWidth = ship.getShipType().getShipSize();
      int shipHeight = 1;

      if (ship.getDirection() == Orientation.VERTICAL) {
        shipHeight = shipWidth;
        shipWidth = 1;
      }

      int startX = startCoord.getX();
      int startY = startCoord.getCoordY();
      int endX = startX + shipWidth - 1;
      int endY = startY + shipHeight - 1;

      for (int i = startX; i <= endX && i < boardHeight; i++) {
        for (int j = startY; j <= endY && j < boardWidth; j++) {
          board[i][j] = currentShipId;
        }
      }
      currentShipId++;
    }
  }


  /**
   * Updates the game board based on the list of hit coordinates.
   * Marks the corresponding cells as negative values to indicate hits.
   *
   * @param hits the list of coordinates representing hits
   */
  public void updateBoard(List<Coord> hits) {
    for (Coord hit : hits) {
      int x = hit.getX();
      int y = hit.getCoordY();

      if (x >= 0 && x < board.length && y >= 0 && y < board[x].length && board[x][y] > 0) {
        board[x][y] = board[x][y] * -1;
      }
    }
  }

  /**
   * Checks if the game is over by examining the game board.
   * Returns true if all ships have been sunk, false otherwise.
   *
   * @return true if the game is over, false otherwise
   */
  public boolean isGameOver() {
    for (int[] row : board) {
      for (int value : row) {
        if (value > 0) {
          return false;
        }
      }
    }
    return true;
  }

  @Override
  public int[][] getBoard() {
    return board;
  }

  public void setBoard(int[][] board) {
    this.board = board;
  }


  /**
   * Gets the height of the game board.
   *
   * @return the height of the game board
   */
  public int getHeight() {
    return board.length;
  }

  /**
   * Gets the width of the game board.
   *
   * @return the width of the game board
   */
  public int getWidth() {
    return (board.length > 0) ? board[0].length : 0;
  }

}
