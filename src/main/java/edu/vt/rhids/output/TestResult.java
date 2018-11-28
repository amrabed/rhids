package edu.vt.rhids.output;

import java.util.List;

public class TestResult {
	private final int nEpochs;
	private final double nAnomalySignals;
	private final List<Double> mismatchCounts;

	public TestResult(int nEpochs, double nAnomalySignals, List<Double> mismatchCounts) {
		this.nEpochs = nEpochs;
		this.nAnomalySignals = nAnomalySignals;
		this.mismatchCounts = mismatchCounts;
	}

	private double getAvgMismatches() {
		double avgMismatchCount = 0;
		for (double mismatchCount : mismatchCounts) {
			avgMismatchCount += mismatchCount;
		}
		return avgMismatchCount / mismatchCounts.size();
	}

	@Override
	public String toString() {
		return "\nTotal Number of epochs: " + nEpochs +
				"\nNumber of anomaly signals: " + nAnomalySignals +
				"\nAverage number of mismatches per epoch: " + getAvgMismatches();
	}
}