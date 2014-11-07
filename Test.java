public class Test implements Runnable
{
	private int id_;
	private static Lock lock_;
	private static int n_ = 0;
	private static boolean count_test_ = false;

	public Test(int id)
	{
		id_ = id;
	}

	public static void set_lock(Lock lock)
	{
		lock_ = lock;
	}

	public static void set_as_count_test()
	{
		count_test_ = true;
	}

	public static void print_n()
	{
		System.out.println(n_);
	}

	@Override
	public void run()
	{
		if (count_test_)
		{
			count_stuff();
		}
		else
		{
			print_stuff();
		}
	}

	private void count_stuff()
	{
		for (int i = 0; i < 100000; i++)
		{
			lock_.lock(id_);
			n_++;
			lock_.unlock(id_);
		}
	}

	private void print_stuff()
	{
		while (true)
		{
			lock_.lock(id_);
			System.out.println("Thread " + id_ + " in the critical section.");
			lock_.unlock(id_);
		}		
	}
}
