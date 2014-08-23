package sk.hackcraft.spacestation;

import com.badlogic.gdx.scenes.scene2d.Action;

public class DockAction extends Action
{
	private Ship ship;
	private Dock dock;
	
	private float executeOffsetTime;
	private float actualTime;
	
	@Override
	public void reset()
	{
		actualTime = 0;
	}
	
	public void set(Ship ship, Dock dock, float executeOffsetTime)
	{
		this.ship = ship;
		this.dock = dock;
		this.executeOffsetTime = executeOffsetTime;
	}
	
	@Override
	public boolean act(float delta)
	{
		actualTime += delta;
		
		if (actualTime >= executeOffsetTime)
		{
			if (dock.hasDockedShip())
			{
				// TODO
				System.out.println("Explosion!");
			}
			else
			{
				dock.dockShip(ship);
			}
			return true;
		}
		
		return false;
	}
}
