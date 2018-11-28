package edu.vt.rhids.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class Logger {
	private static Verbosity level = Verbosity.NONE;

	public static void log(Object object, Verbosity level) {
		if (level.compareTo(Logger.level) <= 0) {
			System.out.println(object);
		}
	}

	public static void emphasize(Object object, Verbosity level) {
		if (level.compareTo(Logger.level) <= 0) {
			System.out.println("\033[1m" + object + "\033[0m");
		}
	}

	public static void signal(Object object, Verbosity level) {
		if (level.compareTo(Logger.level) <= 0) {
			System.out.println("\u001B[31m" + object + "\u001B[0m");
		}
	}

	public static void setLevel(int level) {
		switch (level) {
			case 1:
				Logger.level = Verbosity.LOW;
				break;
			case 2:
				Logger.level = Verbosity.MEDIUM;
				break;
			case 3:
				Logger.level = Verbosity.HIGH;
				break;
			case 4:
				Logger.level = Verbosity.ALL;
				break;
			default:
				Logger.level = Verbosity.NONE;
				break;
		}
	}

	public static void setHandler(String filename) throws FileNotFoundException {
		System.setOut(new PrintStream(new File(filename)));
	}

	public enum Verbosity {
		NONE, LOW, MEDIUM, HIGH, ALL
	}
}
