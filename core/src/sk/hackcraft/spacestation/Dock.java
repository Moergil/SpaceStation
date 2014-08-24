package sk.hackcraft.spacestation;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Dock extends Actor implements Selectable
{
	private Ship dockedShip;
	private Vector2 dockingAdapterPosition;

	private DrawSelector drawSelector;
	private SelectionBound selectionBound;
	
	public Dock(SelectionBound selectionBound)
	{
		this.selectionBound = selectionBound;
		
		setSize(75, 30);
		
		dockingAdapterPosition = new Vector2(15, 15);
		
		drawSelector = new DrawSelector()
		{
			@Override
			public void drawUnselected(Batch batch)
			{
			}
			
			@Override
			public void drawSelected(Batch batch)
			{
				Dock.this.selectionBound.draw(Dock.this, batch);
			}
		};
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
		Vector2 shipDockingAdapterPosition = ship.getDockingAdapterPosition();
		Vector2 dockDockingAdapterPosition = getDockingAdapterPosition();
		
		Vector2 position = new Vector2(getX(), getY())
		.add(dockDockingAdapterPosition)
		.sub(shipDockingAdapterPosition);
		
		return position;
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
	
	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		drawSelector.draw(batch);
	}

	@Override
	public Selector getSelector()
	{
		return drawSelector;
	}
}
