package sk.hackcraft.spacestation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class SelectionBound
{
	private final int MAX_OFFSET = 5;
	
	private int tick;
	private int actualOffset = MAX_OFFSET;

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
			actualOffset--;
			
			if (actualOffset < 4)
			{
				actualOffset = MAX_OFFSET;
			}
		}
		
		float x, y;
		
		x = actor.getX() - actualOffset;
		y = actor.getY() - actualOffset;
		
		batch.draw(corners[0], x, y);
		
		x += actor.getWidth() + actualOffset;
		batch.draw(corners[1], x, y);
		
		y += actor.getHeight() + actualOffset;
		batch.draw(corners[2], x, y);
		
		x -= actor.getWidth() + actualOffset;
		batch.draw(corners[3], x, y);
	}
}
