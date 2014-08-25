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
		GoodsType type = chooseRandomGoods();
		int amount = chooseRandomAmountOfGoods();

		return new PlanetTask(planet,type,amount);
	}
	
	public static int chooseRandomAmountOfGoods(){
		double value = getInstance().getChoosingAmountGenerator().nextDouble();
		
		double iterator = 0.0;
		if(value < (iterator+=ONE_HUNDRED_TON_PERCENTAGE)){
			return 2;
		}
		if(value < (iterator+=TWO_HUNDREDS_TON_PERCENTAGE)){
			return 4;
		}
		if(value < (iterator+=THREE_HUNDREDS_TON_PERCENTAGE)){
			return 6;
		}
		return 8;
	}
	
	public static GoodsType chooseRandomGoods(){
		int value = getInstance().getChoosingGoodsGenerator().nextInt(GoodsType.getNumberOfAllTypes());
		
		switch(value){
			case 0:return GoodsType.FERTILIZERS;
			case 1:return GoodsType.GOODS;
			case 2:return GoodsType.HYDROGEN;
			case 3:return GoodsType.METALS;
			default:return GoodsType.WATER;	
		}
		
		
	}
	
	public static Planet chooseRandomPlanet(ArrayList<Planet> planetsList){
		int numberOfPlanets = planetsList.size();
		
		int value = getInstance().getChoosingPlanetGenerator().nextInt(numberOfPlanets);
		
		return planetsList.get(value);
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
