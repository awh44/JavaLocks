public class Test implements Runnable
{
	private int id_;
	private static Lock lock_;
	public Test(int id)//, Lock lock)
	{
		id_ = id;
		//lock_ = lock;
	}

	public static void set_lock(Lock lock)
	{
		lock_ = lock;
	}

	@Override
	public void run()
	{
		while (true)
		{
			lock_.lock(id_);
			System.out.println("Thread " + id_ + " in the critical section.");
			lock_.unlock(id_);
		}
	}
}
