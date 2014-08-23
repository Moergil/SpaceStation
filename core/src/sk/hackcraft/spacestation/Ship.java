package sk.hackcraft.spacestation;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;

public class Ship extends Actor
{
	private Sprite sprite;
	
	public Ship(Sprite sprite)
	{
		this.sprite = sprite;
	}
	
	public void setTargetPosition(Vector2 targetPosition, float duration)
	{
		MoveToAction action = new MoveToAction();
		action.setPosition(targetPosition.x, targetPosition.y);
		action.setDuration(duration);
		addAction(action);
	}
	
	@Override
	public void act(float delta)
	{
		super.act(delta);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		sprite.setCenter(getX(), getY());
		sprite.draw(batch);
	}
}
