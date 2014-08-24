package sk.hackcraft.spacestation;

public interface Selectable
{
	Selector getSelector();
	
	public interface Selector
	{
		void setSelected(boolean selected);

		boolean toggleSelected();
	}
}
