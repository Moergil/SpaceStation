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
	
	
	private static TimeGenerator instance = null;
	
	private double timeCounter;
	private float primarySeconds;
	
	private Random seedGenerator;
	
	private Random delayOfTaskGenerator;
	
	protected TimeGenerator(){
		
		this.seedGenerator =  new Random();
		
		this.delayOfTaskGenerator = new Random(this.seedGenerator.nextInt());
		
		this.timeCounter = 1;
		this.primarySeconds =60;

	}
	
	public static TimeGenerator getInstance(){
		if(instance == null){
			
			instance = new TimeGenerator();
		}
		
		return instance;
		
	}
	
	public static double getDelayOfTask(int numberOfTasks, int numberOfPlanets,int numberOfShips){
		double counter =  getInstance().getTimeCounter();
		float seconds = getInstance().getPrimarySeconds();
		if(numberOfTasks >= counter){
			getInstance().setTimeCounter( counter + (60/seconds));
			getInstance().setPrimarySeconds((float)(seconds - 0.1*seconds));
		}
		
		double middleValue = (double)numberOfPlanets/(double)numberOfShips;
		
		getInstance();
		double result = seconds*TimeGenerator.getValueOfExponencialDistribution(middleValue,	getInstance().delayOfTaskGenerator);			
		
		return result;
	}
	
	
	
	public static double getValueOfExponencialDistribution(double middleValue,Random generator){
		double value = generator.nextDouble();
        double result = (-1*Math.log(value))/middleValue;
        
        return result;
		
	}
	

	public Random getDelayOfTaskGenerator()
	{
		return delayOfTaskGenerator;
	}

	public Random getSeedGenerator()
	{
		return seedGenerator;
	}

	public double getTimeCounter()
	{
		return timeCounter;
	}

	public void setTimeCounter(double timeCounter)
	{
		this.timeCounter = timeCounter;
	}

	public float getPrimarySeconds()
	{
		return primarySeconds;
	}

	public void setPrimarySeconds(float primarySeconds)
	{
		this.primarySeconds = primarySeconds;
	}
	
	
}
