package sk.hackcraft.spacestation;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import sk.hackcraft.spacestation.StationView.Intent;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public abstract class ShipsQueueMenu extends Actor
{
	private Texture texture;
	
	private LinkedList<Ship> waitingShipsQueue = new LinkedList<Ship>();
	private Set<Ship> waitingShipsSet = new HashSet<Ship>();
	
	public ShipsQueueMenu()
	{
		texture = new Texture(Gdx.files.internal("sprite/shipsQueueBg.png"));
		
		setWidth(texture.getWidth());
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
		
		float x = getX() + 10;
		float y = getY() + getHeight() - 30;
		
		int i = 0;
		for (Ship ship : waitingShipsQueue)
		{
			if (i > 10)
			{
				break;
			}

			ship.setPosition(x, y);
			ship.draw(batch, parentAlpha);
			
			y -= ship.getHeight() + 7;
			
			i++;
		}
	}
	
	public Set<Ship> getWaitingShips()
	{
		return waitingShipsSet;
	}

	public void queueShip(Ship ship)
	{
		if (waitingShipsSet.add(ship))
		{
			ship.addAction(Actions.fadeIn(0.3f));
			ship.showCargoGauge(true);
			ship.setIdle();
			waitingShipsQueue.add(ship);
			
			System.out.println("Ship " + ship + " queued.");
		}
	}
	
	private void removeShip(Ship ship)
	{
		waitingShipsQueue.remove(ship);
		waitingShipsSet.remove(ship);
		
		ship.showCargoGauge(false);
		ship.addAction(Actions.fadeOut(0.3f));
		
		System.out.println("Ship " + ship + " dequeed.");
	}
	
	public void orderShipToDock(Ship ship, Dock dock)
	{
		removeShip(ship);

		initiateDocking(ship, dock);
	}
	
	public void sendShipToPlanet(Ship ship, Planet planet, Intent intent)
	{
		removeShip(ship);
		
		switch (intent)
		{
			case ACQUIRE:
				initiatePlanetAcquire(ship, planet);
				break;
			case DELIVER:
				initiatePlanetDelivery(ship, planet);
				break;
		}
	}
	
	public boolean contains(Ship ship)
	{
		return waitingShipsQueue.contains(ship);
	}
	
	public abstract void initiateDocking(Ship ship, Dock dock);
	public abstract void initiatePlanetDelivery(Ship ship, Planet planet);
	public abstract void initiatePlanetAcquire(Ship ship, Planet planet);
}
