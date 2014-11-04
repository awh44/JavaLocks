import java.util.Random;

public class Airport
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
		try
		{
			num_aircraft = Integer.parseInt(args[0]);
			time_unit = Integer.parseInt(args[1]);
		}
		catch (NumberFormatException e)
		{
			System.out.println("Please enter only numbers for the number of planes and the time unit.");
			return;
		}

		Random rand = new Random();

		Aircraft.set_time_unit(time_unit);
		Aircraft.set_lock(new LockB(num_aircraft));
		Aircraft.set_random(rand);

		Thread threads[] = new Thread[num_aircraft];
		for (int i = 0; i < num_aircraft; i++)
		{
			threads[i] = new Thread(new Aircraft(i, rand.nextInt(MAX_TOUCH_GO)));
		}

		Aircraft.begin_simulation();
		for (int i = 0; i < num_aircraft; i++)
		{
			threads[i].start();
		}

		try
		{
			for (int i = 0; i < num_aircraft; i++)
			{
				threads[i].join();
			}
		}
		catch (InterruptedException e)
		{
			System.out.println("Main interrupted.");
		}
	}
}
