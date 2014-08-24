package sk.hackcraft.spacestation;

public class Planet 
{
	private GoodsType type;
	private int storedGoods; //in tons
	
	public Planet(GoodsType type)
	{
		this.type = type;
		this.storedGoods = 0;
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
	
	
}
