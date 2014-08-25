package sk.hackcraft.spacestation;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class BackgroundStars extends Actor
{
	public static final float DURATION = 0.3f;
	public static final float Y = 0f;
	
	private List<BackgroundImage> sprites = new ArrayList<BackgroundImage>();
	private List<Float> offsetMultipliers = new ArrayList<Float>();
	
	public void addSprite(Sprite sprite, float offset)
	{
		sprites.add(new BackgroundImage(sprite));
		offsetMultipliers.add(offset);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		for (BackgroundImage sprite: sprites)
		{
			sprite.draw(batch, parentAlpha);
		}
	}
	
	public void gameViewMoved(float gameViewXOffset)
	{
		float reverseOffset = -gameViewXOffset;
		
		for (int i = 0; i < sprites.size(); i++)
		{
			float finalOffset = reverseOffset * offsetMultipliers.get(i);
			sprites.get(i).addAction(Actions.moveTo(finalOffset, Y, DURATION, Interpolation.exp5));
		}
	}
	
	public List<BackgroundImage> getBackgroundImageActors()
	{
		return sprites;
	}
}
