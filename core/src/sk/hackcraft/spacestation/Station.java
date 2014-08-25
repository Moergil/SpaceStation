package sk.hackcraft.spacestation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Station extends Actor
{
	private Set<Dock> docks = new HashSet<Dock>();
	private Set<Dock> freeDocks = new HashSet<Dock>();

	private Set<StorageFacility> storageFacilities = new HashSet<StorageFacility>();
	private Set<StorageFacility> freeStorageFacilities = new HashSet<StorageFacility>();
	
	private Sprite sprite;
	
	private int frameCounter = 0;
	private List<TextureRegion[]> textures;
	private List<Integer> texturePointers;
	private List<Integer> delays;
	
	public Station(Sprite sprite)
	{
		this.sprite = sprite;
		
		setSize(sprite.getWidth(), sprite.getHeight());
		
		textures = new ArrayList<TextureRegion[]>();
		texturePointers = new ArrayList<Integer>();
		delays = new ArrayList<Integer>();
	}
	
	public void setTextures(Texture texture, int delay)
	{
		delays.add(delay);
		texturePointers.add(0);
		int frameCount = texture.getWidth()/99;
		TextureRegion[] frameSequence = new TextureRegion[frameCount];
		textures.add(frameSequence);
		for (int i = 0; i < frameCount; i++)
		{
			frameSequence[i] = new TextureRegion(texture, 99*i, 0, 99, 212);
		}
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
		frameCounter++; 
		
		super.draw(batch, parentAlpha);
		
		sprite.setPosition(getX(), getY());
		sprite.draw(batch);
		
		for (int i = 0; i < textures.size(); i++)
		{
			batch.draw(textures.get(i)[texturePointers.get(i)], getX(), getY());
			if (frameCounter % delays.get(i) == 0)
			{
				incrementTexturePointer(i);
			}
		}
	}
	
	@Override
	public void drawDebug(ShapeRenderer shapes)
	{
		super.drawDebug(shapes);
	}
	
	private void incrementTexturePointer(int i)
	{
		int value = (texturePointers.get(i)+1) % textures.get(i).length;
		texturePointers.set(i, value);
	}
}