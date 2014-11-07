public class TestMain
{
	public static int THREADS = 10;

	public static void main(String[] args)
	{
		if (args.length < 1)
		{
			System.out.println("Please specify the type of lock as a command line argument.");
			return;
		}

		Lock lock;
		if (args[0].equals("B"))
		{
			Test.set_lock(new LockB(THREADS));
		}
		else if (args[0].equals("C"))
		{
			Test.set_lock(new LockC());
		}
		else
		{
			System.out.println("That lock type does not exist.");
			return;
		}

		Thread test_threads[] = new Thread[THREADS];
		for (int i = 0; i < THREADS; i++)
		{
			test_threads[i] = new Thread(new Test(i));
			test_threads[i].start();
		}

		for (int i = 0; i < THREADS; i++)
		{
			try
			{
				test_threads[i].join();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
