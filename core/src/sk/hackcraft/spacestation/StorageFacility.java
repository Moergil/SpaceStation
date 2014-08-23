package sk.hackcraft.spacestation;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class StorageFacility extends Actor
{
	private CargoContainer cargoContainer;
	
	public StorageFacility(CargoContainer container)
	{
		this.cargoContainer = cargoContainer;
	}
	
	public boolean canTransferCargo(Dock dock)
	{
		return true;
	}
	
	public void moveCargoTo(Dock dock, int amount)
	{
		
	}
	
	public void receiveCargoFrom(Dock dock, int amount)
	{
		
	}
}
