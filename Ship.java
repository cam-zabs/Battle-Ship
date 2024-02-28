package cs3500.pa04.model;

import cs3500.pa04.json.ShipJson;

/**
 * The Ship class represents a ship in the game.
 */
public class Ship {
  private Coord coord;
  private int length;

  private Orientation direction;

  /**
   * Constructs a ship with the specified ship type, start coordinate, and orientation.
   *
   * @param coord     the starting coordinate of the ship
   * @param length    the length of the ship
   * @param direction the orientation of the ship (vertical or horizontal)
   */
  public Ship(Coord coord, int length, Orientation direction) {
    this.coord = coord;
    this.length = length;
    this.direction = direction;
  }

  /**
   * Returns the length of the ship
   *
   * @return length of the ship
   */
  public int getLength() {
    return this.length;
  }

  /**
   * Returns the ship type.
   *
   * @return the ship type
   */
  public ShipType getShipType() {
    return intToShipType(this.getLength());
  }

  /**
   * Returns the starting coordinate of the ship.
   *
   * @return the starting coordinate of the ship
   */
  public Coord getCoord() {
    return coord;
  }

  /**
   * Returns the orientation of the ship.
   *
   * @return the orientation of the ship
   */
  public Orientation getDirection() {
    return direction;
  }

  /**
   * Returns the ShipType given the length
   *
   * @param length length of the ship
   * @return ShipType enumeration
   */
  public ShipType intToShipType(int length) {
    ShipType shipType;
    if (length == 3) {
      shipType = ShipType.SUBMARINE;
    } else if (length == 4) {
      shipType = ShipType.DESTROYER;
    } else if (length == 5) {
      shipType = ShipType.BATTLESHIP;
    } else {
      shipType = ShipType.CARRIER;
    }
    return shipType;
  }

  public ShipJson shipToJson() {
    return new ShipJson(this.coord, this.length, this.direction);
  }

}
