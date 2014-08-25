package sk.hackcraft.spacestation;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class Intro extends Actor
{
	private BitmapFont font;

	public static final int start = 1, center = 31, end = 1;
	public static final int duration = start + center + end;

	private boolean showText;

	private Sprite background;
	private Sprite introStation;
	
	private String lines[] = {
		"With the amount of people rapidly",
		"increasing, there was no other hope",
		"than to colonize other planets.",
		"The demand for resources grew rapidly,",
		"not even new colonies could extinguish",
		"this thirst. The inhabitants of",
		"this galaxy were brought upon the edge...",
		"",
		"The only way to save the galaxy from",
		"a devastating war was a global",
		"resource storage and delivery station",
		"to cover all planet's needs. You",
		"are to gain control of this station",
		"and secure a steady trade between the",
		"colonies as their only connection."
	};

	/**
	 * With the amount of people rapidly increasing there was no other hope,
	 * than to colonize other planets. The demand for resources grew rapidly,
	 * not even new colonies could extinguish this thirst. The inhabitants of
	 * this galaxy were brought upon the edge�
	 * 
	 * The only way to save the galaxy from devastating war was a global
	 * resource storage and delivery station to cover all planet�s needs. You
	 * are to gain control of this station and secure steady trade between the
	 * colonies as their only connection.
	 */

	public Intro()
	{
		font = SpaceStationGame.mainFont;

		showText = false;

		Texture texture = new Texture(Gdx.files.internal("Intro/Background.png"));
		TextureRegion region = new TextureRegion(texture, 0, 0, 400, 720);
		background = new Sprite(region);

		texture = new Texture(Gdx.files.internal("sprite/station.png"));
		introStation = new Sprite(texture);

		addAction(Actions.sequence(Actions.delay(start), Actions.moveTo(0, 480, center), Actions.delay(end)));
	}

	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		batch.draw(background, getX(), getY(), getOriginX(), getOriginY() + 240, 400, 240, 1, 3, 0);
		batch.draw(introStation, getX() + 50, getY() - 470, getOriginX(), getOriginY(), introStation.getWidth(), introStation.getHeight(), 1, 1, 0);
		
		float y = 0;
		float yOffset = font.getCapHeight() + 7;
		for (String line : lines)
		{
			font.draw(batch, line, 20, y + getY());
			y -= yOffset;
		}
	}
}
