package sk.hackcraft.spacestation;

import java.util.Date;

public class PlanetTask 
{
	private Planet planet;
	private GoodsType type;
	private int amount;

	

	public PlanetTask(Planet planet,GoodsType type, int amount)
	{
		this.planet = planet;
		this.type = type;
		this.amount = amount;
	}


	public Planet getPlanet()
	{
		return planet;
	}


	public void setPlanet(Planet planet)
	{
		this.planet = planet;
	}


	public GoodsType getType()
	{
		return type;
	}


	public void setType(GoodsType type)
	{
		this.type = type;
	}


	public int getAmount()
	{
		return amount;
	}


	public void setAmount(int amount)
	{
		this.amount = amount;
	}


	
}
