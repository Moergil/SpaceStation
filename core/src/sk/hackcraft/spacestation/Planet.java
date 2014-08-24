package sk.hackcraft.spacestation;

public class Planet 
{
	private GoodsType type;
	private double distance;
	private int storedGoods; //in tons
	
	public Planet(GoodsType type, double distance)
	{
		this.type = type;
		this.storedGoods = 0;
		this.distance = distance;
	}

	public GoodsType getType()
	{
		return type;
	}

	public void setType(GoodsType type)
	{
		this.type = type;
	}

	public int getStoredGoods()
	{
		return storedGoods;
	}

	public void setStoredGoods(int storedGoods)
	{
		this.storedGoods = storedGoods;
	}

	public double getDistance()
	{
		return distance;
	}
	
	
	
}
