package edu.vt.rhids.input;

import edu.vt.rhids.main.RHIDS;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class to read system calls from input file
 *
 * @author AmrAbed
 */
public class SyscallParser {
	private SyscallParser() {
	}

	public static String parse(BufferedReader reader) throws IOException {
		try {
			final String line = reader.readLine();

			Matcher matcher = Pattern.compile("STOP TRAINING").matcher(line);
			if (matcher.find()) {
				RHIDS.getInstance().setDoneTraining(true);
				return parse(reader);
			}

			matcher = Pattern.compile("START ATTACK").matcher(line);
			if (matcher.find()) {
				RHIDS.getInstance().setUnderAttack(true);
				return parse(reader);
			}

			matcher = Pattern.compile("END ATTACK").matcher(line);
			if (matcher.find()) {
				RHIDS.getInstance().setUnderAttack(false);
				return parse(reader);
			}

			matcher = Pattern.compile("([a-z_]+)").matcher(line);
			if (matcher.find()) {
				return matcher.group(1);
			} else {
				// No new system call found .. skip to next line
				return parse(reader);
			}
		} catch (NullPointerException e) {
			// End of file
			return null;
		}
	}
}