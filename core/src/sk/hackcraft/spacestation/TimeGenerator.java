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
		int seconds = 0;
		if(numberOfTasks < 8){
			seconds = 30;
		}else if(numberOfTasks < 20){
			seconds = 20;
		}else if(numberOfTasks < 36){
			seconds = 15;
		}else if(numberOfTasks < 50){
			seconds = 10;
		}else{
			seconds = 5;
		}
		
		double middleValue = (double)numberOfPlanets/(double)numberOfShips;
		
		getInstance();
		double result = seconds*TimeGenerator.getValueOfExponencialDistribution(middleValue,	getInstance().delayOfTaskGenerator);			
		
		System.out.println("counter"+numberOfTasks + " primarySeconds "+ seconds+" delay "+result);
		
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
