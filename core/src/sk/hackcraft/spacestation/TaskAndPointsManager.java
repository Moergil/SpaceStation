package sk.hackcraft.spacestation;


public class TaskAndPointsManager
{
	private SpaceStationGame game;

	private boolean generateTasks;
	private int pointsCounter;
	private int tasksCounter ;
	
	public TaskAndPointsManager(SpaceStationGame game){
		this.game = game;
		this.generateTasks = true;
		this.pointsCounter = 0;	
		this.tasksCounter = 0;
	}
	
	
	
	public int getPointsCounter()
	{
		return pointsCounter;
	}



	public void setPointsCounter(int pointsCounter)
	{
		this.pointsCounter = pointsCounter;
	}



	public void startGeneratingTasks(){
		TaskStarter starter = new TaskStarter(this);
		starter.run();
		
		ProductionTask production = new ProductionTask(this);
		production.run();
		
		ConsumptionTask consumption = new ConsumptionTask(this);
		consumption.run();
	
		PointsCountingTask countingPoints = new PointsCountingTask(this);
		countingPoints.run();
	}
	

	public SpaceStationGame getGame()
	{
		return game;
	}

	public boolean isGenerateTasks()
	{
		return generateTasks;
	}

	public void addPoints(int points){
		
		this.pointsCounter+=points;
	}
	
	public boolean checkGameOver(){
		
		for(Planet planet:this.getGame().getPlanets()){
			
			if(planet.isDestroyed()) {
				
				this.generateTasks =false;
				System.out.println("GAME OVER");
				return true;
			}
		}
		
		return false;
	}



	public int getTasksCounter()
	{
		return tasksCounter;
	}



	public void setTasksCounter(int tasksCounter)
	{
		this.tasksCounter = tasksCounter;
	}
	
	
}
