package sk.hackcraft.spacestation;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Dock extends Actor
{
	private Ship dockedShip;
	private Vector2 dockingAdapterPosition;
	
	public Dock()
	{
		setSize(75, 30);
		
		dockingAdapterPosition = new Vector2(15, 15);
	}
	
	public Vector2 getDockingAdapterPosition()
	{
		return dockingAdapterPosition;
	}
	
	public void dockShip(Ship ship)
	{
		Vector2 shipPosition = calculateShipDockingPosition(ship);
		ship.setPosition(shipPosition.x, shipPosition.y);
		
		dockedShip = ship;
	}
	
	public Vector2 calculateShipDockingPosition(Ship ship)
	{
		/*Vector2 shipDockingAdapterPosition = ship.getDockingAdapterPosition();
		Vector2 dockDockingAdapterPosition = getDockingAdapterPosition();
		
		return dockDockingAdapterPosition.add(shipDockingAdapterPosition);*/
		return new Vector2(getX(), getY());
	}
	
	public void undockShip()
	{
		dockedShip = null;
	}
	
	public Ship getDockedShip()
	{
		return dockedShip;
	}
	
	public boolean hasDockedShip()
	{
		return dockedShip != null;
	}
	
	@Override
	public void drawDebug(ShapeRenderer shapes)
	{
		shapes.setColor(Color.BLUE);
		shapes.rect(getX(), getY(), getWidth(), getHeight());
		
		float dockX = getX() + dockingAdapterPosition.x;
		float dockY = getY() + dockingAdapterPosition.y;
		shapes.circle(dockX, dockY, 5);
	}
}
