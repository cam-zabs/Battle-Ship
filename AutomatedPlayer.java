package cs3500.pa04.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * The AutomatedPlayer class represents an automated player in the Battle Salvo game.
 * It implements the Player interface and provides methods for setting up ships,
 * taking shots, reporting damage, and handling the game's end.
 */
public class AutomatedPlayer implements Player {
  private AutomatedBoard automatedBoard;
  private int[][] board;
  AutoAttacks autoAttacks = new AutoAttacks();
  AutomatedBoard board1 = new AutomatedBoard();


  /**
   * Constructs an AutomatedPlayer with the specified game board.
   *
   * @param aboard the game board represented as a 2D integer array
   */
  public AutomatedPlayer(AutomatedBoard aboard) {
    this.automatedBoard = aboard;
  }

  /**
   * Retrieves the name of the player.
   *
   * @return the name of the player
   */
  @Override
  public String name() {
    return "cam-zabs";
  }

  /**
   * Sets up the player's ships on the game board based on the given specifications.
   *
   * @param height         the height of the game board
   * @param width          the width of the game board
   * @param specifications a map specifying the ship types and their counts
   * @return a list of Ship objects representing the player's ship placements
   */
  @Override
  public List<Ship> setup(int height, int width, Map<ShipType, Integer> specifications) {
    board1.makeBoard(height, width);
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
    board1.placeShips(shipLocation, height, width);
    board = board1.getBoard();
    return shipLocation;
  }

  /**
   * Checks if the ship can be placed
   *
   * @param height        the height of the game board
   * @param width         the width of the game board
   * @param shipPlacement list of the current ship placements
   * @return true if the ship can be placed there
   */
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
   * Takes shots on the opponent's board.
   *
   * @return a list of Coord objects representing the shots to be taken
   */
  @Override
  public List<Coord> takeShots() {
    autoAttacks.getNumOfShips(board);
    List<Coord> shots = autoAttacks.listOfShots(board, new RandomDecorator());
    return shots;
  }

  /**
   * Given the list of shots the opponent has fired on this player's board, report which
   * shots hit a ship on this player's board.
   *
   * @param opponentShotsOnBoard the opponent's shots on this player's board
   * @return a filtered list of the given shots that contain all locations of shots that hit a
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
    board1.updateBoard(damage);
    return damage;
  }

  /**
   * Finds the common coordinates between two lists of coordinates.
   *
   * @param list1 the first list of coordinates
   * @param list2 the second list of coordinates
   * @return a list of Coord objects representing the common coordinates between the two lists
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
    autoAttacks.setSuccessfulShots(shotsThatHitOpponentShips);
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
    System.out.printf("The game has ended. You " + result + "!");
  }
}
