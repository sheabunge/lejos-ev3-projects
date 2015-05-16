package shea.ev3;

import static lejos.hardware.lcd.LCD.SCREEN_HEIGHT;
import static lejos.hardware.lcd.LCD.SCREEN_WIDTH;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.LED;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;
import shea.ev3.utils.FileAccess;

/**
 * Scribble pad game
 * @author shea
 */
public class ScribblePad {

	/**
	 * Holds the drawing pointer position
	 */
	private int x, y;

	/**
	 * Holds all points
	 */
	private List<String> points = new ArrayList<>();

	/**
	 * Holds the drawing motor objects
	 */
	private RegulatedMotor motorX, motorY;

	/**
	 * The last tacho meter X and Y position
	 * Used to check if they have been changed
	 */
	private int lastTachoX, lastTachoY;

	/**
	 * These constants hold the coordinates of the borders of the screen
	 *
	 * In order to have a user-friendly experience of beginning drawing in the
	 * center of the screen, this class uses a different coordinate system to the EV3
	 * where (0,0) is in the center of the screen, x decreases to the left and increases
	 * to the right, and y increases to the top and decreases to the bottom.
	 */
	public final static int
	LEFT_BORDER = -SCREEN_WIDTH/2,
	RIGHT_BORDER = SCREEN_WIDTH/2,
	TOP_BORDER = SCREEN_HEIGHT/2,
	BOTTOM_BORDER = -SCREEN_HEIGHT/2;

	/**
	 * Constructor
	 */
	public ScribblePad(RegulatedMotor motorX, RegulatedMotor motorY) {
		this.motorX = motorX;
		this.motorY = motorY;

		// initialize pad variables
		resetPad();
		// clear the display
		LCD.clear();
	}

	/**
	 * Reset the drawing variables. Does not clear the LCD screen
	 */
	public void resetPad() {
		x = 0;
		y = 0;
		points.clear();

		motorX.resetTachoCount();
		motorY.resetTachoCount();
	}

	/**
	 * Run the pad operations
	 */
	public void run() {
		drawPoint();

		// keep looping until exit button is pressed
		while (Button.getButtons() != Button.ID_ESCAPE) {

			// clear the screen if the enter button is pressed
			if (Button.getButtons() == Button.ID_ENTER) {
				resetPad();
				LCD.clear();
				Delay.msDelay(300);
				drawPoint();
			}

			checkTacho();
			checkKeys();

			Delay.msDelay(15);
		}
	}

	/**
	 * Check tachometer position and update the point
	 */
	private void checkTacho() {
		int tachoX = motorX.getTachoCount();
		int tachoY = motorY.getTachoCount();

		if (lastTachoX < tachoX ) {
			moveLeft();
		} else if (lastTachoX > tachoX) {
			moveRight();
		}

		if (lastTachoY < tachoY) {
			moveUp();
		} else if (lastTachoY > tachoY) {
			moveDown();
		}

		lastTachoX = tachoX;
		lastTachoY = tachoY;
	}

	/**
	 * Check for key presses and update the point
	 */
	private void checkKeys() {
		int buttonState = Button.getButtons();
		switch (buttonState) {

		case Button.ID_LEFT:
			moveLeft();
			break;

		case Button.ID_RIGHT:
			moveRight();
			break;

		case Button.ID_UP:
			moveUp();
			break;

		case Button.ID_DOWN:
			moveDown();
			break;
		}
	}

	/**
	 * Move the pointer to the left
	 */
	public void moveLeft() {
		x -= 1;

		if ( x < LEFT_BORDER) {
			x = LEFT_BORDER;
		}

		drawPoint();
	}

	/**
	 * Move the pointer to the right
	 */
	public void moveRight() {
		x += 1;

		if ( x > RIGHT_BORDER) {
			x = RIGHT_BORDER;
		}

		drawPoint();
	}

	/**
	 * Move the pointer up
	 */
	public void moveUp() {
		y += 1;

		if ( y > TOP_BORDER) {
			y = TOP_BORDER;
		}

		drawPoint();
	}

	/**
	 * Move the pointer down
	 */
	public void moveDown() {
		y -= 1;

		if ( y < BOTTOM_BORDER) {
			y = BOTTOM_BORDER;
		}

		drawPoint();
	}

	/**
	 * Draw the current point
	 */
	private void drawPoint() {

		// save to log
		points.add(x + "," + y);

		// convert points from our coordinate system to the EV3 system
		int realX = x + SCREEN_WIDTH/2;
		int realY = -y + SCREEN_HEIGHT/2;

		LCD.setPixel(realX, realY, 1);
	}

	/**
	 * Retrieve all current points in CSV format
	 * @return
	 */
	public List<String> getPoints() {
		return points;
	}

	/**
	 * Load points from a list
	 * @param points String list of comma-separated x and y coordinates
	 */
	public void loadPoints(List<String> points) {
		Iterator<String> i = points.iterator();

		while (i.hasNext()){
			String[] coordinates = i.next().split(",");
			x = Integer.parseInt(coordinates[0]);
			y = Integer.parseInt(coordinates[1]);
			drawPoint();
		}
	}

	/**
	 * Main method
	 * @param args
	 */
	public static void main(String[] args) {
		final LED led = BrickFinder.getLocal().getLED();
		led.setPattern(2); // red
		LCD.drawString("Please wait...", 0, 0);

		// Initialize class
		NXTRegulatedMotor leftMotor = new NXTRegulatedMotor(MotorPort.B);
		NXTRegulatedMotor rightMotor = new NXTRegulatedMotor(MotorPort.C);
		ScribblePad pad = new ScribblePad(leftMotor, rightMotor);

		// Attempt to load points from file
		led.setPattern(3); // orange
		FileAccess saveFile = new FileAccess("scribblepad.csv");
		if (saveFile.fileExists()) {
			LCD.clear();
			pad.loadPoints(saveFile.readData());
		}

		// set timer to turn off light
		led.setPattern(1);
		Thread t = new Thread(new Runnable() { @Override public void run() {
			Delay.msDelay(1000);
			led.setPattern(0);
		}});
		t.start();

		// Run the scribble pad program
		pad.run();

		// Save points to a file
		saveFile.delete();
		saveFile.writeData(pad.getPoints());
	}
}
