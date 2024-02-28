package cs3500.pa04.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa04.model.Coord;
import cs3500.pa04.model.Orientation;
import cs3500.pa04.model.Ship;
import java.util.List;

/**
 * A JSON representation of a fleet of ships, containing a list of ships.
 */
public record FleetJson(
    @JsonProperty("fleet") List<ShipJson> ships) {
}
