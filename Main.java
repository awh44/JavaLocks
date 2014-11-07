import java.util.Random;

public class Main 
{
	static final int MAX_TOUCH_GO = 6; //so 5 touch and goes is the max possible
	public static void main(String[] args)
	{
		if (args.length < 2)
		{
			System.out.println("Please indicate the number of planes and the length of a time unit (in milliseconds) as command line arguments (in that order).");
			return;
		}

		int num_aircraft;
		int time_unit;
		Lock lock;
		try
		{
			num_aircraft = Integer.parseInt(args[0]);
			time_unit = Integer.parseInt(args[1]);
			if ((args.length > 2) && (args[2].equals("C")))
			{
				lock = new LockC();
			}
			else
			{
				lock = new LockB(num_aircraft);
			}
		}
		catch (NumberFormatException e)
		{
			System.out.println("Please enter only numbers for the number of planes and the time unit.");
			return;
		}

		Random rand = new Random();

		Aircraft.set_time_unit(time_unit);
		Aircraft.set_lock(lock);
		Aircraft.set_random(rand);

		//create a thread for each Aircraft, which will have an id from 0 to num-aircraft - 1 and a
		//random number of touch-and-goes
		Thread threads[] = new Thread[num_aircraft];
		for (int i = 0; i < num_aircraft; i++)
		{
			threads[i] = new Thread(new Aircraft(i, rand.nextInt(MAX_TOUCH_GO)));
		}

		//Let the aircraft class know that the simulation is beginning
		//and then start all of the threads/planes
		Aircraft.begin_simulation();
		for (int i = 0; i < num_aircraft; i++)
		{
			threads[i].start();
		}

		try
		{
			//wait on all of the planes to finish their training flights
			for (int i = 0; i < num_aircraft; i++)
			{
				threads[i].join();
			}
		}
		catch (InterruptedException e)
		{
			System.out.println("Error: main interrupted.");
		}
	}
}
