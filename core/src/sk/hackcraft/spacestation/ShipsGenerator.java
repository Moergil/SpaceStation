package sk.hackcraft.spacestation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class ShipsGenerator
{
	public Ship generate()
	{
		Texture texture = new Texture(Gdx.files.internal("sprite/dummyship.png"));
		Sprite sprite = new Sprite(texture);
		return new Ship(sprite, new Vector2(10, 10), new CargoContainer(0, 20));
	}
}
