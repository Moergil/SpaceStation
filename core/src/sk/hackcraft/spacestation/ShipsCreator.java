package sk.hackcraft.spacestation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class ShipsCreator
{
	private SelectionBound selectionBound;
	
	public ShipsCreator()
	{
		Texture texture = new Texture(Gdx.files.local("sprite/"));
		TextureRegion corner = new TextureRegion(texture, x, y, width, height);
		selectionBound = new SelectionBound(corner);
	}
	
	public Ship createGeneric()
	{
		Texture texture = new Texture(Gdx.files.internal("sprite/ship1.png"));
		
		TextureRegion region = new TextureRegion(texture, 0, 0, 54, 20);
		Sprite sprite = new Sprite(region);
		return new Ship(
				sprite,
				new Vector2(48, 16),
				new Vector2(10, 10),
				new CargoContainer(new CargoType(){}, 20),
				selectionBound);
	}
}
