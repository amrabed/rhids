package edu.vt.rhids.input;
import java.util.LinkedList;

/**
 * Sliding Window
 * 
 * @author AmrAbed
 *
 */
public class Window extends LinkedList<String>
{
	private static final long serialVersionUID = 1L;
	private static final int SEQUENCE_SIZE = 10;

	public Window slide(String syscall)
	{
		if (size() == SEQUENCE_SIZE)
		{
			removeFirst();
		}
		addLast(syscall);
		return this;
	}

	public BoSC getBoSC()
	{
		return (size() < SEQUENCE_SIZE) ? null : new BoSC(this);
	}
}
