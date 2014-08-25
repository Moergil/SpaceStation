package sk.hackcraft.spacestation;

import com.badlogic.gdx.utils.Timer;

public class ConsumptionTask extends Timer.Task
{
	
	public static final int CONSUMPTION_AMOUNT_PER_SEC = 1;
	public static final float SECONDS_BETWEEN_CONSUMPTION = 8;
	
	private TaskAndPointsManager manager;

	
	public ConsumptionTask(TaskAndPointsManager manager){
		super();
		this.manager = manager;
	}

	@Override
	public void run()
	{
		for(int i = 0 ; i < this.manager.getGame().getPlanets().size();i++){
			
			int valueOfPlanet = i % GoodsType.getNumberOfAllTypes();
			
			//0 - production fertilizers - consumption hydrogen and water
			//1 - production goods - consumption metal and fertilizers
			//2 - production hydrogen - consumption goods and water
			//3 - production metal - consumption fertilizers and goods
			//4 - production water - consumption hydrogen and metal
			
			switch(valueOfPlanet){
				case 0:
					this.manager.getGame().getPlanets().get(i).reduceAmountOfGoods(GoodsType.HYDROGEN, CONSUMPTION_AMOUNT_PER_SEC);
					this.manager.getGame().getPlanets().get(i).reduceAmountOfGoods(GoodsType.WATER, CONSUMPTION_AMOUNT_PER_SEC);
					break;
				case 1:
					this.manager.getGame().getPlanets().get(i).reduceAmountOfGoods(GoodsType.METALS, CONSUMPTION_AMOUNT_PER_SEC);
					this.manager.getGame().getPlanets().get(i).reduceAmountOfGoods(GoodsType.FERTILIZERS, CONSUMPTION_AMOUNT_PER_SEC);
					break;
				case 2:
					this.manager.getGame().getPlanets().get(i).reduceAmountOfGoods(GoodsType.GOODS, CONSUMPTION_AMOUNT_PER_SEC);
					this.manager.getGame().getPlanets().get(i).reduceAmountOfGoods(GoodsType.WATER, CONSUMPTION_AMOUNT_PER_SEC);
					break;
				case 3:
					this.manager.getGame().getPlanets().get(i).reduceAmountOfGoods(GoodsType.FERTILIZERS, CONSUMPTION_AMOUNT_PER_SEC);
					this.manager.getGame().getPlanets().get(i).reduceAmountOfGoods(GoodsType.GOODS, CONSUMPTION_AMOUNT_PER_SEC);
					break;
				case 4:
					this.manager.getGame().getPlanets().get(i).reduceAmountOfGoods(GoodsType.HYDROGEN, CONSUMPTION_AMOUNT_PER_SEC);
					this.manager.getGame().getPlanets().get(i).reduceAmountOfGoods(GoodsType.METALS, CONSUMPTION_AMOUNT_PER_SEC);
					break;
				
			}
			
			
			
		}
		
		if(manager.isGenerateTasks()){

			 manager.getGame().getTimer().scheduleTask(new ConsumptionTask(this.manager), SECONDS_BETWEEN_CONSUMPTION);

		}
		
	}

}
