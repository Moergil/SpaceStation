package sk.hackcraft.spacestation;

import java.util.List;
import java.util.Set;

import com.badlogic.gdx.utils.Timer;

public class PointsCountingTask extends Timer.Task
{

	public static final double POINTS_FOR_STORAGE = 10.0;
	
	public static final float SECONDS_BETWEEN_COUNTING = 30;
	
	private TaskAndPointsManager manager;

	
	
	public PointsCountingTask(TaskAndPointsManager manager)
	{
		super();
		this.manager = manager;
	}



	@Override
	public void run()
	{
		int points = countPointsForStation();
		points += countPointsForPlanets();
		this.manager.addPoints(points);
		
		if(manager.isGenerateTasks()){

			 manager.getGame().getTimer().scheduleTask(new PointsCountingTask(this.manager), SECONDS_BETWEEN_COUNTING);
			 System.out.println("POINTS "+manager.getPointsCounter());
		}
	}
	
	private int countPointsForStation(){
		
		double points = 0;
		
		Set<StorageFacility> storage = this.manager.getGame().getStation().getStorageFacilities();
		
		for(StorageFacility facility:storage){
			int amount = facility.getCargoContainer().getCargoAmount();
			int capacity = facility.getCargoContainer().getCargoCapacity();
			points += ((double)amount / (double) capacity)*POINTS_FOR_STORAGE;
		}

		return (int) points;
	}
	
	private int countPointsForPlanets(){
		
		double points = 0;
		
		List<Planet> planets = this.manager.getGame().getPlanets();
		for(Planet planet: planets){
			for(CargoContainer container:planet.getCargoContainers().values()){
				
				int capacity = container.getCargoCapacity();
				int amount = container.getCargoAmount();
				
				double percentage = (double)amount/(double)capacity;
				if(percentage <= 0.10){
					points -=1;
				}else if(percentage > 0.70 && percentage <=0.90){
					points+=0.5;
				}else if(percentage >0.90){
					points +=1;
				}
			}
			
		}
		
		return (int)points;
	}

}
