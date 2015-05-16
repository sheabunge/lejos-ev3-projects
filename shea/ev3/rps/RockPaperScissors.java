package shea.ev3.rps;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.LED;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.AnalogSensor;
import lejos.hardware.sensor.NXTTouchSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.TouchAdapter;

/**
 * @author shea
 *
 */
public class RockPaperScissors {

	/**
	 * The EV3 brick
	 */
	private final Brick ev3 = BrickFinder.getDefault();

	/**
	 * Touch sensors and abstractions
	 */
	private AnalogSensor[] touchSensors = new AnalogSensor[3];
	private TouchAdapter[] buttons = new TouchAdapter[3];

	/**
	 * Motors
	 */
	private RegulatedMotor[] lamps = new RegulatedMotor[3];

	/**
	 * LED lights
	 */
	public final LED led = ev3.getLED();

	/**
	 * Run the program
	 */
	public RockPaperScissors() {
		RPS rps = new RPS(this);
		Thread game = new Thread(rps);
		game.setDaemon(true);

		openSenors();
		game.start();

		Button.ESCAPE.waitForPressAndRelease();
		game.interrupt();
		closeSensors();
	}

	/**
	 * Wait until a touch sensor button is pressed
	 * @return the number of the pressed button (0, 1, or 2)
	 */
	public int waitForButtonPress() {

		while (! (buttons[0].isPressed() || buttons[1].isPressed() || buttons[2].isPressed()) ) {}

		if (buttons[0].isPressed()) {
			return 0;
		} else if (buttons[1].isPressed()) {
			return 1;
		} else {
			return 2;
		}
	}

	/**
	 * Switch on a lamp
	 * @param lamp the lamp number
	 */
	public void switchOnLamp(int lamp) {
		lamps[lamp].forward();
	}

	/**
	 * Switch off a lamp
	 * @param lamp the lamp number
	 */
	public void switchOffLamp(int lamp) {
		lamps[lamp].flt();
	}


	/**
	 * Retrieve the corresponding output port letter
	 * @param port a number from 0-3
	 * @return "A", B", "C", or "D"
	 */
	private static String getPortLetter(int port) {
		switch (port) {
		case 0:
			return "A";
		case 1:
			return "B";
		case 2:
			return "C";
		case 3:
		default:
			return "D";
		}
	}

	/**
	 * Initialize the touch sensors and lamps
	 */
	private void openSenors() {
		Port lampPort, touchPort;

		for (int i=0; i < 3; i++) {
			lampPort = ev3.getPort(getPortLetter(i));
			lamps[i] = new EV3MediumRegulatedMotor(lampPort);

			touchPort = ev3.getPort("S" + (i+1));
			touchSensors[i] = new NXTTouchSensor(touchPort);
			buttons[i] = new TouchAdapter(touchSensors[i]);
		}
	}

	/**
	 * Close the touch sensors and lamps
	 */
	private void closeSensors() {
		for (int i=0; i < 3; i++) {
			lamps[i].close();
			touchSensors[i].close();
		}
	}

	/**
	 * Main method
	 * @param args
	 */
	public static void main(String[] args) {
		new RockPaperScissors();
	}
}