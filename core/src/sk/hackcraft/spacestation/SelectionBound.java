package sk.hackcraft.spacestation;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class SelectionBound
{
	private final TextureRegion corner;
	
	public SelectionBound(TextureRegion corner)
	{
		this.corner = corner;
	}
	
	public void draw(Actor actor, Batch batch)
	{
		float x[] = {
				actor.getX(),
				actor.getX() + actor.getWidth(),
				actor.getX() + actor.getWidth(),
				actor.getX()
		};
		
		float y[] = {
				actor.getY(),
				actor.getY(),
				actor.getY() + actor.getHeight(),
				actor.getY() + actor.getHeight()
		};
		
		boolean invertX[] = {
				false,
				true,
				true,
				false
		};
		
		boolean invertY[] = {
				true,
				true,
				false,
				false
		};
		
		float pX, pY;
		float pInvertX, pInvertY;
		
		for (int i = 0; i < 4; i++)
		{
			pX = x[i];
			pY = y[i];
			pInvertX = invertX[i] ? -1 : 1;
			pInvertY = invertY[i] ? -1 : 1;
			
			batch.draw(corner, pX, pY, pX, pY, actor.getWidth(), actor.getHeight(), pInvertX, pInvertY, 0);
		}
	}
}
