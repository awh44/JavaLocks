public class TestMain
{
	public static int THREADS = 10;

	public static void main(String[] args)
	{
		if (args.length < 2)
		{
			System.out.println("Please specify the type of lock (B or C) and test (count or print) as command line arguments.");
			return;
		}

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
			System.out.println("That is not a defined lock type.");
			return;
		}

		if (args[1].equals("count"))
		{
			Test.set_as_count_test();
		}
		else if (!args[1].equals("print"))
		{
			System.out.println("That is not a defined test type.");
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

		Test.print_n();
	}
}
