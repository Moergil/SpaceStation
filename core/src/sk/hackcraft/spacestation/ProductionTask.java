package sk.hackcraft.spacestation;

import com.badlogic.gdx.utils.Timer;

public class ProductionTask extends Timer.Task
{
	private TaskAndPointsManager manager;

	public static final int PRODUCING_AMOUNT_PER_SEC = 1;
	public static final float SECONDS_BETWEEN_PRODUCTION = 2;
	
	public ProductionTask(TaskAndPointsManager manager){
		this.manager = manager;
	}
	@Override
	public void run()
	{
		for(int i = 0 ; i < this.manager.getGame().getPlanets().size();i++){
			
			int valueOfPlanet = i % GoodsType.getNumberOfAllTypes();
			
			switch(valueOfPlanet){
				case 0:
					this.manager.getGame().getPlanets().get(i).increaseAmountOfGoods(GoodsType.FERTILIZERS, PRODUCING_AMOUNT_PER_SEC);
					break;
				case 1:
					this.manager.getGame().getPlanets().get(i).increaseAmountOfGoods(GoodsType.GOODS, PRODUCING_AMOUNT_PER_SEC);
					break;
				case 2:
					this.manager.getGame().getPlanets().get(i).increaseAmountOfGoods(GoodsType.HYDROGEN, PRODUCING_AMOUNT_PER_SEC);
					break;
				case 3:
					this.manager.getGame().getPlanets().get(i).increaseAmountOfGoods(GoodsType.METALS, PRODUCING_AMOUNT_PER_SEC);
					break;
				case 4:
					this.manager.getGame().getPlanets().get(i).increaseAmountOfGoods(GoodsType.WATER, PRODUCING_AMOUNT_PER_SEC);
					break;
				
			}
			
		}
		
		
		if(manager.isGenerateTasks()){

			 manager.getGame().getTimer().scheduleTask(new ProductionTask(this.manager), SECONDS_BETWEEN_PRODUCTION);

		}
	}

}
