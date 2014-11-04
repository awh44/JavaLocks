public interface Lock
{
	public void lock(int thread_id);
	public void unlock(int thread_id);
}
