/**
 *
 */
package shea.ev3.pong;

import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.lcd.LCD;

/**
 * A ball
 * @author shea
 */
public class Ball {

	/**
	 * Holds the X and Y positions of the ball
	 * as well as the delta X and delta Y
	 */
	private int x, y, dx, dy;

	/**
	 * Constructor
	 */
	public Ball() {
		reset();
	}

	/**
	 * Initialize the movement variables
	 */
	private void reset() {
		x = LCD.SCREEN_WIDTH/2;
		y = LCD.SCREEN_HEIGHT/2;
		dx = 1;
		dy = 0;
	}

	/**
	 * Update the ball's position
	 */
	public void update() {
		x += dx;
		y += dy;

		if (y <= 0 || y >= LCD.SCREEN_HEIGHT) {
			y -= dy;
			dy = -dy;
		}
	}

	/**
	 * Check to see if the ball will bounce off a paddle or be lost
	 * @param paddle the paddle to check for collision
	 * @return whether the ball bounced or not
	 */
	public boolean checkForBounce(Paddle paddle) {
		int paddleY = paddle.getPos();
		if (paddleY <= y && y <= (paddleY + Paddle.HEIGHT)) {
			dy = (y - paddleY - Paddle.HEIGHT/2)/2;
			dx = -dx;
			x += dx;
			return true;
		} else {
			reset();
			return false;
		}
	}

	/**
	 * Draw the ball on the LCD screen
	 * @param g GraphicsLCD object
	 */
	public void draw(GraphicsLCD g) {
		g.drawRect(x-1, y-1, 1, 1);
		g.drawRect(x-2, y-2, 3, 3);
	}

	/**
	 * Retrieve the X position of the ball
	 * @return
	 */
	public int getX() {
		return x;
	}

	/**
	 * Retrieve the Y position of the ball
	 * @return
	 */
	public int getY() {
		return y;
	}
}
