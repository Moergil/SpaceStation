package sk.hackcraft.spacestation;

/**
 * Interface for interacting view view to station. It allows to add ship to view
 * (so it will be visible on the waiting ships queue) and register callback for
 * undocked ship.
 */
public interface StationView
{
	void shipArrived(Ship ship);

	void setListener(StationViewListener listener);

	public interface StationViewListener
	{
		void shipDeparted(Ship ship);
	}
}
