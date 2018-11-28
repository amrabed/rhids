package edu.vt.rhids;

import edu.vt.rhids.main.RHIDS;
import edu.vt.rhids.util.Logger;

public class Main {

	public static final RHIDS rhids = RHIDS.getInstance();

	public static void main(String[] args) {
		Logger.log(rhids.run(args), Logger.Verbosity.NONE);
	}
}