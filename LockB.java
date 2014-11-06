public class LockB implements Lock
{
	private volatile boolean choosing_[];
	private volatile int number_[];
	
	public LockB(int num_threads)
	{
		choosing_ = new boolean[num_threads];
		number_ = new int[num_threads];
	}

	private int max()
	{
		int max = number_[0];
		for (int i = 1; i < number_.length; i++)
		{	
			if (number_[i] > max)
			{
				max = number_[i];
			}
		}
		return max;
	}

	@Override
	public void lock(int thread_id)
	{
		choosing_[thread_id] = true;
		choosing_ = choosing_; //force the other threads to see that choosing_[thread_id] has updated
		number_[thread_id] = max() + 1;
		choosing_[thread_id] = false;
		choosing_ = choosing_; //force the other threads to see that choosing_[thread_id] and number_[thread_id] have updated
		
		for (int i = 0; i < number_.length; i++)
		{
			if (i == thread_id)
			{
				continue;
			}
			while (choosing_[i]) {};
			while (!((number_[i] == 0) || ((number_[thread_id] < number_[i]) || ((number_[thread_id] == number_[i]) && (thread_id < i))))) {};
		}
	}

	@Override
	public void unlock(int thread_id)
	{
		number_[thread_id] = 0;
		number_ = number_; //force the other threads to see that number_[thread_id] has updated
	}
}
