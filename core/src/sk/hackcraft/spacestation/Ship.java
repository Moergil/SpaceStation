package sk.hackcraft.spacestation;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class Ship extends Actor implements Selectable
{
	public enum State
	{
		DOCKING,
		UNDOCKING,
		IDLE;
	}
	
	private Sprite sprite;

	private Vector2 dockingPortPosition;
	
	private CargoContainer cargoContainer;

	private DrawSelector drawSelector;
	private SelectionBound selectionBound;
	
	public Ship(Sprite sprite, Vector2 size, Vector2 dockingPortPosition, CargoContainer cargoContainer, SelectionBound selectionBound)
	{
		this.sprite = sprite;
		
		setSize(size.x, size.y);
		this.dockingPortPosition = dockingPortPosition;
		this.cargoContainer = cargoContainer;
		
		this.selectionBound = selectionBound;
		
		drawSelector = new DrawSelector()
		{
			@Override
			public void drawUnselected(Batch batch)
			{
			}
			
			@Override
			public void drawSelected(Batch batch)
			{
				Ship.this.selectionBound.draw(Ship.this, batch);
			}
		};
	}
	
	@Override
	public Selector getSelector()
	{
		return drawSelector;
	}
	
	public Vector2 getDockingAdapterPosition()
	{
		return dockingPortPosition;
	}
	
	public void arrive(Vector2 targetPosition, float duration)
	{
		addAction(Actions.moveTo(targetPosition.x, targetPosition.y, duration, Interpolation.exp5Out));
	}

	public void depart(Vector2 targetPosition, float duration)
	{
		addAction(Actions.moveTo(targetPosition.x, targetPosition.y, duration, Interpolation.exp5In));
	}
	
	public CargoContainer getCargoContainer()
	{
		return cargoContainer;
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);
	}

	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		sprite.setCenter(getCenterX(), getCenterY());
		sprite.draw(batch);
		
		drawSelector.draw(batch);
	}
	
	@Override
	public void drawDebug(ShapeRenderer shapes)
	{
		shapes.setColor(Color.ORANGE);
		shapes.circle(getX() + dockingPortPosition.x, getY() + dockingPortPosition.y, 5);
		
		shapes.setColor(Color.GREEN);
		shapes.rect(getX(), getY(), getWidth(), getHeight());
	}
}
