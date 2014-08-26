package sk.hackcraft.spacestation;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class GameHint extends Actor
{
	private final String text =
			"Press [TAB] for view switch.\n"
			+ "\n"
			+ "Interact with ships, station and planets\n"
			+ "and manage resources.\n"
			+ "\n"
			+ "If resource on planet is depleted, you will lose.\n"
			+ "Keep resources on station for emergencies.\n"
			+ "\n"
			+ "Press [SPACE] to start.";
					
					
	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		BitmapFont font = SpaceStationGame.mainFont;
		font.drawMultiLine(batch, text, getX(), getY());
	}
}
