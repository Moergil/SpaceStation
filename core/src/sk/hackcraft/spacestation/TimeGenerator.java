package sk.hackcraft.spacestation;

import java.util.Random;

public class TimeGenerator
{
	//time in sec. 
	public static final float TIME_OF_LANDING = 1.5f;
	public static final float TIME_OF_DOCKING = 1f;
	public static final float TIME_OF_DOCKING_APPROACH = 5f;
	public static final float TIME_OF_COMPLETE_DOCKING = TIME_OF_DOCKING + TIME_OF_DOCKING_APPROACH;
	
	//time to load or unload 100 ton of cargo
	public static final float TIME_OF_LOADING = 2.0f;
	public static final float TIME_ON_PLANET = 4.0f;
	
	private static TimeGenerator instance = null;
	
	private Random seedGenerator;
	
	private Random delayOfTaskGenerator;
	
	protected TimeGenerator(){
		
		this.seedGenerator =  new Random();
		
		this.delayOfTaskGenerator = new Random(this.seedGenerator.nextInt());

	}
	
	public static TimeGenerator getInstance(){
		if(instance == null){
			
			instance = new TimeGenerator();
		}
		
		return instance;
		
	}
	
	public static double getDelayOfTask(int numberOfTasks, int numberOfPlanets,int numberOfShips){
		
		int primarySeconds = 0;
		if(numberOfTasks < 10){
			primarySeconds = 15;
		}else if(numberOfTasks < 20){
			primarySeconds = 20;
		}else if(numberOfTasks < 30){
			primarySeconds = 8;
		}else if(numberOfTasks < 40){
			primarySeconds = 5;
		}else primarySeconds = 3;
		
		double middleValue = (double)numberOfPlanets/(double)numberOfShips;
		
		getInstance();
		double result = primarySeconds*TimeGenerator.getValueOfExponencialDistribution(middleValue,	getInstance().delayOfTaskGenerator);			
		
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
}
