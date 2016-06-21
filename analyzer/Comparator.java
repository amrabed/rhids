public class Comparator
{
        public static void main(String [] args)
        {
		try
		{
                final Database db1 = new Database(args[0]);
		final Database db2 = new Database(args[1]);

                double similarity = db1.calculateSimilarity(db2);

		System.out.println("Similaity: " + similarity);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			//System.out.println("ERROR: " + e);
		}
        }
}
