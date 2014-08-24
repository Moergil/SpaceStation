package sk.hackcraft.spacestation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;

public class Station extends Group
{
	private Set<Dock> docks = new HashSet<Dock>();
	private Set<StorageFacility> storageFacilities = new HashSet<StorageFacility>();
	
	private Sprite sprite;
	
	public Station(Sprite sprite)
	{
		this.sprite = sprite;
		
		setSize(sprite.getWidth(), sprite.getHeight());
	}
	
	public void addDock(Dock dock)
	{
		docks.add(dock);
		addActor(dock);
	}
	
	public Set<Dock> getDocks()
	{
		return docks;
	}
	
	public Set<StorageFacility> getStorageFacilities()
	{
		return storageFacilities;
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
