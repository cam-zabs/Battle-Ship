package cs3500.pa04.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa04.model.ShipType;
import java.util.Map;

/**
 * A JSON representation of the setup information,
 * containing the height, width, and fleet specifications.
 */
public record SetupJson(
    @JsonProperty("height") int height,
    @JsonProperty("width") int width,
    @JsonProperty("fleet-spec") Map<ShipType, Integer> fleetSpec) {
}
