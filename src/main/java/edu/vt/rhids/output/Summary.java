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

	public String toString()
	{
		// Returns MATLAB cell-array definition statement 
		// (for data analysis and plotting)
		String output = "{";
		for (Statistics result : this)
		{
			output += result + ";";
		}
		output += "};";
		return output;
	}

}
