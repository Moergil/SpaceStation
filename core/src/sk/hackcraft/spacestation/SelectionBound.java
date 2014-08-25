package sk.hackcraft.spacestation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class SelectionBound
{
	private float stateTime;

	private final TextureRegion corners1[], corners2[];
	
	private Animation animations[];
	
	public SelectionBound(Texture cornersAtlas)
	{
		int s = 6;

		corners1 = new TextureRegion[]{
				new TextureRegion(cornersAtlas, 0, 0, s, s),
				new TextureRegion(cornersAtlas, s, 0, s, s),
				new TextureRegion(cornersAtlas, s*2, 0, s, s),
				new TextureRegion(cornersAtlas, s*3, 0, s, s)
		};
		
		corners2 = new TextureRegion[]{
				new TextureRegion(cornersAtlas, 0, s, s, s),
				new TextureRegion(cornersAtlas, s, s, s, s),
				new TextureRegion(cornersAtlas, s*2, s, s, s),
				new TextureRegion(cornersAtlas, s*3, s, s, s)
		};
		
		animations = new Animation[]{
			new Animation(2f, corners1[0], corners2[0]),
			new Animation(2f, corners1[1], corners2[1]),
			new Animation(2f, corners1[2], corners2[2]),
			new Animation(2f, corners1[3], corners2[3]),
		};
	}
	
	public void draw(Actor actor, Batch batch)
	{		
		float x = actor.getX();
		float y = actor.getY();
		
		stateTime += Gdx.graphics.getDeltaTime();

		batch.draw(animations[0].getKeyFrame(stateTime, true), x, y);
		
		x += actor.getWidth();
		batch.draw(animations[1].getKeyFrame(stateTime, true), x, y);
		
		y += actor.getHeight();
		batch.draw(animations[2].getKeyFrame(stateTime, true), x, y);
		
		x -= actor.getWidth();
		batch.draw(animations[3].getKeyFrame(stateTime, true), x, y);
	}
}