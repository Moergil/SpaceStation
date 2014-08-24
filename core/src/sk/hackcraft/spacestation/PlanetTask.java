package sk.hackcraft.spacestation;

import java.util.Date;

public class PlanetTask implements Comparable
{
	private Planet planet;
	private GoodsType type;
	private int amount;
	private Date fromTime;
	private Date toTime;
	

	public PlanetTask(Planet planet,GoodsType type, int amount, Date fromTime, Date toTime)
	{
		this.planet = planet;
		this.type = type;
		this.amount = amount;
		this.fromTime = fromTime;
		this.toTime = toTime;
	}


	@Override
	public int compareTo(Object arg0)
	{
		return this.toTime.compareTo(toTime);
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


	public Date getFromTime()
	{
		return fromTime;
	}


	public void setFromTime(Date fromTime)
	{
		this.fromTime = fromTime;
	}


	public Date getToTime()
	{
		return toTime;
	}


	public void setToTime(Date toTime)
	{
		this.toTime = toTime;
	}
	
	
	
	
	
}
