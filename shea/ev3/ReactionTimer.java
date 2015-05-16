package shea.ev3;

import java.util.Random;

import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.LED;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.TouchAdapter;
import lejos.utility.Delay;
import lejos.utility.Stopwatch;
import shea.ev3.utils.FileAccess;

/**
 * Game to measure a user's reaction time
 * @author shea
 */
class ReactionTimer {

	/**
	 * Random number generator
	 */
	private static Random rand = new Random();

	/**
	 * Stopwatch timer
	 */
	private Stopwatch timer = new Stopwatch();

	/**
	 * EV3 LED lights
	 */
	private LED led = BrickFinder.getLocal().getLED();

	/**
	 * Touch sensor
	 */
	private TouchAdapter touch;

	/**
	 * The time it took the user to react
	 */
	private int reactionTime;

	/**
	 * Class constructor
	 * @param touchSensor
	 */
	public ReactionTimer(TouchAdapter touchSensor) {
		touch = touchSensor;
	}

	/**
	 * Get a random amount of time
	 * @return
	 */
	public static long getWaitTime() {
		return rand.nextInt(5000) + 3000;
	}

	/**
	 * Run the game
	 */
	public void run() {
		//		displayWelcome();

		// get random LED pattern
		int pattern = rand.nextInt(8) + 1;

		// delay for a random amount of time
		Delay.msDelay(getWaitTime());

		// ensure touch sensor is released
		while (touch.isPressed()) {}

		// switch on LED
		led.setPattern(pattern);

		// reset timer
		timer.reset();

		// wait for touch sensor to be pressed
		while (!touch.isPressed()) {}
		reactionTime = timer.elapsed();

		// Turn off LED
		led.setPattern(0);

		displayResults();
	}

	/**
	 * Display a welcome message
	 */
	@SuppressWarnings("unused")
	private void displayWelcome() {
		LCD.clear();
		LCD.drawString("Press any button", 1, 0);
		LCD.drawString("to start", 4, 1);
		Button.waitForAnyPress();

		LCD.drawString("Get ready...", 3, 3);
	}

	/**
	 * Display the user's results on the LCD screen
	 */
	private void displayResults() {
		LCD.clear();
		LCD.drawString("Your time:", 0, 1);
		LCD.drawString(reactionTime + " ms", 0, 2);

		int bestTime = getBestTime();
		LCD.drawString("Best time:", 0, 4);
		LCD.drawString(bestTime + " ms", 0, 5);
	}

	/**
	 * Retrieve the best reaction time from a file saved in storage
	 * @return
	 */
	public int getBestTime() {
		FileAccess file = new FileAccess("reaction_time.txt");

		if (file.fileExists()) {
			int savedTime = Integer.parseInt(file.readData().get(0));
			if ( savedTime < reactionTime ) {
				return savedTime;
			}
		}

		file.write(Integer.toString(reactionTime));
		return reactionTime;
	}


	/**
	 * Main method
	 * @param args arguments passed to the program
	 */
	public static void main(String[] args) {

		// set up touch sensor
		final Port s3 = BrickFinder.getDefault().getPort("S3");
		final EV3TouchSensor touchSensor = new EV3TouchSensor(s3);
		TouchAdapter touch = new TouchAdapter(touchSensor);

		// watch for exit key
		Thread exitWatcher = new Thread(new Runnable() { @Override public void run() {
			Button.waitForAnyPress();
			touchSensor.close();
			System.exit(0);
		}});
		exitWatcher.setDaemon(true);
		exitWatcher.start();

		// initialize class
		ReactionTimer rt = new ReactionTimer(touch);
		rt.run();
	}
}
