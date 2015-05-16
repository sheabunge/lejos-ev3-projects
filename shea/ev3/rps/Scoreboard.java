/**
 *
 */
package shea.ev3.rps;
import static lejos.hardware.lcd.LCD.FONT_HEIGHT;
import static lejos.hardware.lcd.LCD.FONT_WIDTH;
import static lejos.hardware.lcd.LCD.drawString;
import static shea.ev3.rps.RPS.*;
import lejos.hardware.BrickFinder;
import lejos.hardware.lcd.GraphicsLCD;

/**
 * Handles keeping track of moves and results
 * as well as displaying them on the screen
 * @author shea
 */
public class Scoreboard {

	/**
	 * A GraphicsLCD instance
	 */
	private GraphicsLCD g = BrickFinder.getDefault().getGraphicsLCD();

	/**
	 * The X and Y positions of the scoreboard
	 */
	private final int x = 4, y = 3;

	/**
	 * Stores counts of the moves and results
	 */
	private int[] robotMoves = new int[3], userMoves = new int[3], results = new int[3];

	/**
	 * Draw the scoreboard
	 */
	public void draw() {
		drawString("R   P   S", x, y);
		drawUserScores(robotMoves, y-2);
		drawUserScores(userMoves, y+2);
		drawString(String.format("W: %d  L: %d  D: %d", results[WIN], results[LOSS], results[DRAW]), 1, 7);
	}

	/**
	 * Draw a row of user scores
	 * @param moves
	 * @param line
	 */
	protected void drawUserScores(final int[] moves, final int line) {
		drawString(moves[ROCK] + "   " + moves[PAPER] + "   " + moves[SCISSORS], x, line);
	}

	/**
	 * Draw a box around some text
	 * @param str the text to draw the box around
	 * @param x the X position of the text
	 * @param y the Y position of the text
	 */
	protected void drawBox(String str, int x, int y) {
		final int padding = 2;

		x = x * FONT_WIDTH - padding;
		y = y * FONT_HEIGHT - padding;

		int width = FONT_WIDTH * str.length() + padding;
		int height = FONT_HEIGHT + padding;

		g.drawRect(x, y, width, height);
	}

	/**
	 * Draw boxes around the changed scores
	 * @param userMove the move just made by the user
	 * @param robotMove the move just made by the robot
	 * @param result the most recent result
	 */
	public void drawPointers(int userMove, int robotMove, int result) {
		drawBox(Integer.toString(userMove), x + 4 * userMove, y + 2);
		drawBox(Integer.toString(robotMove), x + 4 * robotMove , y - 2);
		drawBox(Integer.toString(result), 6 * result + 4, 7);
	}

	/**
	 * Clear the previously drawn boxes
	 * @param userMove the user moves box to clear
	 * @param robotMove the robot moves box to clear
	 * @param result the result box to clear
	 */
	public void clearPointers(int userMove, int robotMove, int result) {
		g.setColor(GraphicsLCD.WHITE);
		drawPointers(userMove, robotMove, result);
		g.setColor(GraphicsLCD.BLACK);
	}

	/**
	 * Increase the moves count
	 * @param userMove the user move to increase
	 * @param robotMove the robot move to increase
	 */
	public void addMoves(int userMove, int robotMove) {
		userMoves[userMove]++;
		robotMoves[robotMove]++;
	}

	/**
	 * Increase the count of a result
	 * @param result the result to increase
	 */
	public void addResult(int result) {
		results[result]++;
	}
}
