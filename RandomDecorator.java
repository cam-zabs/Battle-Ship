package cs3500.pa04.model;

import java.util.Random;

/**
 * RandomDecorator class that implements Randomable
 */
public class RandomDecorator implements Randomable {
  private final Random rand;

  // Create the Decorator exactly the same way you would create Random normally
  public RandomDecorator() {
    this.rand = new Random();
  }

  public RandomDecorator(int seed) {
    this.rand = new Random(seed);
  }

  // Implement the method from the interface; use the Random object
  @Override
  public int nextInt(int bound) {
    return this.rand.nextInt(bound);
  }
}
