package edu.vt.rhids.main;

public class Parameters
{
	public static final IntegerRange DEFAULT_EPOCH_SIZE = new IntegerRange(2500, 2500, 50000);
	public static final FloatRange DEFAULT_TRAIN_THRESHOLD = new FloatRange(0.9f, 0.001f, 1f);
	public static final IntegerRange DEFAULT_TEST_THRESHOLD = new IntegerRange(5, 5, 200);
	
	public String inputFile;
	public IntegerRange epochSize = DEFAULT_EPOCH_SIZE;
	public FloatRange trainThreshold = DEFAULT_TRAIN_THRESHOLD;
	public IntegerRange testThreshold = DEFAULT_TEST_THRESHOLD;

	public void setNormalFilePath(String normalFilePath)
	{
		this.inputFile = normalFilePath;
	}

	public void setEpochSize(String range)
	{
		this.epochSize.set(range);
	}

	public void setTrainThreshold(String range)
	{
		this.trainThreshold.set(range);
	}

	public void setTestThreshold(String range)
	{
		this.testThreshold.set(range);
	}

	public String toString()
	{
		String output = new String();
		output += "\nInput file: " + inputFile;
		output += "\nEpoch-size range: " + epochSize;
		output += "\nTraining-threshold range: " + trainThreshold;
		output += "\nTest-threshold range: " + testThreshold;
		return output;
	}

	public static class IntegerRange
	{
		public int min, step, max;

		private IntegerRange(int min, int step, int max)
		{
			this.min = min;
			this.step = step;
			this.max = max;
		}

		public void set(String range)
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
				System.out.println("Parsing failed: invalid range");
				break;
			}
		}
		
		public String toString()
		{
			return "[" + min + ((max == min) ? "" : ":" + step + ":" + max) + "]";
		}
	}

	public static class FloatRange
	{
		public float min, step, max;

		private FloatRange(float min, float step, float max)
		{
			this.min = min;
			this.step = step;
			this.max = max;
		}

		public void set(String range)
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
				System.out.println("Parsing failed: invalid range");
				break;
			}
		}
		
		public String toString()
		{
			return "[" + min + ((max == min) ? "" : ":" + step + ":" + max) + "]";
		}
	}
	
	public static class Range<T>
	{
		public T min, step, max;

		private Range(T min, T step, T max)
		{
			this.min = min;
			this.step = step;
			this.max = max;
		}

		public void set(String range)
		{
			if (range == null)
			{
				return;
			}
			String[] values = range.split(":");
			min = parse(values[0]);
			switch (values.length)
			{
			case 1:
				max = min;
				break;
			case 2:
				max = parse(values[1]);
				break;
			case 3:
				step = parse(values[1]);
				max = parse(values[2]);
				break;
			default:
				System.out.println("Parsing failed: invalid range");
				break;
			}
		}
		private T parse(String value)
		{
			// TODO (AmrAbed): Implement this
			return null;
		}
		
		public String toString()
		{
			return "[" + min + ((max == min) ? "" : ":" + step + ":" + max) + "]";
		}
	}


}