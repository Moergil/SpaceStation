package sk.hackcraft.spacestation;

import java.util.Random;

public class TimeGenerator
{
	//time in sec. 
	
	public static final double TIME_OF_LANDING = 1.5;
	public static final double TIME_OF_DOCKING = 0.5;
	
	//time to load or unload 100 ton of cargo
	public static final double TIME_OF_LOADING = 2.0;
	public static final double TIME_ON_PLANET = 4.0;
	
	private Random delayOfTaskGenerator;
	
	public double getDelayOfTask(int numberOfTasks, int numberOfPlanets,int numberOfShips){
		//to do
		return -1;
	}
}
