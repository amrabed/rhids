package analyzer.aggregate;

import java.util.logging.Logger;
import java.util.logging.Level;

import analyzer.Database;

public class Aggregator {
	public static void main(String[] args) {
		try {
			final Database db1 = new Database(args[0]);
			final Database db2 = new Database(args[1]);

			db1.commit(db2);
			db1.dump(args[2]);

		} catch (Exception e) {
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, e.toString());
		}
	}
}
