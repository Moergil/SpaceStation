package sk.hackcraft.spacestation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;


public class Intro extends Actor
{
	private String IntroTextLines[] =  {"Since the number of people rapidly increase",", they must start to colony new planets.","Due to limited resources of these planets","an unlimited demands for new gods of its inhabitants","a big galaxy war was upon the edge… ", "The only hope to save galaxy from destructive war","was the global resource space station to delivery"," all needs to other planets.","And they made you as the Captain..."};
//	private String IntroText =  "SpaceStation";
	private SpriteBatch batch;
    private BitmapFont font;
	
	public Intro()
	{
		
		batch = new SpriteBatch();    
        font = new BitmapFont();
        font.setColor(Color.BLUE);
		
	}
	
	public void dispose() 
	{
        batch.dispose();
        font.dispose();
    }
	
	@Override
	public void act(float delta) 
	{
		
	};
	
	@Override
	public void draw(Batch batch, float parentAlpha)
	{        
//		sprite.setCenter(getCenterX(), getCenterY());
//		sprite.draw(batch);
//		
//        Gdx.gl.glClearColor(1, 1, 1, 1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        //batch.begin();
		for (int i = 0; i < IntroTextLines.length; i++)
        font.draw(batch, IntroTextLines[i], 20, 200 - 15*i);
        //batch.end();
    }
}
