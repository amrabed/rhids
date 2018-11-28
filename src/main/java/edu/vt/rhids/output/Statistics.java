package edu.vt.rhids.output;

import edu.vt.rhids.util.Logger;

import java.util.ArrayList;

/**
 * Statistics collected from running the test for certain epoch size, training
 * threshold, and testing threshold
 *
 * @author AmrAbed
 */
public class Statistics {
	private final int epochSize;
	private final int testThreshold;
	private final float trainThreshold;

	private final ArrayList<Integer> normalMismatchCounts = new ArrayList<>();
	private final ArrayList<Integer> anomalousMismatchCounts = new ArrayList<>();

	private int trainingEpochs;
	private int normalEpochs;
	private int anomalousEpochs;
	private int falseAlarms;
	private int trueAlarms;

	public Statistics(int epochSize, float trainThreshold, int testThreshold) {
		this.epochSize = epochSize;
		this.trainThreshold = trainThreshold;
		this.testThreshold = testThreshold;
	}

	public void incrementEpochs(boolean isAnomalous) {
		if (isAnomalous) {
			anomalousEpochs++;
		} else {
			normalEpochs++;
		}
	}

	public void incrementAlarms(boolean isAnomalous) {
		if (isAnomalous) {
			trueAlarms++;
		} else {
			falseAlarms++;
		}
	}

	public void addMismatchCount(int nMismatches, boolean isAnomalous) {
		if (isAnomalous) {
			anomalousMismatchCounts.add(nMismatches);
		} else {
			normalMismatchCounts.add(nMismatches);
		}

	}

	public int getTestThreshold() {
		return testThreshold;
	}

	public int getEpochSize() {
		return epochSize;
	}

	public float getTrainThreshold() {
		return trainThreshold;
	}

	private double getFalsePositiveRate() {
		return 100 * (double) falseAlarms / normalEpochs;
	}

	private double getTruePositiveRate() {
		return 100 * (double) trueAlarms / anomalousEpochs;
	}

	public String toString() {
		return String.valueOf(epochSize) + ',' +
				trainThreshold + ',' +
				testThreshold + ',' +
				trainingEpochs + ',' +
				normalEpochs + ',' +
				anomalousEpochs + ',' +
				getAverage(normalMismatchCounts) + ',' +
				getAverage(anomalousMismatchCounts) + ',' +
				getFalsePositiveRate() + ',' +
				getTruePositiveRate();
	}

	private double getAverage(ArrayList<Integer> array) {
		double avg = 0;
		for (int entry : array) {
			avg += entry;
		}
		return avg / array.size();
	}

	public void print() {
		Logger.log("\nEpoch Size = " + epochSize, Logger.Verbosity.LOW);
		Logger.log("Training Threshold = " + trainThreshold, Logger.Verbosity.LOW);
		Logger.log("Test Threshold = " + testThreshold, Logger.Verbosity.LOW);
		Logger.log("Number of training epochs = " + trainingEpochs, Logger.Verbosity.LOW);
		Logger.log("Number of normal epochs = " + normalEpochs, Logger.Verbosity.LOW);
		Logger.log("Average number of mismatches for normal epochs = " + getAverage(normalMismatchCounts), Logger.Verbosity.LOW);
		Logger.log("Number of false alarms = " + falseAlarms, Logger.Verbosity.LOW);
		Logger.log("False Positive Rate = " + getFalsePositiveRate() + "%", Logger.Verbosity.LOW);
		Logger.log("Number of anomalous epochs = " + anomalousEpochs, Logger.Verbosity.LOW);
		Logger.log("Average number of mismatches for anomalous epochs = " + getAverage(anomalousMismatchCounts), Logger.Verbosity.LOW);
		Logger.log("Number of true alarms = " + trueAlarms, Logger.Verbosity.LOW);
		Logger.log("True Positive Rate = " + getTruePositiveRate() + "%", Logger.Verbosity.LOW);
	}

	public int getTrainingEpochs() {
		return trainingEpochs;
	}

	public int getTotalEpochs() {
		return trainingEpochs + anomalousEpochs + normalEpochs;
	}

	public void incrementTrainingEpochs() {
		trainingEpochs++;
	}
}
