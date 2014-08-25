package sk.hackcraft.spacestation;

import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Planet extends Actor
{
	private Sprite sprite;
	
	private GoodsType type;
	private double distance;
	private HashMap<GoodsType,CargoContainer> storage;
	private boolean isDestroyed;
	
	public Planet(Sprite sprite, Vector2 size, Vector2 position, GoodsType type, double distance)
	{
		this.sprite = sprite;
		setSize(size.x, size.y);
		setPosition(position.x, position.y);
		this.isDestroyed = false;
		
		this.type = type;
		
		this.storage = new HashMap<GoodsType,CargoContainer>();
		this.storage.put(GoodsType.FOOD, new CargoContainer(GoodsType.FOOD,1200));
		this.storage.put(GoodsType.ORE, new CargoContainer(GoodsType.ORE,1200));
		this.storage.put(GoodsType.MEDICINE, new CargoContainer(GoodsType.MEDICINE,1200));
		this.storage.put(GoodsType.MATERIAL, new CargoContainer(GoodsType.MATERIAL,1200));
		this.storage.put(GoodsType.ELECTRONICS, new CargoContainer(GoodsType.ELECTRONICS,1200));
		
	}
	
	public void setAmountOfGoods(GoodsType type, int amount){
		
		this.storage.get(type).setCargoAmount(amount);
	}
	
	public void  reduceAmountofGoods(GoodsType type, int amount){
		int original = this.storage.get(type).getCargoAmount();
		if(original > amount){
			this.setAmountOfGoods(type, (original - amount));
		}else{
			this.setAmountOfGoods(type, 0);
			this.isDestroyed = true;
		}
	}
	
	public void setAmountOfGoods(int food, int ore, int medicine, int material, int electronics){
		this.setAmountOfGoods(GoodsType.FOOD, food);
		this.setAmountOfGoods(GoodsType.ORE, ore);
		this.setAmountOfGoods(GoodsType.MEDICINE, medicine);
		this.setAmountOfGoods(GoodsType.MATERIAL, material);
		this.setAmountOfGoods(GoodsType.ELECTRONICS, electronics);
		
	}

	public GoodsType getType()
	{
		return type;
	}

	public void setType(GoodsType type)
	{
		this.type = type;
	}

	
	
	public HashMap<GoodsType, CargoContainer> getStorage()
	{
		return storage;
	}

	public void setStorage(HashMap<GoodsType, CargoContainer> storage)
	{
		this.storage = storage;
	}

	public boolean isDestroyed()
	{
		return isDestroyed;
	}

	public void setDestroyed(boolean isDestroyed)
	{
		this.isDestroyed = isDestroyed;
	}

	public void setDistance(double distance)
	{
		this.distance = distance;
	}

	public double getDistance()
	{
		return distance;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		sprite.setCenter(getCenterX(), getCenterY());
		sprite.draw(batch);
	}
	
	
}
