package sk.hackcraft.spacestation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class Intro
{
	private String IntroText =  "Since the number of people start to expands to enormous size, they start to colony new planets. Due to limited resources of these planets an unlimited demands for new gods of its inhabitants a big galaxy war was upon the edge…  \n\n The only hope to save galaxy from destructive war was the global resource space station to delivery all needs to other planets. And they made you as the Captain...";
	private SpriteBatch batch;
    private BitmapFont font;
	
	public Intro()
	{
	}
	
	public void DisplayIntro()
	{
		SpriteBatch batch = new SpriteBatch();    
        BitmapFont font = new BitmapFont();
        font.setColor(Color.RED);
	}
	
	public void dispose() 
	{
        batch.dispose();
        font.dispose();
    }
	
	public void showIntro() 
	{        
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        batch.begin();
        font.draw(batch, IntroText, 200, 200);
        batch.end();
    }
}
