package sk.hackcraft.spacestation;

import java.util.Date;

public class PlanetTask implements Comparable
{
	private GoodsType type;
	private int amount;
	private Date fromTime;
	private Date toTime;
	

	public PlanetTask(GoodsType type, int amount, Date fromTime, Date toTime)
	{
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
	
	
	
}
