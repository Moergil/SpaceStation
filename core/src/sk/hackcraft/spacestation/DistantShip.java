package sk.hackcraft.spacestation;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class DistantShip extends Actor
{
	private final TextureRegion textureRegion;
	
	public DistantShip(TextureRegion region)
	{
		this.textureRegion = region;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		batch.draw(textureRegion, getX(), getY());
	}
}
