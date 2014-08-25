package sk.hackcraft.spacestation;

import java.text.SimpleDateFormat;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class GameElapsedTimeLabel extends Actor
{
	private final long startTime;
	private final BitmapFont font;

	public GameElapsedTimeLabel(long startTime, BitmapFont font)
	{
		this.startTime = startTime;
		this.font = font;
		
		TextBounds bounds = font.getBounds("00:00");
		setSize(bounds.width, bounds.height);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		long diff = System.currentTimeMillis() - startTime;
		
		int totalSeconds = (int)(diff / 1000);
		
		int minutes = totalSeconds / 60;
		int seconds = totalSeconds % 60;
		
		String time = String.format("%02d:%02d", minutes, seconds);
		font.draw(batch, time, getX(), getY());
	}
}
