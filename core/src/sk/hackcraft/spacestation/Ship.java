package sk.hackcraft.spacestation;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class Ship extends Actor implements Selectable
{
	public static final int SPRITE = 0;
	public static final int ENGINE_FWD = 1;
	public static final int ENGINE_RVS = 2;
	public static final int WIREFRAME = 3;
	
	public static final int[] DEFAULT_TEXTURE_DELAYS = {
		60, 5, 5, 60
	};
	
	public enum State
	{
		DOCKING,
		UNDOCKING,
		IDLE;
	}
	
	private State state;
	
	private TextureRegion[][] textures;
	private int[] texturePointers;
	private int[] textureDelays;
	private int frameCounter = 0;

	private Vector2 dockingPortPosition;
	
	private CargoContainer cargoContainer;

	private DrawSelector drawSelector;
	
	public Ship(Texture texture, Vector2 size, Vector2 dockingPortPosition, CargoContainer cargoContainer)
	{
		state = State.IDLE;
		
		initTextures(texture);
		
		setSize(size.x, size.y);
		this.dockingPortPosition = dockingPortPosition;
		this.cargoContainer = cargoContainer;
	}
	
	@Override
	public Selector getSelector()
	{
		return drawSelector;
	}
	
	public Vector2 getDockingAdapterPosition()
	{
		return dockingPortPosition;
	}
	
	public void arrive(Vector2 targetPosition, float duration)
	{
		state = State.DOCKING;
		addAction(Actions.moveTo(targetPosition.x, targetPosition.y, duration, Interpolation.exp5Out));
	}

	public void depart(Vector2 targetPosition, float duration)
	{
		state = State.UNDOCKING;
		addAction(Actions.moveTo(targetPosition.x, targetPosition.y, duration, Interpolation.exp5In));
	}
	
	public void setIdle()
	{
		state = State.IDLE;
	}
	
	public CargoContainer getCargoContainer()
	{
		return cargoContainer;
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);
	}

	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		incrementFrameCounter();
		batch.draw(getTexture(SPRITE), getX(), getY());
		
		switch (state)
		{
		case DOCKING:
			batch.draw(getTexture(ENGINE_FWD), getX(), getY());
			break;
		case UNDOCKING:
			batch.draw(getTexture(ENGINE_RVS), getX(), getY());
			break;
		}
		
		drawSelector.draw(batch);
	}
	
	@Override
	public void drawDebug(ShapeRenderer shapes)
	{
		shapes.setColor(Color.ORANGE);
		shapes.circle(getX() + dockingPortPosition.x, getY() + dockingPortPosition.y, 5);
		
		shapes.setColor(Color.GREEN);
		shapes.rect(getX(), getY(), getWidth(), getHeight());
	}
	
	public void setTextureDelays(int[] delays)
	{
		if (delays.length != 4)
			throw new IllegalArgumentException("Arr length should be 4. Call Epholl.");
		this.textureDelays = delays;
	}
	
	private void initTextures(Texture texture)
	{
		textures = new TextureRegion[4][];
		texturePointers = new int[4];
		initTextureDelays();
		
		int frameCount = texture.getWidth() / 54;
		
		for (int textureType = 0; textureType < 4; textureType++)
		{
			textures[textureType] = new TextureRegion[frameCount];
			
			for (int frame = 0; frame < frameCount; frame++)
			{
				textures[textureType][frame] = new TextureRegion(texture, frame*54, textureType*20, 54, 20);
			}
		}
	}
	
	private void initTextureDelays()
	{
		textureDelays = DEFAULT_TEXTURE_DELAYS;
	}
	
	private void incrementFrameCounter()
	{
		frameCounter++;
		
		for (int i = 0; i < textureDelays.length; i++)
		{
			if (frameCounter % textureDelays[i] == 0)
			{
				incrementTexturePointer(i);
			}
		}
	}
	
	private void incrementTexturePointer(int index)
	{
		texturePointers[index] = (texturePointers[index]+1) % textures[index].length;
	}
	
	private TextureRegion getTexture(int type)
	{
		return textures[type][texturePointers[type]];
	}
}
