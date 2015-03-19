package edu.vt.rhids.common;

import java.util.ArrayList;
import java.util.HashMap;

import edu.vt.rhids.input.BoSC;

/**
 * Normal-behavior Database
 * 
 * @author AmrAbed
 *
 */
public class Database extends HashMap<BoSC, Long>
{
	private static final long serialVersionUID = 1L;

	public void add(BoSC bosc)
	{
		if (bosc == null)
		{
			return;
		}

		if (containsKey(bosc))
		{
			replace(bosc, get(bosc) + 1);
		}
		else
		{
			put(bosc, 1L);
		}
	}

	public void commit(Database change)
	{
		for (BoSC bosc : change.keySet())
		{
			long value = change.get(bosc);
			if (this.containsKey(bosc))
			{
				value += this.get(bosc);
			}
			this.put(bosc, value);
		}
	}

	private double getNorm()
	{
		double norm = 0;
		for (BoSC bosc : keySet())
		{
			long val = get(bosc);
			norm += val * val;
		}
		return Math.sqrt(norm);
	}

	public double calculateSimilarity(Database other)
	{
		if (this.isEmpty() || other.isEmpty())
		{
			return 0;
		}

		double dot = 0, norm1 = this.getNorm(), norm2 = other.getNorm();
		for (BoSC bosc : keySet())
		{
			if (other.containsKey(bosc))
			{
				long value1 = other.get(bosc);
				long value2 = this.get(bosc);
				dot += value1 * value2;
			}
		}
		return dot / (norm1 * norm2);
	}

	public String toString()
	{
		String output = new String();
		for (ArrayList<Byte> entry : keySet())
		{
			output += entry + " => " + get(entry);
		}
		return output;
	}
}
