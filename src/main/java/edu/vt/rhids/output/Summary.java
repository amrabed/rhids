package edu.vt.rhids.output;

import java.util.ArrayList;

/**
 * Summarizes results from the whole run
 * 
 * @author AmrAbed
 *
 */
public class Summary extends ArrayList<Statistics>
{
	private static final long serialVersionUID = 1L;

	/**
	 * @return MATLAB cell-array definition statement (for data analysis and plotting)
	 */
	@Override
	public String toString() {
		final StringBuilder output = new StringBuilder("{");
		for (Statistics result : this) {
			output.append(result);
			output.append(';');
		}
		output.append("};");
		return output.toString();
	}

}
