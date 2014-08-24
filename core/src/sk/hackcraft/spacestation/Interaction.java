package sk.hackcraft.spacestation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class Interaction
{
	private Actor activeMaster, activeIntent;

	private Map<Actor, Map<Actor, Set<Actor>>> data = new HashMap<Actor, Map<Actor, Set<Actor>>>();
	
	private Map<Actor, InteractAction> interactActions = new HashMap<Actor, Interaction.InteractAction>();
	
	public boolean isSelectable(Actor actor)
	{
		if (activeMaster == null && data.containsKey(actor))
		{
			return true;
		}
		else
		{
			if (activeIntent == null && data.get(activeMaster).containsKey(actor))
			{
				return true;
			}
			else
			{
				if (data.get(activeMaster).get(activeIntent).contains(actor))
				{
					return true;
				}
			}
		}

		return false;
	}
	
	public void selectMaster(Actor actor)
	{
		this.activeMaster = actor;
	}
	
	public void selectIntent(Actor intent)
	{
		this.activeIntent = intent;
	}
	
	public void cancelSelection()
	{
		activeMaster = null;
		activeIntent = null;
	}
	
	public Actor getActiveMaster()
	{
		return activeMaster;
	}
	
	public boolean hasActiveMaster()
	{
		return activeMaster != null;
	}
	
	public Actor getActiveIntent()
	{
		return activeIntent;
	}
	
	public boolean hasActiveIntent()
	{
		return activeIntent != null;
	}
	
	public void addMaster(Actor master)
	{
		data.put(master, new HashMap<Actor, Set<Actor>>());
	}
	
	public void setIntentInteractAction(Actor intent, InteractAction action)
	{
		interactActions.put(intent, action);
	}
	
	public void setSlavesSet(Actor master, Actor intent, Set<Actor> slavesSet)
	{
		data.get(master).put(intent, slavesSet);
	}
	
	public Set<Actor> getIntentsSet(Actor master)
	{
		return data.get(master).keySet();
	}
	
	public Set<Actor> getSlavesSet(Actor master, Actor intent)
	{
		return data.get(master).get(intent);
	}
	
	public Set<? extends Actor> getSelectableActors()
	{
		if (activeMaster == null)
		{
			return data.keySet();
		}
		else
		{
			if (activeIntent == null)
			{
				return data.get(activeMaster).keySet();
			}
			else
			{
				return data.get(activeMaster).get(activeIntent);
			}
		}
	}
	
	public void selectActor(Actor actor)
	{
		if (data.containsKey(actor))
		{
			cancelSelection();
			activeMaster = actor;
		}
		else if (activeMaster != null && data.get(activeMaster).containsKey(actor))
		{
			activeIntent = actor;
		}
		else if (activeMaster != null && activeIntent != null && data.get(activeMaster).get(activeIntent).contains(actor))
		{
			interactActions.get(activeIntent).interact(activeMaster, actor);
		}
	}
	
	public interface InteractAction
	{
		void interact(Actor master, Actor slave);
	}
}
