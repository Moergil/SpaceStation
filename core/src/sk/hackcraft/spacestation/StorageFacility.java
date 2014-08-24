package sk.hackcraft.spacestation;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class StorageFacility extends Actor
{
	private CargoContainer cargoContainer;
	private boolean transfer;
	
	public StorageFacility(CargoContainer container)
	{
		this.cargoContainer = container;
		
		setSize(30, 30);
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
	
	public void moveCargoTo(Dock dock, int amount)
	{
		
	}
	
	public void receiveCargoFrom(Dock dock, int amount)
	{
		
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
	public void drawDebug(ShapeRenderer shapes)
	{
		shapes.setColor(Color.ORANGE);
		shapes.rect(getX(), getY(), getWidth(), getHeight());
	}
}
