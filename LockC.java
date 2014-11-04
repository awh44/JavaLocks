import java.util.concurrent.atomic.AtomicInteger;

public class LockC implements Lock
{
	private AtomicInteger common;
	public LockC()
	{
		common = new AtomicInteger(0);
	}

	@Override
	public void lock(int thread_id)
	{
		while (!common.compareAndSet(0, thread_id)) {};
	}

	@Override
	public void unlock(int thread_id)
	{
		common.set(0);
	}
}