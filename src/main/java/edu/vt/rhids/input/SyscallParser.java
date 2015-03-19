package edu.vt.rhids.input;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.vt.rhids.main.RHIDS;

/**
 * Read system calls from input file
 * 
 * @author AmrAbed
 *
 */
public class SyscallParser
{
	public static String parse(BufferedReader reader) throws IOException
	{
		try
		{		
			final String line = reader. readLine();
			
			Matcher matcher = Pattern.compile("STOP TRAINING").matcher(line);
			if (matcher.find())
			{
				RHIDS.setDoneTraining(true);
				return parse(reader);
			}

			matcher = Pattern.compile("START ATTACK").matcher(line);
			if (matcher.find())
			{
				RHIDS.setUnderAttack(true);
				return parse(reader);
			}
			
			matcher = Pattern.compile("END ATTACK").matcher(line);
			if (matcher.find())
			{
				RHIDS.setUnderAttack(false);
				return parse(reader);
			}
						
			matcher = Pattern.compile("([a-z_]+)\\(").matcher(line);
			if (matcher.find())
			{
				return matcher.group(1);
			}
			else
			{
				// No new system call found .. skip to next line
				return parse(reader);
			}
		}
		catch (NullPointerException e)
		{
			// End of file
			return null;
		}
	}
}
