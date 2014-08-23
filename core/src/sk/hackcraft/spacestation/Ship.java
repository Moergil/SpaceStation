package sk.hackcraft.spacestation;

import java.util.HashSet;
import java.util.Set;

import javax.swing.plaf.basic.BasicToolBarUI.DockingListener;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;

public class Ship extends Actor
{
	private Sprite sprite;

	private Vector2 dockingPortPosition;
	
	private CargoContainer cargoContainer;
	
	public Ship(Sprite sprite, Vector2 dockingPortPosition, CargoContainer cargoContainer)
	{
		this.sprite = sprite;
		
		setSize(50, 20);
		this.dockingPortPosition = dockingPortPosition;
		this.cargoContainer = cargoContainer;
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
