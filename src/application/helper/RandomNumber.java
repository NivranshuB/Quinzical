package application.helper;

import java.util.Random;

public class RandomNumber {

	/**
	 * Static method that given the lower limit and the upper limit, randomly returns an int
	 * value n such that, low <= n < high.
	 * @param low: lower limit
	 * @param high: upper limit
	 * 
	 * @return random int value within range
	 */
	public static int generateBetween(int low, int high) {
		Random random = new Random();
		return random.nextInt(high-low) + low;
	}
}
