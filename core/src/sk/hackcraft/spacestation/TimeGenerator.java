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
	
	private Random seedGenerator;
	
	private Random delayOfTaskGenerator;
	private Random durationOfTaskGenerator;
	
	protected TimeGenerator(){
		
		this.seedGenerator =  new Random();
		
		this.delayOfTaskGenerator = new Random(this.seedGenerator.nextInt());
		this.durationOfTaskGenerator = new Random(this.seedGenerator.nextInt());

	}
	
	public static TimeGenerator getInstance(){
		if(instance == null){
			
			instance = new TimeGenerator();
		}
		
		return instance;
		
	}
	
	public static double getDelayOfTask(int numberOfTasks, int numberOfPlanets,int numberOfShips){
		double value = ((double)numberOfPlanets/(double) numberOfShips)*numberOfTasks*4;
		
		return getValueOfExponencialDistribution(value, getInstance().getDelayOfTaskGenerator());
	}
	
	public static double getDurationOfTask(int numberOfTasks,int numberOfCargo, double distance){
		//raw time = min time
		double minTime = distance + 2 * TIME_OF_LANDING + TIME_OF_DOCKING + (numberOfCargo/100)*TIME_OF_LOADING + TIME_ON_PLANET;
		
		double middleValue = minTime*0.4;
		
		return getValueOfExponencialDistribution(middleValue, getInstance().getDurationOfTaskGenerator()) + minTime;
		
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

	public Random getDurationOfTaskGenerator()
	{
		return durationOfTaskGenerator;
	}

	
	
	
	
}
