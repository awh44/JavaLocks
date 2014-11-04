public class Main
{
	public static int THREADS = 10;

	public static void main(String[] args)
	{
		LockB lock = new LockB(THREADS);
		Thread test_threads[] = new Thread[THREADS];
		for (int i = 0; i < THREADS; i++)
		{
			test_threads[i] = new Thread(new Test(i, lock));
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
