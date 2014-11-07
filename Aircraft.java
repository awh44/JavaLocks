import java.util.Random;

public class Aircraft implements Runnable
{
	private static long start_time_;
	private static int time_unit_;
	private static Lock runway_lock_;
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

	//Strings used in the logging of messages by the planes
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
		runway_lock_ = lock;
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
		//indicate that the plane is waiting to take off
		waiting_lock_.lock(id_);
		waiting_planes_++;
		waiting_lock_.unlock(id_);

		//Try to enter the runway, and wait until its free
		runway_lock_.lock(id_);

		//indicate that the plane is no longer waiting to take off
		waiting_lock_.lock(id_);
		waiting_planes_--;
		waiting_lock_.unlock(id_);


		//If there are no other planes waiting to take off, let the takeoff take a random amount of
		//time between to take off, between a set min and max. Otherwise, force the plan to use the
		//min amount of time.
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
			//wait for the specified time, until takeoff has occurred.
			Thread.sleep(takeoff_time * time_unit_);
		}
		catch (InterruptedException e)
		{
		}
		log(id_, END_TAKEOFF_STRING);

		//indicate the plane is done with the runway
		runway_lock_.unlock(id_);
	}

	private void fly()
	{
		//simply fly around for a random amount of time less than MAX_FLY time units
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
		//perform all of the plane's required touch-and-goes
		for (int i = 0; i < touch_and_goes_; i++)
		{
			touch_and_go();
			fly();
		}
	}

	private void touch_and_go()
	{
		//perform a single touch_and_go by first trying to enter the runway and waiting for it to be
		//available
		runway_lock_.lock(id_);

		//determine how long this touch-and-go will take, between a set max and min
		int touch_go_time = random_.nextInt(MAX_TOUCH_GO - MIN_TOUCH_GO + 1) + MIN_TOUCH_GO;

		log(id_, BEGIN_TOUCH_GO_STRING);
		try
		{
			//wait for the specified number of time units, until the touch-and-go is complete
			Thread.sleep(touch_go_time * time_unit_);
		}
		catch (InterruptedException e)
		{
		}
		log(id_, END_TOUCH_GO_STRING);

		//release the lock to show that the runway is free again
		runway_lock_.unlock(id_);
	}

	private void land()
	{
		//try to obtain the runway, and if it's not avaiable, wait for it
		runway_lock_.lock(id_);

		log(id_, BEGIN_LANDING_STRING);
		try
		{
			//sleep for the number of time units it takes to land
			Thread.sleep(LANDING * time_unit_);
		}
		catch (InterruptedException e)
		{
		}
		log(id_, END_LANDING_STRING);

		//indicate that the runway is free again by releasing the lock
		runway_lock_.unlock(id_);
	}
}
