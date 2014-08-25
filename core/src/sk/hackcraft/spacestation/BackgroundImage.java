package sk.hackcraft.spacestation;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class BackgroundImage extends Actor
{
	private Sprite sprite;
	
	public BackgroundImage(Sprite sprite)
	{
		this.sprite = sprite;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		batch.draw(sprite, getX(), getY());
	}
}
