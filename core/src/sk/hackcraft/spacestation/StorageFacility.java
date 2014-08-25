package sk.hackcraft.spacestation;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class StorageFacility extends Actor
{
	private CargoContainer cargoContainer;
	private boolean transfer;
	
	private Gauge gauge;
	
	public StorageFacility(CargoContainer container)
	{
		this.cargoContainer = container;
		
		setSize(30, 30);
		
		gauge = Gauge.create(cargoContainer.getCargoType(), 30);
		gauge.setMax(container.getCargoCapacity());
		gauge.setValueProvider(new Gauge.ValueProvider()
		{
			@Override
			public float getValue()
			{
				return cargoContainer.getCargoAmount();
			}
		});
	}
	
	public boolean canTransferCargo(Dock dock)
	{
		if (dock.hasDockedShip())
		{
			Ship ship = dock.getDockedShip();
			
			return ship.getCargoContainer().getCargoType().equals(cargoContainer.getCargoType());
		}
		
		return false;
	}
	
	public CargoContainer getCargoContainer()
	{
		return cargoContainer;
	}
	
	public void setCargoTransfer(boolean transfer)
	{
		this.transfer = transfer;
	}
	
	public boolean isTransferringCargo()
	{
		return transfer;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		gauge.draw(batch, getX(), getY());
	}
	
	@Override
	public void drawDebug(ShapeRenderer shapes)
	{
		shapes.setColor(Color.ORANGE);
		shapes.rect(getX() - 10, getY(), getWidth(), getHeight());
	}
}
