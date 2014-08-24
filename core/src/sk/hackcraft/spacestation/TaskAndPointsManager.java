package sk.hackcraft.spacestation;

import java.util.PriorityQueue;

public class TaskAndPointsManager
{
	private SpaceStationGame game;
	private PriorityQueue<PlanetTask> planetTasks;
	private boolean generateTasks;
	
	public TaskAndPointsManager(SpaceStationGame game){
		this.game = game;
		this.generateTasks = true;
		this.planetTasks = new PriorityQueue<PlanetTask>();
		
	}
	
	public void startGeneratingTasks(){
		TaskStarter starter = new TaskStarter(this);
		starter.run();
	
	}
	
	

	public SpaceStationGame getGame()
	{
		return game;
	}

	public PriorityQueue<PlanetTask> getPlanetTasks()
	{
		return planetTasks;
	}

	public boolean isGenerateTasks()
	{
		return generateTasks;
	}

	
	
}
