package sk.hackcraft.spacestation;

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
//	private String IntroTextLines[] =  {"Since the number of people rapidly increase",", they must start to colony new planets.","Due to limited resources of these planets","an unlimited demands for new gods of its inhabitants","a big galaxy war was upon the edge… ", "The only hope to save galaxy from destructive war","was the global resource space station to delivery"," all needs to other planets.","And they made you as the Captain..."};
//	private String IntroText =  "SpaceStation";
	private SpriteBatch batch;
    private BitmapFont font;
    private int actualNum;
    final private int pageNum = 2;
    final private int duration = 30;
	private int backgroundX,backgroundY;
    
    private Sprite Background;
    private Sprite introStation;
    private Sprite[] introPages;
	
	public Intro()
	{
		backgroundX = 0;
		backgroundY = 0;
		actualNum = 0;
		batch = new SpriteBatch();    
		introPages = new Sprite[pageNum];
		
		for (int i = 0; i < pageNum; i++)
		{
			String fileName = "Intro/IntroText" + (i+1) + ".png";
			Texture texture = new Texture(Gdx.files.internal(fileName));
			
			TextureRegion region = new TextureRegion(texture, 0, 0, 400, 240);
			introPages[i] = new Sprite(region);
		}
//        font = new BitmapFont();
//        font.setColor(Color.BLUE);
		Texture texture = new Texture(Gdx.files.internal("Intro/Background.png"));
		TextureRegion region = new TextureRegion(texture, 0, 0, 400, 720);
		Background = new Sprite(region);
		
		
		texture = new Texture(Gdx.files.internal("sprite/station.png"));
		introStation = new Sprite(texture);
////		Background.setRegion(backgroundX,backgroundY,400,240);
		addAction(Actions.moveTo(0, 480, duration, Interpolation.sineIn));
	}
	
	public boolean hasNextPage()
	{
		return (actualNum < pageNum-1);
	}
	
	public boolean setNextPage()
	{
		if (hasNextPage())
		{
			actualNum++;
			return true;
		}
		else
			return false;
		
	}
	
	public void dispose() 
	{
        batch.dispose();
        font.dispose();
    }
	
	@Override
	public void act(float delta) 
	{
		super.act(delta);
	};
	
	@Override
	public void draw(Batch batch, float parentAlpha)
	{        
		batch.draw(Background, getX(), getY(), getOriginX() , getOriginY()+240, 400, 240, 1, 3, 0);
		batch.draw(introStation, getX() + 50, getY() - 460, getOriginX() , getOriginY(), introStation.getWidth(), introStation.getHeight(), 1, 1, 0);
		
		
		for (int i = 0; i < pageNum; i++)
			introPages[actualNum].draw(batch);
//			batch.draw(introPages[actualNum], getX(), getY(), getOriginX() , getOriginY(), 400, 240, 1, 1, 0);
    }
}
