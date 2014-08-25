package sk.hackcraft.spacestation;

public class CargoContainer
{
	private int amount, capacity;
	private GoodsType type;
	
	public CargoContainer(GoodsType type, int capacity)
	{
		this.type = type;
		this.capacity = capacity;
	}
	
	public GoodsType getCargoType()
	{
		return type;
	}
	
	public int getCargoAmount()
	{
		return amount;
	}
	
	public int getCargoCapacity()
	{
		return capacity;
	}
	
	public int getRemainingCapacity()
	{
		return capacity - amount;
	}

	public void modifyCargoAmount(int amount)
	{
		this.amount += amount;
	}
	
	public void setCargoAmount(int amount)
	{
		this.amount = amount;
	}
	
	public boolean transferUnitTo(CargoContainer target)
	{
		int capacity = target.getRemainingCapacity();
		
		if (capacity > 0 && getCargoAmount() > 0)
		{
			modifyCargoAmount(-1);
			target.modifyCargoAmount(1);
			return true;
		}
		else
		{
			return false;
		}
	}
}
