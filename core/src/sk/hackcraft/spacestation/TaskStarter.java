package sk.hackcraft.spacestation;



import java.util.Date;

import com.badlogic.gdx.utils.Timer;

public class TaskStarter extends Timer.Task
{
	private TaskAndPointsManager manager;
	private int numberOfTasks;
	
	public TaskStarter(TaskAndPointsManager manager){
		super();
		this.manager = manager;
		this.numberOfTasks = 0;
		
	}

	@Override
	public void run()
	{
		//toto dokoncit, ked budu zname lode, zatial nastavene na 5
		int numberOfShips = 5;
		

		PlanetTask newTask = TaskGenerator.generateNewTask(manager.getGame().getPlanets());
		
		int index = 0;
		switch(newTask.getPlanet().getType()){
			case FOOD:index = 0;
				break;
			case ORE: index = 1;
				break;
			case MEDICINE:index =2;
				break;
			case MATERIAL:index =3;
				break;
			case ELECTRONICS:index=4;
				break;
		}
		this.manager.getGame().getPlanets().get(index).reduceAmountofGoods(newTask.getType(), newTask.getAmount());
		

		
		//testovanie a debugg
		System.out.println("test TaskStarter-run");
		Date cas = new Date();
		System.out.println(cas.toString()+" Planeta " + newTask.getPlanet().getType()+" typ "+newTask.getType()+" mnozstvo "+newTask.getAmount());
		
		if(manager.isGenerateTasks()){

			float secondsOfDelay = (float)TimeGenerator.getDelayOfTask(this.numberOfTasks, manager.getGame().getPlanets().size(), numberOfShips);
			this.numberOfTasks++;
			System.out.println("Seconds of delay"+secondsOfDelay);
			 manager.getGame().getTimer().scheduleTask(new TaskStarter(this.manager), secondsOfDelay);
			//manager.getGame().getTimer().scheduleTask(this, secondsOfDelay);
			 
			

		}
		
		
	}

}
