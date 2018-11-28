package edu.vt.rhids.common;

import java.util.LinkedList;

public class SimilarityVector extends LinkedList<Double> {
	private static final long serialVersionUID = 1L;

	public void update(double similarity) {
		if (size() == 3) {
			removeFirst();
		}
		addLast(similarity);
	}
}
