package shea.ev3.pong;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.lcd.LCD;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

/**
 * The classic pong game for the EV3
 * @author shea
 */
public class Pong {

	/**
	 * Holds the player paddles
	 */
	private Paddle paddle1, paddle2;

	/**
	 * Holds the ball
	 */
	private Ball ball;

	/**
	 * Holds the players' scores
	 */
	private int playerOneScore = 0, playerTwoScore = 0;

	Brick ev3 = BrickFinder.getDefault();
	GraphicsLCD g = ev3.getGraphicsLCD();
	TextLCD t = ev3.getTextLCD();

	/**
	 * Constructor
	 * @param playerOneMotor the motor used to control player one's paddle
	 * @param playerTwoMotor the motor used to control player two's  paddle
	 */
	public Pong(RegulatedMotor playerOneMotor, RegulatedMotor playerTwoMotor) {
		paddle1 = new Paddle(playerOneMotor, 0);
		paddle2 = new Paddle(playerTwoMotor, 1);
		ball = new Ball();
	}

	/**
	 * Run the game
	 */
	public void run() {
		drawNet();

		while (! Button.ESCAPE.isDown()) {
			clearScreen();

			paddle1.update();
			paddle2.update();
			ball.update();

			if (ball.getX() < 0) {
				if (! ball.checkForBounce(paddle1)) {
					playerTwoScore++;
				}
			} else if (ball.getX() > LCD.SCREEN_WIDTH) {
				if (! ball.checkForBounce(paddle2)) {
					playerOneScore++;
				}
			}

			g.setColor(GraphicsLCD.BLACK);
			drawScreen();
			Delay.msDelay(60);
		}
	}

	/**
	 * Draw the player score on the screen
	 * @param t TextLCD object
	 */
	public void drawScore() {
		t.drawInt(playerOneScore, 7, 7);
		t.drawInt(playerTwoScore, 10, 7);
	}

	/**
	 * Draw the pong net
	 * @param g GraphicsLCD object
	 */
	public void drawNet() {
		final int x = LCD.SCREEN_WIDTH/2, width = 1, height = 10;

		for (int y = height; y < LCD.SCREEN_HEIGHT; y += height*2 ) {
			g.drawRect(x, y, width, height);
		}
	}

	public void clearScreen() {
		drawScreen(GraphicsLCD.WHITE);
	}

	public void drawScreen() {
		drawScreen(GraphicsLCD.BLACK);
	}

	/**
	 * Draw on the LCD screen
	 */
	public void drawScreen(int color) {
		g.setColor(color);

		paddle1.draw(g);
		paddle2.draw(g);
		ball.draw(g);
		drawScore();
	}

	/**
	 * Main method
	 * @param args
	 */
	public static void main(String[] args) {
		Pong pong = new Pong(new NXTRegulatedMotor(MotorPort.C), new NXTRegulatedMotor(MotorPort.D));
		pong.run();
	}
}
