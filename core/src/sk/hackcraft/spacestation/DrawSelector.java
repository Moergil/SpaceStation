package sk.hackcraft.spacestation;

import sk.hackcraft.spacestation.Selectable.Selector;

import com.badlogic.gdx.graphics.g2d.Batch;

public abstract class DrawSelector implements Selector
{
	private boolean selected;

	public void setSelected(boolean selected)
	{
		this.selected = selected;
	}

	public boolean toggleSelected()
	{
		return selected = !selected;
	}
	
	public void draw(Batch batch)
	{
		if (selected)
		{
			drawSelected(batch);
		}
		else
		{
			drawUnselected(batch);
		}
	}
	
	public abstract void drawSelected(Batch batch);
	public abstract void drawUnselected(Batch batch);
}