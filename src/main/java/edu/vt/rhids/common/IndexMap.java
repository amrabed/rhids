package edu.vt.rhids.common;

import edu.vt.rhids.input.SyscallParser;
import edu.vt.rhids.util.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * Lookup table for system call index
 *
 * @author AmrAbed
 */
public class IndexMap extends HashMap<String, Integer> {
	private static final long serialVersionUID = 1L;

	private IndexMap() {
	}

	/**
	 * Build index map from count file
	 *
	 * @param reader BufferedReader of file to read from
	 */
	public IndexMap(BufferedReader reader) {
		try {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] words = line.split("\t");
				put(words[0], size());
			}
			reader.close();
		} catch (IOException e) {
			Logger.log(e.getMessage(), Logger.Verbosity.NONE);
			System.exit(-2);
		}
	}

	/**
	 * Build index map from first epoch
	 *
	 * @param reader BufferedReader of file to read from
	 * @param epochSize Epoch size
	 * @throws IOException if reading from file fails
	 */
	@SuppressWarnings("unused")
	public IndexMap(BufferedReader reader, int epochSize) throws IOException {
		IndexMap temp = new IndexMap();
		for (int i = 0; i < epochSize; i++) {
			String syscall;
			if ((syscall = SyscallParser.parse(reader)) != null) {
				if (temp.containsKey(syscall)) {
					temp.replace(syscall, temp.get(syscall) + 1);
				} else {
					temp.put(syscall, 1);
				}
			}
		}

		for (String syscall : temp.keySet()) {
			if (temp.get(syscall) > 1) {
				put(syscall, size());
			}
		}
	}

	private int get(String syscall) {
		if (containsKey(syscall)) {
			return super.get(syscall);
		} else {
			return size();
		}
	}

	@Override
	public String toString() {
		final StringBuilder output = new StringBuilder();
		for (Entry<String, Integer> entry : entrySet()) {
			output.append(entry.getKey());
			output.append(" => ");
			output.append(entry.getValue());
			output.append('\n');
		}
		return output.toString();
	}
}
