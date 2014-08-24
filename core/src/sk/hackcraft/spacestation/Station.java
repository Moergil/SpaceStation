package sk.hackcraft.spacestation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

public class Station extends Actor
{
	private Set<Dock> docks = new HashSet<Dock>();
	private Set<Dock> freeDocks = new HashSet<Dock>();

	private Set<StorageFacility> storageFacilities = new HashSet<StorageFacility>();
	private Set<StorageFacility> freeStorageFacilities = new HashSet<StorageFacility>();
	
	private Sprite sprite;
	
	public Station(Sprite sprite)
	{
		this.sprite = sprite;
		
		setSize(sprite.getWidth(), sprite.getHeight());
	}
	
	public void addDock(Dock dock)
	{
		docks.add(dock);
	}
	
	public Set<Dock> getDocks()
	{
		return docks;
	}
	
	public Set<Dock> getFreeDocks()
	{
		freeDocks.clear();
		
		for (Dock dock : docks)
		{
			if (dock.isFree())
			{
				freeDocks.add(dock);
			}
		}
		
		return freeDocks;
	}
	
	public void addStorageFacility(StorageFacility storageFacility)
	{
		storageFacilities.add(storageFacility);
	}
	
	public Set<StorageFacility> getStorageFacilities()
	{
		return storageFacilities;
	}
	
	public Set<StorageFacility> getFreeStorageFacilities(GoodsType type)
	{
		freeStorageFacilities.clear();
		
		for (StorageFacility facility : storageFacilities)
		{
			if (!facility.isTransferringCargo())
			{
				if (facility.getCargoContainer().getCargoType().equals(type))
				{
					freeStorageFacilities.add(facility);
				}
			}
		}
		
		return freeStorageFacilities;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		super.draw(batch, parentAlpha);
		
		sprite.setPosition(getX(), getY());
		sprite.draw(batch);
	}
	
	@Override
	public void drawDebug(ShapeRenderer shapes)
	{
		super.drawDebug(shapes);
	}
}