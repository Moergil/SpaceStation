package sk.hackcraft.spacestation;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Dock extends Actor
{
	private Ship dockedShip;
	private Vector2 dockingAdapterPosition;

	private boolean reserved;
	private boolean cargoTransfer;
	
	public Dock(int dockIndex)
	{
		setName("Dock no" + dockIndex);

		setSize(16, 16);
		
		dockingAdapterPosition = new Vector2(8, 8);
	}

	public Vector2 getDockingAdapterPosition()
	{
		return dockingAdapterPosition;
	}
	
	public void dockShip(Ship ship)
	{
		Vector2 shipPosition = calculateShipDockingPosition(ship);
		ship.setPosition(shipPosition.x, shipPosition.y);
		
		ship.showCargoGauge(true);
		
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
	
	public void setReserved()
	{
		reserved = true;
	}
	
	public void undockShip()
	{
		dockedShip.showCargoGauge(false);
		
		dockedShip = null;
		reserved = false;
	}
	
	public Ship getDockedShip()
	{
		return dockedShip;
	}
	
	public boolean hasDockedShip()
	{
		return dockedShip != null;
	}
	
	public boolean isFree()
	{
		return !reserved;
	}
	
	public void setCargoTransfer(boolean cargoTransfer)
	{
		this.cargoTransfer = cargoTransfer;
	}
	
	public boolean isTransferringCargo()
	{
		return cargoTransfer;
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
