package edu.vt.rhids.common;

import java.util.LinkedList;

public class SimilarityVector extends LinkedList<Double>
{
	private static final long serialVersionUID = 1L;
	private static final int SIZE = 3;

	public void update(double similarity)
	{
		if (size() == SIZE)
		{
			removeFirst();
		}
		addLast(similarity);
	}

	public boolean isAbove(float threshold)
	{
		boolean isAboveThreshold = (size() == SIZE);
		for (double s : this)
		{
			isAboveThreshold &= (s >= threshold);
		}
		return isAboveThreshold;
	}
}
