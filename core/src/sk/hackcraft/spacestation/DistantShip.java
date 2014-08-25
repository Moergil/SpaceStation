package sk.hackcraft.spacestation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class DistantShip extends Actor
{
	private final Texture texture;
	
	public DistantShip(Texture region)
	{
		this.texture = region;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		batch.draw(texture, getX(), getY());
	}
}
