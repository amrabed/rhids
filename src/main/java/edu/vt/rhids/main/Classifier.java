package edu.vt.rhids.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import edu.vt.rhids.common.Database;
import edu.vt.rhids.common.SimilarityVector;
import edu.vt.rhids.input.BoSC;
import edu.vt.rhids.input.SyscallParser;
import edu.vt.rhids.input.Window;
import edu.vt.rhids.output.Statistics;
import edu.vt.rhids.output.TestResult;
import edu.vt.rhids.util.Logger;
import edu.vt.rhids.util.Logger.Verbosity;

/**
 * "Real-time" Classifier
 * 
 * @author AmrAbed
 *
 */
public class Classifier
{
	private final BufferedReader reader;
	private final Statistics stats;
	private final Database db;

	public Classifier(BufferedReader reader, Statistics stats, String databaseFile) throws IOException
	{
		this.reader = reader;
		this.stats = stats;

		db = new Database(databaseFile);
	}

	public boolean trainUnconditionally() throws IOException
	{
		final Window window = new Window();
		BoSC bosc;
		String syscall;

		while (!RHIDS.isDoneTraining())
		{
			for (int i = 0; i < stats.getEpochSize(); i++)
			{
				if ((syscall = SyscallParser.parse(reader)) != null)
				{
					bosc = window.slide(syscall).getBoSC();
					db.add(bosc);
					Logger.log(syscall + " => " + window + " => " + bosc, Verbosity.HIGH);
				}
				else
				{
					// Failed to train using input file
					Logger.signal("Training failed", Verbosity.LOW);
					db.dump("/var/log/db.dump");
					return false;
				}
			}
			Logger.log("Epoch " + stats.getTotalEpochs() + ": Database size is " + db.size(), Verbosity.MEDIUM);
			stats.incrementTrainingEpochs();
		}
		db.dump("/var/log/db.dump");
		return true;

		// return train();
	}

	public boolean train() throws IOException
	{
		final SimilarityVector similarity = new SimilarityVector();
		final Window window = new Window();
		Database lastEpochChange = new Database(null);
		String syscall;

		// while (!similarity.isAbove(stats.getTrainThreshold()))
		while (!RHIDS.isDoneTraining())
		{
			Logger.log("\nEpoch " + stats.getTotalEpochs(), Verbosity.MEDIUM);
			final Database currentEpochChange = new Database(null);
			for (int i = 0; i < stats.getEpochSize(); i++)
			{
				if ((syscall = SyscallParser.parse(reader)) != null)
				{
					BoSC bosc = window.slide(syscall).getBoSC();
					currentEpochChange.add(bosc);
					Logger.log(syscall + " => " + window + " => " + bosc, Verbosity.HIGH);
				}
				else
				{
					// Failed to train using input file
					Logger.signal("Training failed", Verbosity.LOW);
					db.dump("/var/log/db.dump");
					return false;
				}
			}
			stats.incrementTrainingEpochs();
			Logger.log("Database change size: " + currentEpochChange.size(), Verbosity.MEDIUM);
			db.commit(currentEpochChange);
			Logger.log("Updated Database size: " + db.size(), Verbosity.MEDIUM);
			similarity.update(lastEpochChange.calculateSimilarity(currentEpochChange));
			Logger.log("Similarity vector: " + similarity, Verbosity.MEDIUM);
			lastEpochChange = currentEpochChange;
		}

		Logger.log(db, Verbosity.ALL);
		Logger.emphasize("\nStable Database size: " + db.size(), Verbosity.LOW);
		Logger.emphasize("\n" + stats.getTrainingEpochs() * stats.getEpochSize() + " system calls used for training\n",
				Verbosity.LOW);
		db.dump("/var/log/db.dump");
		return true;
	}

	public void test() throws IOException
	{
		final Window window = new Window();
		String syscall;
		while (true)
		{
			final Database currentEpochChange = new Database(null);

			boolean isAnomalousEpoch = false;
			int nMismatches = 0;

			for (int i = 0; i < stats.getEpochSize(); i++)
			{
				if ((syscall = SyscallParser.parse(reader)) != null)
				{
					if (RHIDS.isUnderAttack())
					{
						isAnomalousEpoch = true;
					}
					final BoSC bosc = window.slide(syscall).getBoSC();

					currentEpochChange.add(bosc);
					if (!db.containsKey(bosc))
					{
						nMismatches++;
						Logger.signal(syscall + " => " + window + " => " + bosc + " => mismatch", Verbosity.HIGH);
					}
					else
					{
						Logger.log(syscall + " => " + window + " => " + bosc + " => match", Verbosity.HIGH);
					}
				}
				else
				{
					// Incomplete epoch .. ignore (for now)
					return;
				}
			}

			Logger.log("Epoch " + stats.getTotalEpochs() + ": " + nMismatches + " mismatches", Verbosity.MEDIUM);
			if (isAnomalousEpoch)
			{
				Logger.signal("ANOMALOUS EPOCH", Verbosity.MEDIUM);
			}

			if (nMismatches > stats.getTestThreshold())
			{
				Logger.signal("Anomaly signal raised", Verbosity.MEDIUM);
				stats.incrementAlarms(isAnomalousEpoch);
			}
			else if (nMismatches > 0)
			{
				db.commit(currentEpochChange);
				Logger.emphasize("Current Database size: " + db.size(), Verbosity.MEDIUM);
			}
			stats.incrementEpochs(isAnomalousEpoch);
			stats.addMismatchCount(nMismatches, isAnomalousEpoch);
		}
	}

	/**
	 * Test classifier in epochs
	 * 
	 * @param reader
	 * @param epochSize
	 * @param testThreshold
	 * @return test results
	 * @throws IOException
	 */
	@Deprecated
	public TestResult test(BufferedReader reader, double testThreshold, int epochSize) throws IOException
	{
		final Window window = new Window();

		String syscall;

		final ArrayList<Double> mismatchCounts = new ArrayList<>();

		int nEpochs = 0;
		int nSyscalls = 0;
		double nAnomalySignals = 0;
		double nMismatches = 0;

		while ((syscall = SyscallParser.parse(reader)) != null)
		{
			final BoSC bosc = window.slide(syscall).getBoSC();

			if (!db.containsKey(bosc))
			{
				nMismatches++;
			}

			nSyscalls++;
			if (nSyscalls >= epochSize)
			{

				Logger.log("\nTotal mismatches for epoch " + nEpochs + " is " + nMismatches, Verbosity.MEDIUM);

				nEpochs++;
				// Reached end of epoch
				mismatchCounts.add(nMismatches);
				if (nMismatches > testThreshold * epochSize)
				{
					nAnomalySignals++;
				}
				nMismatches = 0;
				nSyscalls = 0;
			}
		}
		return new TestResult(nEpochs, nAnomalySignals, mismatchCounts);
	}

	/**
	 * Test classifier in sequences
	 * 
	 * @param reader
	 * @return percentage of mismatches
	 * @throws IOException
	 */
	@Deprecated
	public double test(BufferedReader reader) throws IOException
	{
		final Window window = new Window();

		double mismatchCount = 0;
		int totalSequences = 0;

		String syscall;
		while ((syscall = SyscallParser.parse(reader)) != null)
		{
			totalSequences++;

			if (!db.containsKey(window.slide(syscall).getBoSC()))
			{
				mismatchCount++;
			}
		}

		Logger.log("Total number of seqences: " + totalSequences, Verbosity.MEDIUM);
		Logger.log("Number of mismatches: " + mismatchCount, Verbosity.MEDIUM);

		return 100 * mismatchCount / totalSequences;
	}
}
