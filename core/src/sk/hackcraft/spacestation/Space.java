package sk.hackcraft.spacestation;

import sk.hackcraft.spacestation.StationView.StationViewListener;

public class Space 
{
	private StationView stationView;
	
	public Space(StationView stationView)
	{
		stationView.setListener(new StationViewListener()
		{
			
			@Override
			public void shipDeparted(Ship ship) 
			{
				
				
			}
		});
	}
}
