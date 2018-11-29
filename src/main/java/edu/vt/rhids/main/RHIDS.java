package edu.vt.rhids.main;

import edu.vt.rhids.common.IndexMap;
import edu.vt.rhids.output.Statistics;
import edu.vt.rhids.output.Summary;
import edu.vt.rhids.util.Logger;
import edu.vt.rhids.util.Logger.Verbosity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Resilient Host-based Intrusion Detection System
 *
 * @author AmrAbed
 */
public class RHIDS {
	private static RHIDS instance = null;

	private boolean isUnderAttack;
	private boolean isDoneTraining;
	private IndexMap indexMap;

	private RHIDS() {
	}

	public static RHIDS getInstance() {
		if (instance == null) instance = new RHIDS();
		return instance;
	}

	public Summary run(Parameters p) {
		Logger.log(p, Verbosity.NONE);
		final Summary summary = new Summary();
		try {
			indexMap = new IndexMap(new BufferedReader(new FileReader("syscalls")));
			Logger.log(indexMap, Verbosity.ALL);
			Logger.emphasize("Number of distinct system calls: " + indexMap.size(), Verbosity.LOW);

			for (int epochSize = p.epochSize.min; epochSize <= p.epochSize.max; epochSize += p.epochSize.step) {
				for (float trainThreshold = p.trainThreshold.min; trainThreshold <= p.trainThreshold.max; trainThreshold += p.trainThreshold.step) {
					for (int testThreshold = p.testThreshold.min; testThreshold <= p.testThreshold.max; testThreshold += p.testThreshold.step) {
						isDoneTraining = false;
						isUnderAttack = false;

						Logger.log("\n#######################################################", Verbosity.LOW);
						Logger.log("Epoch size: " + epochSize, Verbosity.LOW);
						Logger.log("Similarity threshold: " + trainThreshold, Verbosity.LOW);
						Logger.log("Test threshold: " + testThreshold, Verbosity.LOW);
						Logger.log("#######################################################", Verbosity.LOW);

						try (BufferedReader reader = new BufferedReader(new FileReader(p.inputFile))) {
							final Statistics stats = new Statistics(epochSize, trainThreshold, testThreshold);
							final Classifier classifier = new Classifier(reader, stats, p.databaseFile);

							if (classifier.trainUnconditionally()) {
								classifier.test();
								stats.print();
								summary.add(stats);
							}
							Logger.log(summary, Verbosity.LOW);
						}
					}
				}
			}
		} catch (IOException e) {
			Logger.log(e.getMessage(), Verbosity.NONE);
			System.exit(-2);
		}
		return summary;
	}

	public IndexMap getIndexMap() {
		return indexMap;
	}

	boolean isUnderAttack() {
		return isUnderAttack;
	}

	public void setUnderAttack(boolean isUnderAttack) {
		this.isUnderAttack = isUnderAttack;
	}

	boolean isTrainingActive() {
		return !isDoneTraining;
	}

	public void setDoneTraining(boolean doneTraining) {
		this.isDoneTraining = doneTraining;
	}
}