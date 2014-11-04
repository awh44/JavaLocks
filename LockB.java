import java.util.Arrays;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class LockB implements Lock
{
	//use atomic arrays because volatile variables cannot be placed into Java arrays
	AtomicIntegerArray choosing_;
	AtomicIntegerArray number_;
	
	public LockB(int num_threads)
	{
		choosing_ = new AtomicIntegerArray(num_threads);
		number_ = new AtomicIntegerArray(num_threads);
	}

	private int max()
	{
		int max = number_.get(0);
		for (int i = 1; i < number_.length(); i++)
		{
			if (number_.get(i) > max)
			{
				max = number_.get(i);
			}
		}
		return max;
	}

	@Override
	public void lock(int thread_id)
	{
		choosing_.set(thread_id, 1);
		number_.set(thread_id, max() + 1);
		choosing_.set(thread_id, 0);

		for (int i = 0; i < number_.length(); i++)
		{
			if (i == thread_id)
			{
				continue;
			}
			while (choosing_.get(i) == 1) {};
			while (!((number_.get(i) == 0) || ((number_.get(thread_id) < number_.get(i)) || ((number_.get(thread_id) == number_.get(i)) && (thread_id < i))))) {};
		}
	}

	@Override
	public void unlock(int thread_id)
	{
		number_.set(thread_id, 0);
	}
}
