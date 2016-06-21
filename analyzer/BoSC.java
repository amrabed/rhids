import java.util.ArrayList;
import java.util.Collections;

/**
 * Bag of System Calls
 *
 * @author AmrAbed
 *
 */
public class BoSC extends ArrayList<Byte>
{
	private static final long serialVersionUID = 1L;

	public BoSC(String string)
	{
		string = string.replace("[", "");
		string = string.replace("]", "");

		for (String value : string.split(","))
		{
			add(Byte.parseByte(value.trim()));
		}
	}
}
