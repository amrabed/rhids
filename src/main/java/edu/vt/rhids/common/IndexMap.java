package edu.vt.rhids.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

import edu.vt.rhids.input.SyscallParser;

/**
 * Lookup table for system call index
 *
 * @author AmrAbed
 *
 */
public class IndexMap extends HashMap<String, Integer>
{
	private static final long serialVersionUID = 1L;

	private IndexMap()
	{
	}

	/**
	 * Build index map from count file
	 *
	 * @param reader
	 */
	public IndexMap(BufferedReader reader)
	{
		try
		{
			String line;
			while ((line = reader.readLine()) != null)
			{
				String[] words = line.split("\t");
//				if (Integer.parseInt(words[1]) < size())
//				{
//					break;
//				}
				put(words[0], size());
			}
			reader.close();
		}
		catch (IOException e)
		{
			System.err.println(e);
			System.exit(-2);
		}
	}

	/**
	 * Build index map from first epoch
	 *
	 * @param reader
	 * @param epochSize
	 * @throws IOException
	 */
	public IndexMap(BufferedReader reader, int epochSize) throws IOException
	{
		IndexMap temp = new IndexMap();
		for (int i = 0; i < epochSize; i++)
		{
			String syscall;
			if ((syscall = SyscallParser.parse(reader)) != null)
			{
				if (temp.containsKey(syscall))
				{
					temp.replace(syscall, temp.get(syscall) + 1);
				}
				else
				{
					temp.put(syscall, 1);
				}
			}
		}

		for (String syscall : temp.keySet())
		{
			if (temp.get(syscall) > 1)
			{
				put(syscall, size());
			}
		}
	}

	public int get(String syscall)
	{
		if (containsKey(syscall))
		{
			return super.get(syscall);
		}
		else
		{
			return size();
		}
	}

	public String toString()
	{
		String output = new String();
		for (String entry : keySet())
		{
			output += entry + " => " + get(entry);
		}
		return output;
	}
}
