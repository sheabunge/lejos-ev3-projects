package shea.ev3.utils;

import static lejos.hardware.lcd.LCD.*;
import lejos.hardware.lcd.Font;

/**
 * Utility class containing methods for drawing text on the LCD screen
 * @author shea
 */
public class TextUtils {

	/**
	 * Retrieve the X position for some text to be centered on the LCD screen
	 * @param str the text to calculate the X position for
	 * @return the text's centered X position
	 */
	public static int getCenteredTextX(String str) {
		return getCenteredTextX(str.length());
	}

	/**
	 * Retrieve the X position for some text to be centered on the LCD screen
	 * @param int the length of the text
	 * @return the centered X position
	 */
	public static int getCenteredTextX(int strlen) {

		// length of printed string
		int x = strlen * FONT_WIDTH;

		// center text on screen
		x = (SCREEN_WIDTH - x) / 2;

		// correction for odd-length strings
		x += x % 2;

		return x;
	}

	/**
	 * Draw a string at real X and Y coordinates
	 * @param str the string to draw
	 * @param x the X coordinate
	 * @param y the Y coordinate
	 */
	public static void realDrawString(String str, int x, int y) {
		realDrawString(str, x, y, false);
	}

	/**
	 * Draw a string at real X and Y positions
	 * @param str the string to draw
	 * @param x the X coordinate
	 * @param y the Y coordinate
	 * @param inverted draw the text in white?
	 */
	public static void realDrawString(String str, int x, int y, boolean inverted) {
		Font font = Font.getDefaultFont();
		char[] strData = str.toCharArray();

		// Draw the background rectangle
		bitBlt(null,
				SCREEN_WIDTH, SCREEN_HEIGHT, 0, 0,
				x, y, strData.length * font.glyphWidth, font.height,
				inverted ? ROP_SET : ROP_CLEAR);

		// Draw the characters
		for (int i = 0; (i < strData.length); i++) {
			bitBlt(font.glyphs,
					font.width * font.glyphCount, font.height, font.width * (strData[i]-32), 0,
					x + i * font.glyphWidth, y, font.width, font.height,
					inverted ? ROP_COPYINVERTED : ROP_COPY);
		}
	}

	/**
	 * Draw a string centered on the screen
	 * @param str the text to draw
	 * @param line the line number
	 */
	public static void drawString(String str, int line) {
		int y = line * FONT_HEIGHT;
		int x = getCenteredTextX(str);
		realDrawString(str, x, y);
	}
}
