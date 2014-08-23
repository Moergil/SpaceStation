package sk.hackcraft.spacestation;

public class CargoContainer
{
	private int amount, capacity;
	private CargoType type;
	
	public CargoContainer(CargoType type, int capacity)
	{
		this.type = type;
		this.capacity = capacity;
	}
	
	public CargoType getCargoType()
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
}
