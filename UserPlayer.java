package cs3500.pa04.model;

import cs3500.pa04.view.UserAttacks;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

/**
 * Represents a player that sends inputs
 */
public class UserPlayer implements Player {
  private static final Random random = new Random();
  private List<Coord> shots;
  private int[][] board;

  public UserPlayer(PlayerBoard board) {
    this.board = board.getBoard();
  }

  /**
   * Get the player's name.
   *
   * @return the player's name
   */
  @Override
  public String name() {
    return "cam-zabs";
  }


  /**
   * Given the specifications for a BattleSalvo board, return a list of ships with their locations
   * on the board.
   *
   * @param height         the height of the board, range: [6, 15] inclusive
   * @param width          the width of the board, range: [6, 15] inclusive
   * @param specifications a map of ship type to the number of occurrences each ship should
   *                       appear on the board
   * @return the placements of each ship on the board
   */
  @Override
  public List<Ship> setup(int height, int width, Map<ShipType, Integer> specifications) {
    List<Ship> shipLocation = new ArrayList<>();
    Random random = new Random();

    for (Map.Entry<ShipType, Integer> entry : specifications.entrySet()) {
      ShipType shipType = entry.getKey();
      int numOccurrences = entry.getValue();

      for (int i = 0; i < numOccurrences; i++) {
        boolean isValidPlacement = false;
        Ship ship = null;

        while (!isValidPlacement) {
          int shipWidth = shipType.getShipSize();
          int shipHeight = 1;

          if (random.nextBoolean()) {
            int startX = random.nextInt(width - shipWidth + 1);
            int startY = random.nextInt(height);
            Coord startCoord = new Coord(startX, startY);
            ship = new Ship(startCoord, shipWidth, Orientation.HORIZONTAL);
          } else {
            int startX = random.nextInt(width);
            int startY = random.nextInt(height - shipHeight + 1);
            Coord startCoord = new Coord(startX, startY);
            ship = new Ship(startCoord, shipWidth, Orientation.VERTICAL);
          }

          if (isShipPlacementValid(ship, width, height, shipLocation)) {
            isValidPlacement = true;
          }
        }

        shipLocation.add(ship);
      }
    }
    return shipLocation;
  }

  private boolean isShipPlacementValid(Ship ship, int height, int width, List<Ship> shipPlacement) {
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

    if (startX < 0 || endX >= width || startY < 0 || endY >= height) {
      return false;
    }
    for (Ship placedShip : shipPlacement) {
      Coord placedStartCoord = placedShip.getCoord();
      int placedShipWidth = placedShip.getShipType().getShipSize();
      int placedShipHeight = 1;

      if (placedShip.getDirection() == Orientation.VERTICAL) {
        placedShipHeight = placedShipWidth;
        placedShipWidth = 1;
      }

      int placedStartX = placedStartCoord.getX();
      int placedStartY = placedStartCoord.getCoordY();
      int placedEndX = placedStartX + placedShipWidth - 1;
      int placedEndY = placedStartY + placedShipHeight - 1;

      boolean isOverlap = !(endX < placedStartX || startX > placedEndX || endY < placedStartY
          || startY > placedEndY);

      if (isOverlap) {
        return false;
      }
    }
    return true;
  }

  /**
   * Returns this player's shots on the opponent's board. The number of shots returned should
   * equal the number of ships on this player's board that have not sunk.
   *
   * @return the locations of shots on the opponent's board
   */
  @Override
  public List<Coord> takeShots() {
    UserAttacks userAttacks = new UserAttacks(new Scanner(System.in));
    shots = userAttacks.listOfShots(board);
    return shots;
  }

  /**
   * Given the list of shots the opponent has fired on this player's board, report which
   * shots hit a ship on this player's board.
   *
   * @param opponentShotsOnBoard the opponent's shots on this player's board
   * @return a filtered list of the given shots that hit a ship on this board
   */
  @Override
  public List<Coord> reportDamage(List<Coord> opponentShotsOnBoard) {
    List<Coord> damage;

    List<Coord> listOfShipCoords = new ArrayList<>();
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        if (board[i][j] > 0) {
          Coord c = new Coord(i, j);
          listOfShipCoords.add(c);
        }
      }
    }
    damage = findCommonCoords(opponentShotsOnBoard, listOfShipCoords);
    return damage;
  }

  /**
   * Returns common coords between the two given lists
   *
   * @param list1 first list of coords
   * @param list2 second list of coords
   * @return list of coords that are the same in each given list
   */
  private static List<Coord> findCommonCoords(List<Coord> list1, List<Coord> list2) {
    List<Coord> commonCoords = new ArrayList<>();

    for (Coord coord1 : list1) {
      for (Coord coord2 : list2) {
        if (coord1.getX() == coord2.getX() && coord1.getCoordY() == coord2.getCoordY()) {
          commonCoords.add(coord1);
          break;
        }
      }
    }

    return commonCoords;
  }

  /**
   * Reports to this player what shots in their previous volley returned from takeShots()
   * successfully hit an opponent's ship.
   *
   * @param shotsThatHitOpponentShips the list of shots that successfully hit the opponent's ships
   */
  @Override
  public void successfulHits(List<Coord> shotsThatHitOpponentShips) {

  }

  /**
   * Notifies the player that the game is over.
   * Win, lose, and draw should all be supported
   *
   * @param result if the player has won, lost, or forced a draw
   * @param reason the reason for the game ending
   */
  @Override
  public void endGame(GameResult result, String reason) {

  }
}
