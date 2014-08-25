package sk.hackcraft.spacestation;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class GameOverScreen extends Actor
{
	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		BitmapFont font = SpaceStationGame.mainFont;

		font.draw(batch, "Some of the planets run out of supplies.", 100, 150);
		font.draw(batch, "Game Over", 200, 130);
	}
}
