package cs3500.pa04.json;

import cs3500.pa04.model.Ship;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to convert a list of Ship to a list of ShipJson
 */
public class ShipAdapter {

  /**
   * converts a list of Ship to a list of ShipJson
   *
   * @param ships given list of ships
   * @return list of ShipJson
   */
  public List<ShipJson> adapt(List<Ship> ships) {
    List<ShipJson> shipJsons = new ArrayList<>();
    for (Ship ship : ships) {
      ShipJson shipJson = ship.shipToJson();
      shipJsons.add(shipJson);
    }
    return shipJsons;
  }
}
