package sk.hackcraft.spacestation;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Dock extends Actor
{
	private Ship dockedShip;
	private float dockingRelX, dockingRelY;
	
	public Dock()
	{
		setSize(75, 30);
		dockingRelX = 30;
	}
	
	public Vector2 getDockingPosition()
	{
		return new Vector2(getCenterX() + dockingRelX, getCenterY() + dockingRelY);
	}
	
	public void dockShip(Ship ship)
	{
		Vector2 shipDockingRelPos = ship.getDockingAdapterPosition();
		
		float newCX, newCY;
		newCX = ship.getCenterX() + shipDockingRelPos.x;
		newCY = ship.getCenterY() + shipDockingRelPos.y;
		ship.setCenterPosition(newCX, newCY);
		
		dockedShip = ship;
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
		
		float dockX = getCenterX() + dockingRelX;
		float dockY = getCenterY() + dockingRelY;
		shapes.circle(dockX, dockY, 5);
	}
}
