package edu.vt.rhids.main;

import edu.vt.rhids.util.Logger;

class Parameters {
	static final IntegerRange DEFAULT_EPOCH_SIZE = new IntegerRange(2500, 2500, 50000);
	static final FloatRange DEFAULT_TRAIN_THRESHOLD = new FloatRange(0.9f, 0.001f, 1f);
	static final IntegerRange DEFAULT_TEST_THRESHOLD = new IntegerRange(5, 5, 200);
	private static final String PARSE_FAILED = "Parsing failed: invalid range";
	String inputFile;
	String databaseFile;
	IntegerRange epochSize = DEFAULT_EPOCH_SIZE;
	FloatRange trainThreshold = DEFAULT_TRAIN_THRESHOLD;
	IntegerRange testThreshold = DEFAULT_TEST_THRESHOLD;

	void setNormalFilePath(String normalFilePath)
	{
		this.inputFile = normalFilePath;
	}

	void setDatabaseFilePath(String databaseFile)
	{
		this.databaseFile = databaseFile;
	}

	void setEpochSize(String range)
	{
		this.epochSize.set(range);
	}

	void setTrainThreshold(String range)
	{
		this.trainThreshold.set(range);
	}

	void setTestThreshold(String range)
	{
		this.testThreshold.set(range);
	}

	@Override
	public String toString()
	{
		String output = "";
		output += "\nInput file: " + inputFile;
		output += "\nEpoch-size range: " + epochSize;
		output += "\nTraining-threshold range: " + trainThreshold;
		output += "\nTest-threshold range: " + testThreshold;
		return output;
	}

	public static class IntegerRange
	{
		int min;
		int step;
		int max;

		private IntegerRange(int min, int step, int max)
		{
			this.min = min;
			this.step = step;
			this.max = max;
		}

		private void set(String range)
		{
			if (range == null)
			{
				return;
			}
			String[] values = range.split(":");
			min = Integer.parseInt(values[0]);
			switch (values.length)
			{
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
		public String toString()
		{
			return "[" + min + ((max == min) ? "" : ":" + step + ":" + max) + "]";
		}
	}

	public static class FloatRange
	{
		float min;
		float step;
		float max;

		private FloatRange(float min, float step, float max)
		{
			this.min = min;
			this.step = step;
			this.max = max;
		}

		private void set(String range)
		{
			if (range == null)
			{
				return;
			}
			String[] values = range.split(":");
			min = Float.parseFloat(values[0]);
			switch (values.length)
			{
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
		public String toString()
		{
			return "[" + min + ((max == min) ? "" : ":" + step + ":" + max) + "]";
		}
	}
}