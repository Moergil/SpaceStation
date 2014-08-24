package sk.hackcraft.spacestation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.sun.javafx.tk.quantum.MasterTimer;

public class Interaction extends Actor
{
	private final Stage stage;
	
	private final SelectionBound selectedBound;
	private final SelectionBound possibleSelectBound;
	
	private Actor activeMaster;
	private InteractAction activeAction;
	
	private Map<Actor, List<InteractAction>> actions = new HashMap<Actor, List<InteractAction>>();
	private Map<Actor, ActiveCheck> checks = new HashMap<Actor, Interaction.ActiveCheck>();
	
	private final InputListener selectionListener = new InputListener()
	{
		public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
		{
			Actor target = event.getTarget();
			
			return actorTouched(target);
		}
	};
	
	public Interaction(Stage stage, SelectionBound selectedBound, SelectionBound possibleSelectBound)
	{
		this.stage = stage;
		this.selectedBound = selectedBound;
		this.possibleSelectBound = possibleSelectBound;
	}
	
	public boolean actorTouched(Actor actor)
	{
		if (activeMaster == null && actions.containsKey(actor))
		{
			System.out.println("New master selection");
			initiateSelection(actor);
			return true;
		}
		else if (activeMaster == actor)
		{
			System.out.println("Cancelled selection");
			cancelSelection(false);
			return true;
		}
		else if (activeMaster != null)
		{
			if (actions.get(activeMaster).contains(actor))
			{
				InteractAction action = (InteractAction)actor;
				
				if (!action.isActive())
				{
					return false;
				}
				
				System.out.println("Changed action: " + action.getName());
				changeAction(action);
				
				if (action.execute())
				{
					System.out.println("Action immediate executed");
					cancelSelection(true);
					
					return true;
				}
			}
			else if (activeAction != null && activeAction.getTargets().contains(actor))
			{
				if (!activeAction.isActive())
				{
					return false;
				}
				
				if (activeAction.executeWithTarget(actor))
				{
					System.out.println("Action executed with target " + actor.getName());
					cancelSelection(true);
					
					return true;
				}
				
				return false;
			}
		}
		
		return false;
	}
	
	public void addSelectionListener(Actor actor)
	{
		actor.addListener(selectionListener);
	}
	
	public void addMasterActor(Actor actor, ActiveCheck activeCheck)
	{
		actions.put(actor, new ArrayList<InteractAction>());
		checks.put(actor, activeCheck);
		addSelectionListener(actor);
	}
	
	public void removeMasterActor(Actor actor)
	{
		actions.remove(actor);
		actor.removeCaptureListener(selectionListener);
	}
	
	public void addInteractAction(Actor masterActor, String title, InteractAction action)
	{
		actions.get(masterActor).add(action);
		
		action.prepare(title);
		action.setVisible(false);
		
		addSelectionListener(action);
		
		stage.addActor(action);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		if (activeMaster != null)
		{
			selectedBound.draw(activeMaster, batch);
		}
		else
		{
			for (Actor actor : actions.keySet())
			{
				if (checks.get(actor).isActive())
				{
					possibleSelectBound.draw(actor, batch);
				}
			}
		}
		
		if (activeAction != null)
		{
			for (Actor actor : activeAction.getTargets())
			{
				possibleSelectBound.draw(actor, batch);
			}
		}
	}
	
	private void initiateSelection(Actor masterActor)
	{
		this.activeMaster = masterActor;
		
		float x = 0;
		float y = 0;
		float verticalOffset = 5;
		
		/*if (actions.get(masterActor).size() == 1)
		{
			if (actions.get(masterActor).iterator().next().execute())
			{
				cancelSelection();
			}
		}
		else
		{*/
			for (InteractAction action : actions.get(masterActor))
			{
				action.setPosition(x, y);
				action.setVisible(true);
				
				y += verticalOffset + action.getHeight();
			}
		//}
	}
	
	private void cancelSelection(boolean actionExecuted)
	{
		if (activeMaster != null && actions.containsKey(activeMaster))
		{
			for (InteractAction action : actions.get(activeMaster))
			{
				action.setVisible(false);
			}
		}
		
		if (actionExecuted && activeAction != null && activeAction.isOneTime())
		{
			removeMasterActor(activeMaster);
		}
		
		activeMaster = null;
		activeAction = null;
	}
	
	private void changeAction(InteractAction action)
	{
		this.activeAction = action;
	}
	
	public interface ActiveCheck
	{
		boolean isActive();
	}
}
