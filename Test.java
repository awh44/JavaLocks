public class Test implements Runnable
{
	int id_;
	Lock lock_;
	public Test(int id, Lock lock)
	{
		id_ = id;
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
