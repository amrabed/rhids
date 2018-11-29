package edu.vt.rhids.main;

import edu.vt.rhids.util.Logger;
import picocli.CommandLine;

public class Parameters {
	private static final String PARSE_FAILED = "Parsing failed: invalid range";

	final String inputFile;
	final String databaseFile;
	final IntegerRange epochSize;
	final FloatRange trainThreshold;
	final IntegerRange testThreshold;

	public Parameters(String inputFile, String databaseFile, IntegerRange epochSize, FloatRange trainThreshold, IntegerRange testThreshold) {
		this.inputFile = inputFile;
		this.databaseFile = databaseFile;
		this.epochSize = epochSize;
		this.trainThreshold = trainThreshold;
		this.testThreshold = testThreshold;
	}

	@Override
	public String toString() {
		return "\nInput file: " + inputFile +
				"\nEpoch-size range: " + epochSize +
				"\nTraining-threshold range: " + trainThreshold +
				"\nTest-threshold range: " + testThreshold;
	}

	public static class IntegerRange implements CommandLine.ITypeConverter<IntegerRange> {
		int min;
		int step;
		int max;


		// Needed for picocli instantitation
		@SuppressWarnings("unused")
		public IntegerRange() {
		}

		IntegerRange(String range) {
			if (range == null) {
				return;
			}
			String[] values = range.split(":");
			min = Integer.parseInt(values[0]);
			switch (values.length) {
				case 1:
					max = min;
					break;
				case 2:
					max = Integer.parseInt(values[1]);
					break;
				case 3:
					step = Integer.parseInt(values[1]);
					max = Integer.parseInt(values[2]);
					break;
				default:
					Logger.log(PARSE_FAILED, Logger.Verbosity.NONE);
					break;
			}
		}

		@Override
		public String toString() {
			return "[" + min + ((max == min) ? "" : ":" + step + ":" + max) + "]";
		}

		@Override
		public IntegerRange convert(String value) {
			return new IntegerRange(value);
		}
	}

	public static class FloatRange implements CommandLine.ITypeConverter<FloatRange> {
		float min;
		float step;
		float max;

		// Needed for picocli instantitation
		@SuppressWarnings("unused")
		public FloatRange() {
		}

		FloatRange(String range) {
			if (range == null) {
				return;
			}
			String[] values = range.split(":");
			min = Float.parseFloat(values[0]);
			switch (values.length) {
				case 1:
					max = min;
					break;
				case 2:
					max = Float.parseFloat(values[1]);
					break;
				case 3:
					step = Float.parseFloat(values[1]);
					max = Float.parseFloat(values[2]);
					break;
				default:
					Logger.log(PARSE_FAILED, Logger.Verbosity.NONE);
					break;
			}
		}

		@Override
		public String toString() {
			return "[" + min + ((max == min) ? "" : ":" + step + ":" + max) + "]";
		}

		@Override
		public FloatRange convert(String value) {
			return new FloatRange(value);
		}
	}
}