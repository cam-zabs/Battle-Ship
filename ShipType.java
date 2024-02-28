package cs3500.pa04.model;

/**
 * The ShipType enum represents the types of ships in the game.
 */
public enum ShipType {

  /**
   * The carrier ship type with a size of 6.
   */
  CARRIER(6),

  /**
   * The battleship ship type with a size of 5.
   */
  BATTLESHIP(5),

  /**
   * The destroyer ship type with a size of 4.
   */
  DESTROYER(4),

  /**
   * The submarine ship type with a size of 3.
   */
  SUBMARINE(3);

  private int shipSize;

  /**
   * Constructs a ShipType with the specified ship size.
   *
   * @param shipSize the size of the ship
   */
  ShipType(int shipSize) {
    this.shipSize = shipSize;
  }

  /**
   * Returns the size of the ship.
   *
   * @return the size of the ship
   */
  public int getShipSize() {
    return shipSize;
  }
}
