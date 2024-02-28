package cs3500.pa04.controller;

import cs3500.pa04.model.AutomatedBoard;
import cs3500.pa04.model.AutomatedPlayer;
import cs3500.pa04.model.Coord;
import cs3500.pa04.model.PlayerBoard;
import cs3500.pa04.model.Ship;
import cs3500.pa04.model.ShipType;
import cs3500.pa04.model.UserPlayer;
import cs3500.pa04.view.BoardDisplay;
import cs3500.pa04.view.BoardSpecs;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * The BattleSalvo class represents the main class for the Battle Salvo game.
 * It is responsible for initializing the boards, executing player turns,
 * and determining the game's outcome.
 */
public class BattleSalvo {

  int[][] userBoard;
  int[][] autoBoard;
  private String[][] userBoardView;
  private String[][] autoBoardView;
  PlayerBoard playerBoard;
  UserPlayer user;
  AutomatedBoard automatedBoard;
  AutomatedPlayer autoPlayer;
  Readable readable;
  BoardDisplay display;
  List<Coord> userShots;
  List<Coord> autoShots;

  public BattleSalvo(Readable readable) {
    this.readable = readable;
  }

  /**
   * Creates the game boards and sets up the initial configuration of ships.
   */
  public void makeBoards() {
    System.out.println("Welcome to OOD Battle Salvo!");
    // gets dimensions and ships
    BoardSpecs specs = new BoardSpecs();
    Scanner scanner = new Scanner(readable);
    int[] handw = specs.getHeightAndWidth(scanner);
    int maxSize = specs.maxFleetSize(handw);

    //Player1
    playerBoard = new PlayerBoard();
    playerBoard.makeBoard(handw[0], handw[1]);
    Map<ShipType, Integer> ships =  specs.getShipSpecifications(maxSize);
    user = new UserPlayer(playerBoard);
    List<Ship> shipPlaces = user.setup(handw[0], handw[1], ships);
    playerBoard.placeShips(shipPlaces, handw[0], handw[1]);
    userBoard = playerBoard.getBoard();


    //Player2
    automatedBoard = new AutomatedBoard();
    automatedBoard.makeBoard(handw[0], handw[1]);
    autoPlayer = new AutomatedPlayer(automatedBoard);
    List<Ship> shipPlaces2 = autoPlayer.setup(handw[0], handw[1], ships);
    automatedBoard.placeShips(shipPlaces2, handw[0], handw[1]);
    autoBoard = automatedBoard.getBoard();

    display = new BoardDisplay();
    userBoardView = display.boardDisplay(userBoard);
    autoBoardView = display.boardDisplay(autoBoard);
    display.changeNegativeValuesToString(autoBoardView);
    display.changeValuesToZero(autoBoardView);
    display.printBoards(userBoardView, autoBoardView);

  }

  /**
   * Executes the game turns until the game ends.
   */
  public void getAttacks() {
    while (!automatedBoard.isGameOver() && !playerBoard.isGameOver()) {
      userShots = user.takeShots();
      autoShots = autoPlayer.takeShots();

      List<Coord> damageUser = user.reportDamage(autoShots);
      List<Coord> damageAi = autoPlayer.reportDamage(userShots);
      user.successfulHits(damageAi);
      autoPlayer.successfulHits(damageUser);
      playerBoard.updateBoard(autoShots);
      autoBoardView = display.boardDisplay(autoBoard);
      userBoardView = display.updateBoardDisplay(userBoardView, autoShots);
      autoBoardView = display.updateBoardDisplay(autoBoardView, userShots);
      automatedBoard.updateBoard(userShots);
      userBoardView = display.updateBoardDisplay(userBoardView, autoShots);
      autoBoardView = display.updateBoardDisplay(autoBoardView, userShots);
      display.changeNegativeValuesToString(autoBoardView);
      display.changeValuesToZero(autoBoardView);
      display.printBoards(userBoardView, autoBoardView);
    }
  }

  /**
   * Displays the outcome of the game.
   */
  public void gameOver() {
    if (automatedBoard.isGameOver() == true) {
      System.out.println("You win! ");
    }
    if (playerBoard.isGameOver() == true) {
      System.out.println("Your opponent wins! ");
    }
  }

  /**
   * Runs the Battle Salvo game.
   */
  public void run() {
    makeBoards();
    getAttacks();
    gameOver();
  }
}
