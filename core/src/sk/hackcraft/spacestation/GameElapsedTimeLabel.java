package sk.hackcraft.spacestation;

import java.text.SimpleDateFormat;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class GameElapsedTimeLabel extends Actor
{
	private long startTime = -1;
	private final BitmapFont font;

	public GameElapsedTimeLabel(BitmapFont font)
	{
		this.font = font;
		
		TextBounds bounds = font.getBounds("00:00");
		setSize(bounds.width, bounds.height);
	}
	
	public void start()
	{
		startTime = System.currentTimeMillis();
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		if (startTime == -1)
		{
			return;
		}
		
		long diff = System.currentTimeMillis() - startTime;
		
		int totalSeconds = (int)(diff / 1000);
		
		int minutes = totalSeconds / 60;
		int seconds = totalSeconds % 60;
		
		String time = String.format("%02d:%02d", minutes, seconds);
		font.draw(batch, time, getX(), getY());
	}
}
