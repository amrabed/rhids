package edu.vt.rhids.input;

import java.util.ArrayList;
import java.util.Collections;

import edu.vt.rhids.main.RHIDS;

/**
 * Bag of System Calls
 * 
 * @author AmrAbed
 *
 */
public class BoSC extends ArrayList<Byte>
{
	private static final long serialVersionUID = 1L;
	
	public BoSC(Window window)
	{
		super(Collections.nCopies(RHIDS.indexMap.size() + 1, (byte) 0));
		for(String syscall : window)
		{
			int index = RHIDS.indexMap.get(syscall);
			Byte count = get(index);
			set(index, ++count);
		}
	}
}
