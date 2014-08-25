package sk.hackcraft.spacestation;

import java.util.Collections;
import java.util.Set;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class InteractAction extends Actor
{
	private final BitmapFont font = new BitmapFont();

	public void prepare(String name)
	{
		setName(name);
		
		setWidth(font.getBounds(name).width);
		setHeight(font.getBounds(name).height);
	}
	
	public abstract boolean isActive();
	
	public boolean execute() { return false; }
	public boolean executeWithTarget(Actor target) { return false; }
	
	public Set<? extends Actor> getTargets() { return Collections.emptySet(); }
	
	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		if (isActive())
		{
			font.draw(batch, getName(), getX(), getY() + getHeight());
		}
	}
}