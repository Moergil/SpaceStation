package sk.hackcraft.spacestation;

import java.text.SimpleDateFormat;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ScoreLabel extends Actor
{
	private int score;
	private final BitmapFont font;

	public ScoreLabel(BitmapFont font)
	{
		this.font = font;
		
		setHeight(font.getCapHeight());
	}
	
	public void setScore(int score)
	{
		this.score = score;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		font.draw(batch, Integer.toString(score), getX(), getY());
	}
}
