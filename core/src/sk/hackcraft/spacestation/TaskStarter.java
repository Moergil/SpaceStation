package sk.hackcraft.spacestation;



import java.util.Date;

import com.badlogic.gdx.utils.Timer;

public class TaskStarter extends Timer.Task
{
	private TaskAndPointsManager manager;
	
	public TaskStarter(TaskAndPointsManager manager){
		super();
		this.manager = manager;
		
	}

	@Override
	public void run()
	{
		//toto dokoncit, ked budu zname lode
		int numberOfShips = 5;
		

		PlanetTask newTask = TaskGenerator.generateNewTask(manager.getGame().getPlanets(), manager.getPlanetTasks().size());
		this.manager.getPlanetTasks().add(newTask);
		
		//testovanie a debugg
		System.out.println("test TaskStarter-run");
		Date cas = new Date();
		System.out.println(cas.toString()+" Planeta " + newTask.getPlanet().getType()+" typ "+newTask.getType()+" mnozstvo "+newTask.getAmount());
		
		if(manager.isGenerateTasks()){
			float secondsOfDelay = (float)TimeGenerator.getDelayOfTask(manager.getPlanetTasks().size(), manager.getGame().getPlanets().size(), numberOfShips)*10;
			System.out.println("Seconds of delay"+secondsOfDelay);
			manager.getGame().getTimer().scheduleTask(new TaskStarter(this.manager), secondsOfDelay);
		}
		
		
	}

}
