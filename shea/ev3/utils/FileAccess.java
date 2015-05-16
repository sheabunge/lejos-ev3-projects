package shea.ev3.utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Convenience class for reading and writing data to files on the EV3
 * @author shea
 */
public class FileAccess {

	/**
	 * Stores the filename, without path
	 */
	public final String filename;

	/**
	 * Path object
	 */
	private Path path;

	/**
	 * File object
	 */
	private File file;

	/**
	 * Constructor
	 * @param filename the file's name
	 */
	public FileAccess(String filename) {
		this.filename = filename;
		path = Paths.get(filename);
		file = new File(filename);
	}

	/**
	 * Check if the file exists
	 * @return boolean true if exists, false if not
	 */
	public boolean fileExists() {
		return file.exists();
	}

	/**
	 * Deletes a file if it exists
	 */
	public void delete() {
		try {
			Files.deleteIfExists(path);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Creates a new empty file, unless it already exists
	 */
	public void create() {
		try {
			Files.createFile(path);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Writes a String of data to a file
	 * @param data the data to write
	 */
	public void write(String data) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
			if (! file.exists()) {
				file.createNewFile();
			}

			writer.write(data);
		} catch(IOException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Writes data from a List of Strings to a file.
	 * Each List element forms a separate line
	 * @param list the data to write.
	 */
	public void writeData(List<String> list) {
		String[] data = new String[list.size()];
		list.toArray(data);
		writeData(data);
	}

	/**
	 * Writes an array of String data to a file.
	 * Each array element forms a separate line
	 * @param data the data to write
	 */
	public void writeData(String[] data) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
			if (! file.exists()) {
				file.createNewFile();
			}

			for (String line : data) {
				writer.append(line);
				writer.newLine();
			}
			writer.flush();
		} catch(IOException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Reads data from a file to a List
	 * @return the retrieved data, with each List element representing a line in the file
	 */
	public List<String> readData() {
		List<String> data = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
			String line;

			while ((line = reader.readLine()) != null) {
				data.add(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return data;
	}
}
