package edu.vt.rhids.main;

import edu.vt.rhids.common.IndexMap;
import edu.vt.rhids.output.Statistics;
import edu.vt.rhids.output.Summary;
import edu.vt.rhids.util.Logger;
import edu.vt.rhids.util.Logger.Verbosity;
import org.apache.commons.cli.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Resilient Host-based Intrusion Detection System
 *
 * @author AmrAbed
 */
public class RHIDS {
	private static final String OUTPUT_FILE = "output-file";
	private static final String DB_FILE = "database-file";
	private static final String VERBOSE = "verbose";
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

	public List<Statistics> run(String[] args) {
		final Parameters p = parseArguments(args);
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

	private Parameters parseArguments(String[] args) {
		final Parameters parameters = new Parameters();
		final Options options = createOptions();

		try {
			CommandLine command = new BasicParser().parse(options, args);

			if (command.hasOption("help")) {
				new HelpFormatter().printHelp("RHIDS", options);
				System.exit(0);
			}

			if (command.hasOption(VERBOSE)) {
				Logger.setLevel(Integer.parseInt(command.getOptionValue(VERBOSE)));
			}

			if (command.hasOption(OUTPUT_FILE)) {
				Logger.setHandler(command.getOptionValue(OUTPUT_FILE));
			}

			if (command.hasOption(DB_FILE)) {
				parameters.setDatabaseFilePath(command.getOptionValue(DB_FILE));
			}

			parameters.setNormalFilePath(command.getOptionValue("input-file"));
			parameters.setEpochSize(command.getOptionValue("epoch-size"));
			parameters.setTrainThreshold(command.getOptionValue("train-threshold"));
			parameters.setTestThreshold(command.getOptionValue("detection-threshold"));
		} catch (ParseException | FileNotFoundException e) {
			Logger.log("Parsing failed: " + e.getMessage(), Verbosity.NONE);
			System.exit(-1);
		}

		return parameters;
	}

	private Options createOptions() {

		final Options options = new Options();

		final OptionGroup group = new OptionGroup();
		group.addOption(new Option("h", "help", false, "Print this help message"));
		group.addOption(new Option("i", "input-file", true, "Input file path"));
		group.setRequired(true);

		options.addOptionGroup(group);

		options.addOption("b", DB_FILE, true, "File to read database from");
		options.addOption("e", "epoch-size", true, "Range for epoch size (default " + Parameters.DEFAULT_EPOCH_SIZE
				+ ")");
		options.addOption("t", "train-threshold", true, "Range for training threshold (default "
				+ Parameters.DEFAULT_TRAIN_THRESHOLD + ")");
		options.addOption("d", "detection-threshold", true, "Range for detection threshold (default "
				+ Parameters.DEFAULT_TEST_THRESHOLD + ")");
		options.addOption("v", VERBOSE, true, "Verbose level (default 0)");
		options.addOption("o", OUTPUT_FILE, true, "Output file path");

		return options;
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
