package sk.hackcraft.spacestation;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class TaskGenerator
{
	private static final double ONE_HUNDRED_TON_PERCENTAGE = 0.5;
	private static final double TWO_HUNDREDS_TON_PERCENTAGE = 0.30;
	private static final double THREE_HUNDREDS_TON_PERCENTAGE = 0.15;
	private static final double FOUR_HUNDREDS_TON_PERCENTAGE = 0.05;
	
	private static TaskGenerator instance = null;
	
	private TimeGenerator timeGenerator;
	
	private Random choosingPlanetGenerator;
	private Random choosingGoodsGenerator;
	private Random choosingAmountGenerator;
	
	private TaskGenerator(){
		this.timeGenerator = TimeGenerator.getInstance();

		this.choosingPlanetGenerator = new Random(this.timeGenerator.getSeedGenerator().nextInt());
		this.choosingGoodsGenerator = new Random(this.timeGenerator.getSeedGenerator().nextInt());
		this.choosingAmountGenerator = new Random(this.timeGenerator.getSeedGenerator().nextInt());
		
	}
	
	public static TaskGenerator getInstance(){
		
		if(instance == null){
			
			instance = new TaskGenerator();
		}
		
		return instance;
	}
	
	public static PlanetTask generateNewTask(ArrayList<Planet> planetList){
		
		Planet planet = chooseRandomPlanet(planetList);
		GoodsType type = chooseRandomGoods(planet);
		int amount = chooseRandomAmountOfGoods();

		return new PlanetTask(planet,type,amount);
	}
	
	public static int chooseRandomAmountOfGoods(){
		double value = getInstance().getChoosingAmountGenerator().nextDouble();
		
		double iterator = 0.0;
		if(value < (iterator+=ONE_HUNDRED_TON_PERCENTAGE)){
			return 100;
		}
		if(value < (iterator+=TWO_HUNDREDS_TON_PERCENTAGE)){
			return 200;
		}
		if(value < (iterator+=THREE_HUNDREDS_TON_PERCENTAGE)){
			return 300;
		}
		return 400;
	}
	
	public static GoodsType chooseRandomGoods(Planet planet){
		
		GoodsType forbidenType = planet.getType();
		while(true){
			int value = getInstance().getChoosingGoodsGenerator().nextInt(GoodsType.getNumberOfAllTypes());
			switch(value){
				case 0:
					if(forbidenType != GoodsType.FOOD)
						return GoodsType.FOOD;
				case 1:
					if(forbidenType != GoodsType.ORE)
						return GoodsType.ORE;	
				case 2:
					if(forbidenType != GoodsType.MEDICINE)
						return GoodsType.MEDICINE;
				case 3:
					if(forbidenType != GoodsType.MATERIAL)
						return GoodsType.MATERIAL;
				case 4:
					if(forbidenType != GoodsType.ELECTRONICS)
						return GoodsType.ELECTRONICS;	
			}// switch
			
		}//while
		
	}
	
	public static Planet chooseRandomPlanet(ArrayList<Planet> planetsList){
		int numberOfPlanets = planetsList.size();
		
		double value = getInstance().getChoosingPlanetGenerator().nextDouble();
		
		for(int i = 0 ; i < numberOfPlanets-1; i++){
			
			if(value < i * (1.0 / (double) numberOfPlanets) ){
				
				return planetsList.get(i);
			}
		}
		
		return planetsList.get(numberOfPlanets-1);
	}

	
	public Random getChoosingPlanetGenerator()
	{
		return choosingPlanetGenerator;
	}

	public Random getChoosingGoodsGenerator()
	{
		return choosingGoodsGenerator;
	}

	public Random getChoosingAmountGenerator()
	{
		return choosingAmountGenerator;
	}

	public TimeGenerator getTimeGenerator()
	{
		return timeGenerator;
	}
	
	
	
}
