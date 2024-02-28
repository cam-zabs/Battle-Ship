package cs3500.pa04.controller;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cs3500.pa04.json.EndGameJson;
import cs3500.pa04.json.FleetJson;
import cs3500.pa04.json.JoinJson;
import cs3500.pa04.json.JsonUtils;
import cs3500.pa04.json.MessageJson;
import cs3500.pa04.json.SetupJson;
import cs3500.pa04.json.ShipAdapter;
import cs3500.pa04.json.ShipJson;
import cs3500.pa04.json.VolleyJson;
import cs3500.pa04.model.AutomatedBoard;
import cs3500.pa04.model.Coord;
import cs3500.pa04.model.GameResult;
import cs3500.pa04.model.GameType;
import cs3500.pa04.model.Player;
import cs3500.pa04.model.Ship;
import cs3500.pa04.model.ShipType;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A controller class that acts as a proxy between the client and the server in a game.
 * It receives messages from the server, delegates them to appropriate methods, and sends
 * responses back to the server.
 */
public class ProxyController {
  private final Socket server;
  private final InputStream in;
  private final PrintStream out;
  private final Player player;
  private final ObjectMapper mapper = new ObjectMapper();
  private final AutomatedBoard board;

  private static final JsonNode VOID_RESPONSE =
      new ObjectMapper().getNodeFactory().textNode("void");

  /**
   * Construct an instance of a ProxyPlayer.
   *
   * @param server the socket connection to the server
   * @param player the instance of the player
   * @throws IOException if
   */
  public ProxyController(Socket server, Player player) throws IOException {
    this.server = server;
    this.in = server.getInputStream();
    this.out = new PrintStream(server.getOutputStream());
    this.board = new AutomatedBoard();
    this.player = player;
  }

  /**
   * Runs the proxy controller by continuously receiving messages from the server and delegating
   * them to the appropriate methods.
   */
  public void run() {
    try {
      JsonParser parser = this.mapper.getFactory().createParser(this.in);

      while (!this.server.isClosed()) {
        MessageJson message = parser.readValueAs(MessageJson.class);
        delegateMessage(message);
      }
    } catch (IOException e) {
      try {
        this.server.close();
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
    }
  }

  /**
   * Delegates a received message to the appropriate handling method based on the message name.
   *
   * @param message the message received from the server
   */
  private void delegateMessage(MessageJson message) {
    String name = message.messageName();
    JsonNode arguments = message.arguments();
    if ("join".equals(name)) {
      handleJoin();
    } else if ("setup".equals(name)) {
      handleSetup(arguments);
    } else if ("take-shots".equals(name)) {
      handleTakeShots();
    } else if ("report-damage".equals(name)) {
      handleReportDamage(arguments);
    } else if ("successful-hits".equals(name)) {
      handleSuccessfulHits(arguments);
    } else if ("end-game".equals(name)) {
      handleWin(arguments);
    } else {
      throw new IllegalStateException("Invalid message name");
    }
  }

  /**
   * Handles the "join" message by sending a join request to the server.
   */
  public void handleJoin() {
    String name = player.name();
    GameType gameType = GameType.MULTI;
    JoinJson joinJson = new JoinJson(name, gameType);
    MessageJson msgJson = new MessageJson("join", JsonUtils.serializeRecord(joinJson));
    JsonNode jsonResponse = JsonUtils.serializeRecord(msgJson);
    this.out.println(jsonResponse);
  }


  /**
   * Handles the "take-shots" message by requesting the player to take shots and sending the
   * shots to the server.
   */
  public void handleTakeShots() {
    List<Coord> shots = player.takeShots();
    VolleyJson response = new VolleyJson(shots);
    MessageJson msgJson = new MessageJson("take-shots", JsonUtils.serializeRecord(response));
    JsonNode jsonResponse = JsonUtils.serializeRecord(msgJson);
    this.out.println(jsonResponse);
  }

  /**
   * Handles the "setup" message by retrieving the board setup from the player and sending it
   * to the server.
   *
   * @param fleet the fleet specification received from the server
   */
  public void handleSetup(JsonNode fleet) {
    SetupJson setup = this.mapper.convertValue(fleet, SetupJson.class);
    int height = setup.height();
    int width = setup.width();
    Map<ShipType, Integer> fleetSpec = setup.fleetSpec();
    List<Ship> ships = player.setup(height, width, fleetSpec);
    ShipAdapter adapter = new ShipAdapter();
    List<ShipJson> shipJsons = adapter.adapt(ships);
    FleetJson response = new FleetJson(shipJsons);
    MessageJson msgJson = new MessageJson("setup", JsonUtils.serializeRecord(response));
    JsonNode jsonResponse = JsonUtils.serializeRecord(msgJson);
    this.out.println(jsonResponse);
  }

  /**
   * Handles the "report-damage" message by receiving the opponent's shots, reporting the damage
   * to the player, and sending the successful hits back to the server.
   *
   * @param shots the opponent's shots received from the server
   */
  public void handleReportDamage(JsonNode shots) {
    VolleyJson opponentShots = this.mapper.convertValue(shots, VolleyJson.class);
    List<Coord> allShots = opponentShots.shots();
    List<Coord> hits = player.reportDamage(allShots);
    VolleyJson response = new VolleyJson(hits);
    MessageJson msgJson = new MessageJson("report-damage", JsonUtils.serializeRecord(response));
    JsonNode jsonResponse = JsonUtils.serializeRecord(msgJson);
    this.out.println(jsonResponse);
  }

  /**
   * Handles the "successful-hits" message by receiving the successful hits from the server
   * and notifying the player.
   *
   * @param shots the successful hits received from the server
   */
  public void handleSuccessfulHits(JsonNode shots) {
    VolleyJson successfulHits = this.mapper.convertValue(shots, VolleyJson.class);
    List<Coord> hits = successfulHits.shots();
    player.successfulHits(hits);
    MessageJson msgJson = new MessageJson("successful-hits", VOID_RESPONSE);
    JsonNode jsonResponse = JsonUtils.serializeRecord(msgJson);
    this.out.println(jsonResponse);
  }

  /**
   * Handles the "end-game" message by receiving the game state from the server and notifying
   * the player about the end of the game.
   *
   * @param gameState the game state received from the server
   */
  public void handleWin(JsonNode gameState) {
    EndGameJson endGame = this.mapper.convertValue(gameState, EndGameJson.class);
    GameResult result = endGame.result();
    String reason = endGame.reason();
    player.endGame(result, reason);
    MessageJson msgJson = new MessageJson("end-game", VOID_RESPONSE);
    JsonNode jsonResponse = JsonUtils.serializeRecord(msgJson);
    this.out.println(jsonResponse);
  }
}
