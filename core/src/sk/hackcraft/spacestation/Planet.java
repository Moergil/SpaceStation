package sk.hackcraft.spacestation;


import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Planet extends Actor
{
	public static final double INITIAL_PERCENTAGE_OF_AMOUNT_OF_GOODS = 0.4;
	
	private Sprite sprite;

	private double distance;

	private boolean isDestroyed;

	
	private Map<GoodsType, CargoContainer> cargoContainers = new EnumMap<GoodsType, CargoContainer>(GoodsType.class);
	private List<Gauge> gaugesList = new ArrayList<Gauge>();
	
	public Planet(Sprite sprite, Vector2 size, Vector2 position, double distance)
	{
		this.sprite = sprite;
		setSize(size.x, size.y);
		setPosition(position.x, position.y);
		this.isDestroyed = false;
		
		for (GoodsType goodsType : GoodsType.values())
		{
			final CargoContainer container = new CargoContainer(goodsType, 100);
			cargoContainers.put(goodsType, container);
			
			Gauge gauge = Gauge.create(goodsType, 30);
			gauge.setMax(container.getCargoCapacity());
			gauge.setValueProvider(new Gauge.ValueProvider()
			{
				@Override
				public float getValue()
				{
					return container.getCargoAmount();
				}
			});
			gaugesList.add(gauge);
		}
		
		this.setInitialGoods();
	}

	public CargoContainer getCargoContainer(GoodsType goodsType)
	{
		return cargoContainers.get(goodsType);
	}

	public double getDistance()
	{
		return distance;
	}
	
	@Override
	public void act(float delta)
	{
		super.act(delta);
				
	}
	
	public void increaseAmountOfGoods(GoodsType type, int amount){
		int originAmount = this.cargoContainers.get(type).getCargoAmount();
		
		if((originAmount+amount) < this.cargoContainers.get(type).getCargoCapacity()){
			this.cargoContainers.get(type).setCargoAmount((originAmount+amount));
		}else{
			this.cargoContainers.get(type).setCargoAmount( this.cargoContainers.get(type).getCargoCapacity());
		}
	}
	
	public void reduceAmountOfGoods(GoodsType type, int amount){
		
		int originAmount = this.cargoContainers.get(type).getCargoAmount();
		
		if(originAmount > amount){
			this.cargoContainers.get(type).setCargoAmount(originAmount - amount);
			
		}else{
			this.cargoContainers.get(type).setCargoAmount(0);
			this.setDestroyed(true);
		}
		
		
	}
	
	public void setInitialGoods(){
		this.cargoContainers.get(GoodsType.FERTILIZERS).
			setCargoAmount((int)(this.cargoContainers.get(GoodsType.FERTILIZERS).getCargoCapacity()*
					INITIAL_PERCENTAGE_OF_AMOUNT_OF_GOODS));
		
		this.cargoContainers.get(GoodsType.GOODS).
		setCargoAmount((int)(this.cargoContainers.get(GoodsType.GOODS).getCargoCapacity()*
				INITIAL_PERCENTAGE_OF_AMOUNT_OF_GOODS));
		
		this.cargoContainers.get(GoodsType.HYDROGEN).
		setCargoAmount((int)(this.cargoContainers.get(GoodsType.HYDROGEN).getCargoCapacity()*
				INITIAL_PERCENTAGE_OF_AMOUNT_OF_GOODS));
		
		this.cargoContainers.get(GoodsType.METALS).
		setCargoAmount((int)(this.cargoContainers.get(GoodsType.METALS).getCargoCapacity()*
				INITIAL_PERCENTAGE_OF_AMOUNT_OF_GOODS));
		
		this.cargoContainers.get(GoodsType.WATER).
		setCargoAmount((int)(this.cargoContainers.get(GoodsType.WATER).getCargoCapacity()*
				INITIAL_PERCENTAGE_OF_AMOUNT_OF_GOODS));
	}
	
	
	public boolean isDestroyed()
	{
		return isDestroyed;
	}

	public void setDestroyed(boolean isDestroyed)
	{
		this.isDestroyed = isDestroyed;
	}

	public Map<GoodsType, CargoContainer> getCargoContainers()
	{
		return cargoContainers;
	}

	public List<Gauge> getGaugesList()
	{
		return gaugesList;
	}

	public void setDistance(double distance)
	{
		this.distance = distance;
	}

	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		sprite.setCenter(getCenterX(), getCenterY());
		sprite.draw(batch);
		
		float x = getX() + 10;
		float y = getY() - 10;
		for (int i = 0; i < gaugesList.size(); i++)
		{
			x += 12;
			
			gaugesList.get(i).draw(batch, x, y);
		}
	}
	
	
}
