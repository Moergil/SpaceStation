package sk.hackcraft.spacestation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class SelectionBound
{
	private int tick;
	private int wobble;

	private final TextureRegion corners[];
	
	public SelectionBound(Texture cornersAtlas)
	{
		corners = new TextureRegion[]{
				new TextureRegion(cornersAtlas, 0, 0, 4, 4),
				new TextureRegion(cornersAtlas, 4, 0, 4, 4),
				new TextureRegion(cornersAtlas, 8, 0, 4, 4),
				new TextureRegion(cornersAtlas, 12, 0, 4, 4)
		};
	}
	
	public void draw(Actor actor, Batch batch)
	{
		tick++;
		
		if (tick % 30 == 0)
		{
			wobble--;
			
			if (wobble < 0)
			{
				wobble = 3;
			}
		}
		
		float offset = 5;
		float x, y;
		
		x = actor.getX() - offset;
		y = actor.getY() - offset;
		
		batch.draw(corners[0], x - wobble, y);
		
		x += actor.getWidth() + offset;
		batch.draw(corners[1], x + wobble, y);
		
		y += actor.getHeight() + offset;
		batch.draw(corners[2], x + wobble, y);
		
		x -= actor.getWidth() + offset;
		batch.draw(corners[3], x - wobble, y);
	}
}