package sk.hackcraft.spacestation;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Planet extends Actor
{
	private Sprite sprite;
	
	private GoodsType type;
	private double distance;
	private int storedGoods; //in tons
	
	public Planet(Sprite sprite, Vector2 size, Vector2 position, GoodsType type, double distance)
	{
		this.sprite = sprite;
		setSize(size.x, size.y);
		setPosition(position.x, position.y);
		
		this.type = type;
		this.storedGoods = 0;
	}

	public GoodsType getType()
	{
		return type;
	}

	public void setType(GoodsType type)
	{
		this.type = type;
	}

	public int getStoredGoods()
	{
		return storedGoods;
	}

	public void setStoredGoods(int storedGoods)
	{
		this.storedGoods = storedGoods;
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
