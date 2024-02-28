package cs3500.pa04.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa04.model.Coord;
import java.util.List;

/**
 * A JSON representation of a volley, containing a list of shots.
 */
public record VolleyJson(
    @JsonProperty("coordinates") List<Coord> shots) {
}
