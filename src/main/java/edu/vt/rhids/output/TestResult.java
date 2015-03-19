package edu.vt.rhids.output;


import java.util.ArrayList;

public class TestResult
{
	public final int nEpochs;
	public final double nAnomalySignals;
	public final ArrayList<Double> mismatchCounts;

	public TestResult(int nEpochs, double nAnomalySignals, ArrayList<Double> mismatchCounts)
	{
		this.nEpochs = nEpochs;
		this.nAnomalySignals = nAnomalySignals;
		this.mismatchCounts = mismatchCounts;
	}

	public double getAvgMismatches()
	{
		double avgMismatchCount = 0;
		for (double mismatchCount : mismatchCounts)
		{
			avgMismatchCount += mismatchCount;
		}
		return avgMismatchCount / mismatchCounts.size();
	}

	public String toString()
	{
		String output = new String();
		output += "\nTotal Number of epochs: " + nEpochs;
		output += "\nNumber of anomaly signals: " + nAnomalySignals;
		output += "\nAverage number of mismatches per epoch: " + getAvgMismatches();
		return output;
	}
}
