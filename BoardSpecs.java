package cs3500.pa04.view;

import cs3500.pa04.model.ShipType;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * The BoardSpecs class represents the specifications of a game board for a battleship game.
 * It provides methods to retrieve the height and width of the board from user input,
 * calculate the maximum fleet size based on the board dimensions, and obtain ship specifications
 * from user input.
 */
public class BoardSpecs {
  public static Scanner asker;

  /**
   * Gets the height and width of the board from user input.
   *
   * @param scanner the scanner object to read user input from
   * @return an array containing the height and width of the board
   */
  public int[] getHeightAndWidth(Scanner scanner) {
    this.asker = scanner;
    int[] dimensions = new int[2];
    boolean validInput = false;

    while (!validInput) {
      System.out.print("Enter the height and width of the board ");
      String input = asker.nextLine();

      String[] heightAndWidth = input.split(" ");
      if (heightAndWidth.length != 2) {
        System.out.println("Invalid input. Please enter two integers");
        continue;
      }

      try {
        int height = Integer.parseInt(heightAndWidth[0]);

        int width = Integer.parseInt(heightAndWidth[1]);


        if (height >= 6 && height <= 15 && width >= 6 && width <= 15) {
          dimensions[0] = height;
          dimensions[1] = width;
          validInput = true;
        } else {
          System.out.println("Invalid dimensions. Please enter integers between 6 and 15.");
        }
      } catch (NumberFormatException e) {
        System.out.println("Invalid input. Please enter valid integers.");
      }
    }

    return dimensions;
  }

  /**
   * Calculates the maximum fleet size based on the board dimensions.
   *
   * @param arr an array containing the height and width of the board
   * @return the maximum fleet size
   */
  public int maxFleetSize(int[] arr) {
    int maxFleet = 0;
    int height = arr[0];
    int width = arr[1];
    if (height > width) {
      maxFleet = width;
    }
    if (height < width) {
      maxFleet = height;
    }
    if (height == width) {
      maxFleet = height;
    }
    return maxFleet;
  }

  /**
   * Gets the ship specifications from user input.
   *
   * @param maxFleetSize the maximum fleet size allowed
   * @return a map of ship types and their corresponding counts
   */
  public Map<ShipType, Integer> getShipSpecifications(int maxFleetSize) {
    Map<ShipType, Integer> specifications = new HashMap<>();

    boolean validSpecs = false;

    while (!validSpecs) {
      specifications.clear();

      System.out.print("Enter ship specifications: ");
      String input = asker.nextLine();

      String[] shipCounts = input.split(" ");
      if (shipCounts.length != ShipType.values().length) {
        System.out.println("Invalid input. Please enter ship counts for all ship types.");
        continue;
      }

      boolean validInput = true;
      int totalShipCount = 0;

      for (int i = ShipType.values().length - 1; i >= 0; i--) {
        try {
          int count = Integer.parseInt(shipCounts[i]);
          ShipType shipType = ShipType.values()[i];

          if (count >= 0 && count <= maxFleetSize - totalShipCount) {
            specifications.put(shipType, count);
            totalShipCount += count;
          } else {
            System.out.println("Invalid count for " + shipType + ". "
                +
                "Please enter a valid integer within the available count.");
            validInput = false;
            break;
          }
        } catch (NumberFormatException e) {
          System.out.println("Invalid input for ship count. Please enter valid integers.");
          validInput = false;
          break;
        }
      }

      if (validInput && totalShipCount <= maxFleetSize) {
        validSpecs = true;
      } else {
        System.out.println("Ship count exceeds the max fleet size. "
            +
            "Please adjust ship counts.");
      }
    }

    return specifications;
  }

  public void setScanner(Scanner scanner) {
    this.asker = scanner;
  }
}



