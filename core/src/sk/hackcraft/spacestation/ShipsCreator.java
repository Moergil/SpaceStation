package sk.hackcraft.spacestation;

import java.util.EnumMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class ShipsCreator
{
	private Map<GoodsType, ShipDescription> descriptions = new EnumMap<GoodsType, ShipsCreator.ShipDescription>(GoodsType.class);
	private Map<GoodsType, TextureRegion> distantShips = new EnumMap<GoodsType, TextureRegion>(GoodsType.class);
	
	public ShipsCreator(SelectionBound selectionBound)
	{
		float shipWidth = 54;
		float shipHeight = 20;
		
		Texture waterTankerTexture = new Texture(Gdx.files.internal("sprite/ship1.png"));
		Texture hydrogenVesselTexture = new Texture(Gdx.files.internal("sprite/ship5.png"));
		Texture fertilizerContainerTexture = new Texture(Gdx.files.internal("sprite/ship3.png"));
		Texture metalsMoverTexture = new Texture(Gdx.files.internal("sprite/ship4.png"));
		Texture goodsContainerTexture = new Texture(Gdx.files.internal("sprite/ship2.png"));
		
		{
			ShipDescription d = new ShipDescription();
			d.name = "Water Tanker";
			d.texture = waterTankerTexture;
			d.size = new Vector2(shipWidth, shipHeight);
			d.dockingPortOffset = new Vector2(28, 11);
			d.cargoContainer = new CargoContainer(GoodsType.WATER, 10);
			
			descriptions.put(GoodsType.WATER, d);
		}
		
		{
			ShipDescription d = new ShipDescription();
			d.name = "Hydrogen Vessel";
			d.texture = hydrogenVesselTexture;
			d.size = new Vector2(shipWidth, shipHeight);
			d.dockingPortOffset = new Vector2(26, 9);
			d.cargoContainer = new CargoContainer(GoodsType.HYDROGEN, 10);
			
			descriptions.put(GoodsType.HYDROGEN, d);
		}
		
		{
			ShipDescription d = new ShipDescription();
			d.name = "Fertilizer Container";
			d.texture = fertilizerContainerTexture;
			d.size = new Vector2(shipWidth, shipHeight);
			d.dockingPortOffset = new Vector2(27, 11);
			d.cargoContainer = new CargoContainer(GoodsType.FERTILIZERS, 10);
			
			descriptions.put(GoodsType.FERTILIZERS, d);
		}
		
		{
			ShipDescription d = new ShipDescription();
			d.name = "Metals Mover";
			d.texture = metalsMoverTexture;
			d.size = new Vector2(shipWidth, shipHeight);
			d.dockingPortOffset = new Vector2(23, 9);
			d.cargoContainer = new CargoContainer(GoodsType.METALS, 10);
			
			descriptions.put(GoodsType.METALS, d);
		}
		
		{
			ShipDescription d = new ShipDescription();
			d.name = "Goods Container";
			d.texture = goodsContainerTexture;
			d.size = new Vector2(shipWidth, shipHeight);
			d.dockingPortOffset = new Vector2(14, 9);
			d.cargoContainer = new CargoContainer(GoodsType.GOODS, 10);
			
			descriptions.put(GoodsType.GOODS, d);
		}
	}

	public Ship createShip(GoodsType type)
	{
		ShipDescription d = descriptions.get(type);
		
		Ship ship = constructShip(d);
		
		ship.getCargoContainer().setCargoAmount(5);
		
		return ship;
	}
	
	public DistantShip createDistantShip(Ship ship)
	{
		//ShipDescription d = descriptions.get(type);
		
		/*Ship ship = constructShip(d);
		
		ship.getCargoContainer().setCargoAmount(5);
		
		return ship;*/
		return null;
	}
	
	private Ship constructShip(ShipDescription description)
	{
		return new Ship(
				description.name,
				description.texture,
				description.size,
				description.dockingPortOffset,
				description.cargoContainer
		);
	}
	
	private static class ShipDescription
	{
		public String name;
		public Texture texture;
		public Vector2 size;
		public Vector2 dockingPortOffset;
		public CargoContainer cargoContainer;
	}
}
