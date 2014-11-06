import java.util.Random;

public class Aircraft implements Runnable
{
	private static long start_time_;
	private static int time_unit_;
	private static Lock lock_;
	private static Random random_;
	private static Lock waiting_lock_ = new LockC();
	private static int waiting_planes_ = 0;

	//Maximum and minimum number of time units for performing the actions
	private static final int MIN_TAKEOFF = 5;
	private static final int MAX_TAKEOFF = 10;
	private static final int MIN_TOUCH_GO = 10;
	private static final int MAX_TOUCH_GO = 12;
	private static final int LANDING = 10;
	private static final int MAX_FLY = 5;

	private static String BEGIN_TAKEOFF_STRING = " is about to take off.";
	private static String END_TAKEOFF_STRING = " took off.";
	private static String BEGIN_TOUCH_GO_STRING = " is beginning a touch and go.";
	private static String END_TOUCH_GO_STRING = " finished its touch and go.";
	private static String BEGIN_LANDING_STRING = " is beginning its landing.";
	private static String END_LANDING_STRING = " finished its landing.";

	private final int id_;
	private final int touch_and_goes_;
	
	public Aircraft(int id, int touch_and_goes)
	{
		id_ = id;
		touch_and_goes_ = touch_and_goes;
	}

	public static void begin_simulation()
	{
		start_time_ = System.currentTimeMillis();
	}

	public static void set_time_unit(int time_unit)
	{
		time_unit_ = time_unit;
	}

	public static void set_lock(Lock lock)
	{
		lock_ = lock;
	}

	public static void set_random(Random random)
	{
		random_ = random;	
	}

	private static void log(int id, String action)
	{
		System.out.println(System.currentTimeMillis() - start_time_ + ": aircraft number " + id + action);
	}


	@Override
	public void run()
	{
		takeoff();
		fly();
		touch_and_goes();
		land();
	}

	private void takeoff()
	{
		waiting_lock_.lock(id_);
		waiting_planes_++;
		waiting_lock_.unlock(id_);

		lock_.lock(id_);

		waiting_lock_.lock(id_);
		waiting_planes_--;
		waiting_lock_.unlock(id_);


		int takeoff_time;	
		if (waiting_planes_ == 0)
		{
			takeoff_time = random_.nextInt(MAX_TAKEOFF - MIN_TAKEOFF + 1) + MIN_TAKEOFF;
		}
		else
		{
			takeoff_time = MIN_TAKEOFF;
		}

		log(id_, BEGIN_TAKEOFF_STRING);
		try 
		{
			Thread.sleep(takeoff_time * time_unit_);
		}
		catch (InterruptedException e)
		{
		}
		log(id_, END_TAKEOFF_STRING);
		lock_.unlock(id_);
	}

	private void fly()
	{
		try
		{
			Thread.sleep(random_.nextInt(MAX_FLY * time_unit_));
		}
		catch (InterruptedException e)
		{
		}
	}

	private void touch_and_goes()
	{
		for (int i = 0; i < touch_and_goes_; i++)
		{
			touch_and_go();
			fly();
		}
	}

	private void touch_and_go()
	{
		lock_.lock(id_);
		int touch_go_time = random_.nextInt(MAX_TOUCH_GO - MIN_TOUCH_GO + 1) + MIN_TOUCH_GO;
		log(id_, BEGIN_TOUCH_GO_STRING);
		try
		{
			Thread.sleep(touch_go_time * time_unit_);
		}
		catch (InterruptedException e)
		{
		}
		log(id_, END_TOUCH_GO_STRING);
		lock_.unlock(id_);
	}

	private void land()
	{
		lock_.lock(id_);
		log(id_, BEGIN_LANDING_STRING);
		try
		{
			Thread.sleep(LANDING * time_unit_);
		}
		catch (InterruptedException e)
		{
		}
		log(id_, END_LANDING_STRING);
		lock_.unlock(id_);
	}
}
