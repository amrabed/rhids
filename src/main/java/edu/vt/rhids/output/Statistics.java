package edu.vt.rhids.output;

import java.util.ArrayList;

/**
 * Statistics collected from running the test for certain epoch size, training
 * threshold, and testing threshold
 * 
 * @author AmrAbed
 *
 */
public class Statistics
{
	private final int epochSize, testThreshold;
	private final float trainThreshold;

	private final ArrayList<Integer> normalMismatchCounts = new ArrayList<>();
	private final ArrayList<Integer> anomalousMismatchCounts = new ArrayList<>();

	private int trainingEpochs, normalEpochs, anomalousEpochs;
	private int falseAlarms, trueAlarms;

	public Statistics(int epochSize, float trainThreshold, int testThreshold)
	{
		this.epochSize = epochSize;
		this.trainThreshold = trainThreshold;
		this.testThreshold = testThreshold;
	}

	public void incrementEpochs(boolean isAnomalous)
	{
		if (isAnomalous)
		{
			anomalousEpochs++;
		}
		else
		{
			normalEpochs++;
		}
	}

	public void incrementAlarms(boolean isAnomalous)
	{
		if (isAnomalous)
		{
			trueAlarms++;
		}
		else
		{
			falseAlarms++;
		}
	}

	public void addMismatchCount(int nMismatches, boolean isAnomalous)
	{
		if (isAnomalous)
		{
			anomalousMismatchCounts.add(nMismatches);
		}
		else
		{
			normalMismatchCounts.add(nMismatches);
		}

	}

	public int getTestThreshold()
	{
		return testThreshold;
	}

	public int getEpochSize()
	{
		return epochSize;
	}

	public float getTrainThreshold()
	{
		return trainThreshold;
	}

	public double getFalsePositiveRate()
	{
		return 100 * (double) falseAlarms / normalEpochs;
	}

	public double getTruePositiveRate()
	{
		return 100 * (double) trueAlarms / anomalousEpochs;
	}

	public String toString()
	{
		String output = new String();
		output += epochSize + ",";
		output += trainThreshold + ",";
		output += testThreshold + ",";
		output += trainingEpochs + ",";
		output += normalEpochs + ",";
		output += anomalousEpochs + ",";
		output += getAverage(normalMismatchCounts) + ",";
		output += getAverage(anomalousMismatchCounts) + ",";
		output += getFalsePositiveRate() + ",";
		output += getTruePositiveRate();
		return output;
	}

	private double getAverage(ArrayList<Integer> array)
	{
		double avg = 0;
		for (int entry : array)
		{
			avg += entry;
		}
		return avg / array.size();
	}

	public void print()
	{
		System.out.println("\nEpoch Size = " + epochSize);
		System.out.println("Training Threshold = " + trainThreshold);
		System.out.println("Test Threshold = " + testThreshold);
		System.out.println("Number of training epochs = " + trainingEpochs);
		System.out.println("Number of normal epochs = " + normalEpochs);
		System.out.println("Average number of mismatches for normal epochs = " + getAverage(normalMismatchCounts));
		System.out.println("Number of false alarms = " + falseAlarms);
		System.out.println("False Positive Rate = " + getFalsePositiveRate() + "%");
		System.out.println("Number of anomalous epochs = " + anomalousEpochs);
		System.out
		.println("Average number of mismatches for anomalous epochs = " + getAverage(anomalousMismatchCounts));
		System.out.println("Number of true alarms = " + trueAlarms);
		System.out.println("True Positive Rate = " + getTruePositiveRate() + "%");
	}

	public int getTrainingEpochs()
	{
		return trainingEpochs;
	}

	public int getTotalEpochs()
	{
		return trainingEpochs + anomalousEpochs + normalEpochs;
	}

	public void incrementTrainingEpochs()
	{
		trainingEpochs ++;
	}
}
