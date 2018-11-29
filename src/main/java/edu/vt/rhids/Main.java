package edu.vt.rhids;

import edu.vt.rhids.common.Database;
import edu.vt.rhids.main.Parameters;
import edu.vt.rhids.main.RHIDS;
import edu.vt.rhids.output.Summary;
import edu.vt.rhids.util.Logger;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Help.Visibility;
import picocli.CommandLine.Option;

import java.util.concurrent.Callable;

@Command(name = "rhids", version = "0.2",
		description = "Resilient Host-based Intrusion Detection System for Docker Containers",
		subcommands = {
				CommandLine.HelpCommand.class,
				Main.Classifier.class,
				Main.Aggregator.class,
				Main.Comparator.class
		})
@SuppressWarnings("unused")
public class Main implements Runnable {

	@Option(names = {"-V", "--version"}, description = "Print version information and exit", versionHelp = true)
	boolean isVersionRequested;

	@Option(names = {"-h", "--help"}, description = "Print this help message and exit", usageHelp = true)
	boolean isHelpRequested;

	public static void main(String[] args) {
		new CommandLine(new Main()).parseWithHandler(new CommandLine.RunFirst(), args);
	}

	@Override
	public void run() {
		CommandLine.usage(this, System.out);
	}

	@Command(name = "classify", description = "Train & test classifier using an input trace file",
			sortOptions = false, mixinStandardHelpOptions = true)
	public static class Classifier implements Callable<Summary> {
		@Option(names = {"-i", "--input-file"}, paramLabel = "<input file>",
				description = "Input file path", required = true)
		String inputFile;
		@Option(names = {"-b", "--database-file"}, paramLabel = "<database file>",
				description = "File to read database from")
		String databaseFile;
		@Option(names = {"-o", "--output-file"}, description = "Output file path", paramLabel = "<input file>", required = true)
		String outputFile;
		@Option(names = {"-e", "--epoch-size"}, description = "Range for epoch size",
				paramLabel = "<start[[:step]:end]>", converter = Parameters.IntegerRange.class,
				defaultValue = "2500:2500:50000", showDefaultValue = Visibility.ALWAYS)
		Parameters.IntegerRange epochSizeRange;
		@Option(names = {"-t", "--train-threshold"}, description = "Range for training threshold",
				paramLabel = "<start[[:step]:end]>", converter = Parameters.FloatRange.class,
				defaultValue = "0.9f:0.001f:1f", showDefaultValue = Visibility.ALWAYS)
		Parameters.FloatRange trainThresholdRange;
		@Option(names = {"-d", "--detection-threshold"}, description = "Range for detection threshold",
				paramLabel = "<start[[:step]:end]>", converter = Parameters.IntegerRange.class,
				defaultValue = "5:5:200", showDefaultValue = Visibility.ALWAYS)
		Parameters.IntegerRange testThresholdRange;
		@Option(names = {"-v", "--verbosity"}, description = "Verbose level",
				defaultValue = "0", showDefaultValue = Visibility.ALWAYS)
		int verbosity;

		@Override
		public Summary call() throws Exception {
			Logger.setHandler(outputFile);
			Logger.setLevel(verbosity);
			final Parameters parameters = new Parameters(inputFile, databaseFile, epochSizeRange, trainThresholdRange, testThresholdRange);
			Summary summary = RHIDS.getInstance().run(parameters);
			Logger.log(summary, Logger.Verbosity.LOW);
			return summary;
		}
	}

	@Command(name = "aggregate", description = "Aggregate two database files into one",
			mixinStandardHelpOptions = true)
	public static class Aggregator implements Callable<Void> {
		@CommandLine.Parameters(index = "0", description = "dump file for first database")
		String file1;
		@CommandLine.Parameters(index = "1", description = "dump file for second database")
		String file2;
		@CommandLine.Parameters(index = "2", description = "output dump file of aggregate database")
		String output;

		@Override
		public Void call() throws Exception {
			final Database db1 = new Database(file1);
			db1.commit(new Database(file2));
			db1.dump(output);
			return null;
		}
	}

	@Command(name = "compare", description = "Calculate similarity matrix between two database files", mixinStandardHelpOptions = true)
	public static class Comparator implements Callable<Double> {
		@CommandLine.Parameters(description = "dump file for first database")
		String file1;
		@CommandLine.Parameters(description = "dump file for second database")
		String file2;

		@Override
		public Double call() throws Exception {
			final Database db1 = new Database(file1);
			final Database db2 = new Database(file2);
			final double similarity = db1.calculateSimilarity(db2);
			Logger.log("Similarity: " + similarity, Logger.Verbosity.LOW);
			return similarity;
		}
	}
}