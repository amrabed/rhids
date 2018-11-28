package analyzer.compare;

import java.util.logging.Logger;
import java.util.logging.Level;

import analyzer.Database;

public class Comparator {
	public static void main(String[] args) {
		try {
			final Database db1 = new Database(args[0]);
			final Database db2 = new Database(args[1]);
			final double similarity = db1.calculateSimilarity(db2);
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.INFO, () -> "Similaity: " + similarity);

		} catch (Exception e) {
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, e.toString());
		}
	}
}