package sk.hackcraft.spacestation;

import java.util.LinkedList;
import java.util.List;

import sk.hackcraft.spacestation.Selectable.Selector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class ShipsQueueMenu extends Actor
{
	private Texture texture;
	
	private LinkedList<Ship> waitingShips = new LinkedList<Ship>();
	
	public ShipsQueueMenu()
	{
		texture = new Texture(Gdx.files.internal("sprite/shipsQueueBg.png"));
		
		setWidth(100);
	}
	
	public void matchHeightToStage()
	{
		float height = getStage().getHeight();
		
		setSize(getWidth(), height);
	}

	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		batch.draw(texture, getX(), getY());
		
		float x = getX() + 15;
		float y = getY() + getHeight() - 30;
		
		int i = 0;
		for (Ship avatar : waitingShips)
		{
			if (i > 10)
			{
				break;
			}
			
			avatar.setPosition(x, y);
			avatar.draw(batch, parentAlpha);
			
			y -= avatar.getHeight() + 5;
			
			i++;
		}
	}

	@Override
	public void act(float delta)
	{
		// TODO Auto-generated method stub
		super.act(delta);
	}

	public void queueShip(Ship ship)
	{
		waitingShips.add(ship);
	}
	
	public void orderShipToDock(Ship ship, Dock dock)
	{
		waitingShips.remove(ship);
		
		initiateDocking(ship, dock);
	}
	
	public boolean contains(Ship ship)
	{
		return waitingShips.contains(ship);
	}
	
	public abstract void initiateDocking(Ship ship, Dock dock);
}
