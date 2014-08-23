package sk.hackcraft.spacestation;

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
	
	public Ship(Sprite sprite, Vector2 dockingPortPosition)
	{
		this.sprite = sprite;
		
		setSize(sprite.getWidth(), sprite.getHeight());
		this.dockingPortPosition = dockingPortPosition;
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
		shapes.circle(getCenterX() + dockingPortPosition.x, getCenterY() + dockingPortPosition.y, 5);
		
		shapes.setColor(Color.GREEN);
		shapes.rect(getX(), getY(), getWidth(), getHeight());
	}
}
