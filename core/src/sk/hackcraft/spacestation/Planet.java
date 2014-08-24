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
	private Sprite sprite;

	private double distance;
	
	private Map<GoodsType, CargoContainer> cargoContainers = new EnumMap<GoodsType, CargoContainer>(GoodsType.class);
	private List<Gauge> gaugesList = new ArrayList<Gauge>();
	
	public Planet(Sprite sprite, Vector2 size, Vector2 position, double distance)
	{
		this.sprite = sprite;
		setSize(size.x, size.y);
		setPosition(position.x, position.y);
		
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
