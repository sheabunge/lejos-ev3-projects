package shea.ev3.utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shea
 *
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

	public FileAccess(String filename) {
		this.filename = filename;
		path = Paths.get(filename);
		file = new File(filename);
	}

	public boolean fileExists() {
		return file.exists();
	}

	public void delete() {
		try {
			Files.deleteIfExists(path);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public void create() {
		try {
			Files.createFile(path);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public void writeData(List<String> list) {
		String[] data = new String[list.size()];
		list.toArray(data);
		writeData(data);
	}

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
