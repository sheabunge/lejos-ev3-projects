package shea.ev3.pong;

import static lejos.hardware.lcd.LCD.SCREEN_HEIGHT;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.lcd.LCD;
import lejos.robotics.RegulatedMotor;

/**
 * Used for the two player paddles
 * @author shea
 */
public class Paddle {

	/**
	 * Motor controller
	 */
	private RegulatedMotor control;

	/**
	 * Stores the initial, current and previous tacho counts
	 */
	private int initTacho, currentTacho, prevTacho;

	/**
	 * Stores the paddle Y position
	 */
	private int y;

	/**
	 * Stores the constant paddle X position
	 */
	private final int x;

	/**
	 * The height of the paddle
	 */
	final static int HEIGHT = SCREEN_HEIGHT/4;

	/**
	 * The width of the paddle
	 */
	final static int WIDTH = 1;

	/**
	 * The minimum movement boundary of the paddle
	 */
	final static int MIN = 0;

	/**
	 * The maximum movement boundary of the paddle
	 */
	final static int MAX = SCREEN_HEIGHT - HEIGHT;

	/**
	 * Constructor
	 * @param motor
	 */
	public Paddle(RegulatedMotor motor, int player) {
		control = motor;
		initTacho = control.getTachoCount();
		currentTacho = initTacho;
		prevTacho = initTacho;

		y = SCREEN_HEIGHT/2 - HEIGHT/2;
		x = (player == 1) ? (LCD.SCREEN_WIDTH - WIDTH*2) : 0;
	}

	/**
	 * Update the paddle Y position
	 */
	public void update() {
		currentTacho = control.getTachoCount() - initTacho;

		// only update if tachometer position has changed
		if (currentTacho == prevTacho) return;

		// calculate the new paddle position
		y += currentTacho - prevTacho;

		// make sure paddle does not exceed bounds
		if (y < MIN) y = MIN;
		if (y > MAX) y = MAX;

		// update previous tachometer value
		prevTacho = currentTacho;
	}

	/**
	 * Draw the paddle on the LCD screen
	 * @param g a GraphicsLCD object
	 */
	public void draw(GraphicsLCD g) {
		g.drawRect(x, y, WIDTH, HEIGHT);
	}

	/**
	 * Retrieve the paddle Y position
	 * @return
	 */
	public int getPos() {
		return y;
	}
}
