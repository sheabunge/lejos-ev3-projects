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
	private RegulatedMotor control;
	private int initTacho, currentTacho, prevTacho;

	private int y;
	private final int x;

	final static int HEIGHT = SCREEN_HEIGHT/4;
	final static int WIDTH = 1;
	final static int MIN = 0;
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

		// only update if tacho position has changed
		if (currentTacho == prevTacho) return;

		// calculate the new paddle position
		y += currentTacho - prevTacho;

		// make sure paddle does not exceed bounds
		if (y < MIN) y = MIN;
		if (y > MAX) y = MAX;

		// update previous tacho
		prevTacho = currentTacho;
	}

	public void draw(GraphicsLCD g) {
		g.drawRect(x, y, WIDTH, HEIGHT);
	}

	public int getPos() {
		return y;
	}
}
