package sk.hackcraft.spacestation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class StationSmall extends Actor
{
	private Sprite sprite;
	
	private TextureRegion[] lights;
	private int lightsPointer;
	private float sum = 0;
	
	public StationSmall(Sprite sprite, Vector2 position)
	{
		this.sprite = sprite;
		setPosition(position.x, position.y);
		setSize(sprite.getWidth(), sprite.getHeight());
	}
	
	public void setLights(Texture lightsTexture)
	{
		this.lights = new TextureRegion[]{
				new TextureRegion(lightsTexture, 0, 0, 20, 41),
				new TextureRegion(lightsTexture, 20, 0, 20, 41),
				new TextureRegion(lightsTexture, 40, 0, 20, 41),
		};
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		sprite.setCenter(getCenterX(), getCenterY());
		sprite.draw(batch);
		
		if (lights != null)
		{
			batch.draw(lights[lightsPointer], getX(), getY());
			
			sum += parentAlpha;
			
			if (sum > 30f)
			{
				incrementLightsPointer();
				sum = 0;
			}
		}
	}
	
	private void incrementLightsPointer()
	{
		if (lights != null)
		{
			lightsPointer = (lightsPointer+1) % lights.length;
		}
	}
}
