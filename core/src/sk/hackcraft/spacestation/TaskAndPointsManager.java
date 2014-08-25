package sk.hackcraft.spacestation;


public class TaskAndPointsManager
{
	private SpaceStationGame game;

	private boolean generateTasks;
	
	public TaskAndPointsManager(SpaceStationGame game){
		this.game = game;
		this.generateTasks = true;
		
	}
	
	public void startGeneratingTasks(){
		TaskStarter starter = new TaskStarter(this);
		starter.run();
	
	}
	

	public SpaceStationGame getGame()
	{
		return game;
	}

	public boolean isGenerateTasks()
	{
		return generateTasks;
	}

	
	
}
