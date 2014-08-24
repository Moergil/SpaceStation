package sk.hackcraft.spacestation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class ShipsCreator
{
	private SelectionBound selectionBound;
	private int i = 0;
	
	public ShipsCreator(SelectionBound selectionBound)
	{
		this.selectionBound = selectionBound;
	}
	
	public Ship createGeneric()
	{
		Texture texture = new Texture(Gdx.files.internal("sprite/ship5.png"));


		Ship ship = new Ship(
				"S" + ++i,
				texture,
				new Vector2(48, 16),
				new Vector2(10, 10),
				new CargoContainer(GoodsType.FOOD, 20));
		
		ship.getCargoContainer().setCargoAmount(10);
		
		return ship;
	}
}
