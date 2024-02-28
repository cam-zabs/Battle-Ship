package cs3500.pa04;

import cs3500.pa04.controller.BattleSalvo;
import cs3500.pa04.controller.ProxyController;
import cs3500.pa04.model.AutomatedBoard;
import cs3500.pa04.model.AutomatedPlayer;
import cs3500.pa04.model.Board;
import cs3500.pa04.model.Player;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * This is the main driver of this project.
 */
public class Driver {
  /**
   * Project entry point
   *
   * @param args - no command line args required
   */
  public static void main(String[] args) {
    if (args.length == 0) {
      BattleSalvo battleSalvo = new BattleSalvo(new InputStreamReader(System.in));
      battleSalvo.run();
    } else if (args.length >= 2) {
      ProxyController controller;
      try {
        Socket server = new Socket(args[0], Integer.parseInt(args[1]));
        AutomatedBoard board = new AutomatedBoard();
        Player player = new AutomatedPlayer(board);
        ProxyController proxyController = new ProxyController(server, player);
        proxyController.run();
      } catch (IOException e) {
        System.err.println("Host not found");
      }
    }
  }
}