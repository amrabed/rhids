package edu.vt.rhids.input;

import edu.vt.rhids.Main;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Bag of System Calls
 *
 * @author AmrAbed
 */
public class BoSC extends ArrayList<Byte> {
	private static final long serialVersionUID = 1L;

	BoSC(Window window) {
		super(Collections.nCopies(Main.rhids.getIndexMap().size() + 1, (byte) 0));
		for (String syscall : window) {
			int index = Main.rhids.getIndexMap().get(syscall);
			Byte count = get(index);
			set(index, ++count);
		}
	}

	public BoSC(String string) {
		string = string.replace("[", "");
		string = string.replace("]", "");

		for (String value : string.split(",")) {
			add(Byte.parseByte(value.trim()));
		}
	}
}