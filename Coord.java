package cs3500.pa04.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The Coord class represents a coordinate on a game board.
 * It holds the x and y values of the coordinate.
 */
public class Coord {
  int coordX;
  int coordY;

  /**
   * Constructs a Coord object with the specified x and y values.
   *
   * @param x the x coordinate value
   * @param y the y coordinate value
   */
  public Coord(@JsonProperty("x") int x,
               @JsonProperty("y") int y) {
    this.coordX = x;
    this.coordY = y;
  }

  /**
   * Returns the x coordinate value.
   *
   * @return the x coordinate value
   */
  public int getX() {
    return coordX;
  }

  /**
   * Returns the y coordinate value.
   *
   * @return the y coordinate value
   */
  public int getCoordY() {
    return coordY;
  }

  public boolean equals(Coord coord) {
    return this.getX() == coord.getX() && this.getCoordY() == coord.getCoordY();
  }

}
