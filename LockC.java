import java.util.concurrent.atomic.AtomicInteger;

public class LockC implements Lock
{
	private AtomicInteger common;
	public LockC()
	{
		//Start the common variable at -1 to show that no threads want it
		common = new AtomicInteger(-1);
	}

	@Override
	public void lock(int thread_id)
	{
		//If the common variable is currently -1, that means no other thread has the lock, so take
		//lock by setting the common value to the current thread's id. Otherwise, wait for the lock
		//to be released by common being reset to -1.
		while (!common.compareAndSet(-1, thread_id)) {};
	}

	@Override
	public void unlock(int thread_id)
	{
		//Reset common to -1 to show that the thread is done in the critical section and the lock is
		//avaiable again.
		common.set(-1);
	}
}
