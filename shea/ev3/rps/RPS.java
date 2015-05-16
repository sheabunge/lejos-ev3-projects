/**
 *
 */
package shea.ev3.rps;

import java.util.Random;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;
import shea.ev3.utils.LCDUtils;

/**
 * @author shea
 *
 */
public class RPS implements Runnable {

	/**
	 * Main class instance
	 */
	private RockPaperScissors control;

	/**
	 * Scoreboard class instance
	 */
	private Scoreboard scores = new Scoreboard();

	/**
	 * Value assigned to different moves
	 */
	public final static int ROCK = 0, PAPER = 1, SCISSORS = 2;

	/**
	 * Value assigned to different results
	 */
	public final static int USER_WIN = 0, WIN = 0, ROBOT_WIN = 1, LOSS = 1, DRAW = 2, TIE = 2;

	/**
	 * Constructor
	 * @param control
	 */
	public RPS(RockPaperScissors control) {
		this.control = control;
	}

	public void countdown() {
		long delay = 1500;
		LCD.clear();
		Delay.msDelay(delay);

		LCDUtils.drawString("Rock", 1);
		Delay.msDelay(delay);

		LCDUtils.drawString("Paper", 3);
		Delay.msDelay(delay);

		LCDUtils.drawString("Scissors", 5);
		Delay.msDelay(delay);

		LCDUtils.drawString("GO!", 7);
	}

	/**
	 * Run the game
	 */
	@Override
	public void run() {
		while (true) {

			// display a countdown and wait for user input
			countdown();
			int robotMove = getRandomMove();
			int userMove = control.waitForButtonPress();

			// indicate our move
			control.switchOnLamp(robotMove);

			// calculate result
			int result = checkWinner(userMove, robotMove);

			// draw scoreboard
			scores.addMoves(userMove, robotMove);
			scores.addResult(result);
			LCD.clear();
			scores.draw();
			scores.drawPointers(userMove, robotMove, result);

			// turn on LED
			control.led.setPattern(result + 1);

			// wait for button press seconds
			Button.ENTER.waitForPressAndRelease();

			// reset
			control.switchOffLamp(robotMove);
			scores.clearPointers(userMove, robotMove, result);
			control.led.setPattern(0);
		}
	}

	/**
	 * Get a randomly decided move
	 * @return the move number
	 */
	public int getRandomMove() {
		Random rand = new Random();
		return rand.nextInt(2);
	}

	/**
	 * Compare moves and calculate a result
	 * @param userMove a move number
	 * @param robotMove a move number
	 * @return the result number
	 */
	private int checkWinner(final int userMove, final int robotMove) {

		if (userMove == ROCK) {
			if (robotMove == PAPER) {
				return ROBOT_WIN; // paper beats rock
			} else if (robotMove == SCISSORS ) {
				return USER_WIN; // rock beats scissors
			}
		} else if (userMove == PAPER) {
			if (robotMove == ROCK) {
				return USER_WIN; // paper beats rock
			} else if (robotMove == SCISSORS) {
				return ROBOT_WIN; // scissors beat paper
			}
		} else if (userMove == SCISSORS) {
			if (robotMove == ROCK) {
				return ROBOT_WIN; // rock beats scissors
			} else if (robotMove == PAPER) {
				return USER_WIN; // scissors beat paper
			}
		}

		return DRAW;
	}
}
