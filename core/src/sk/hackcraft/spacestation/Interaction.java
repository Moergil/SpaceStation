package sk.hackcraft.spacestation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;

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
			
			if (actorTouched(target))
			{
				return true;
			}
			else
			{
				return super.touchDown(event, x, y, pointer, button);
			}
		}
	};
	
	public Interaction(Stage stage, SelectionBound selectedBound, SelectionBound possibleSelectBound)
	{
		this.stage = stage;
		this.selectedBound = selectedBound;
		this.possibleSelectBound = possibleSelectBound;
	}
	
	public void cancelActualInteraction()
	{
		cancelSelection();
	}
	
	public boolean hasActualInteraction()
	{
		return activeMaster != null;
	}
	
	public boolean actorTouched(Actor actor)
	{
		if (activeMaster == null && actions.containsKey(actor) && checks.get(actor).isActive())
		{
			System.out.println("New master selection");
			initiateSelection(actor);
			return true;
		}
		else if (activeMaster == actor)
		{
			System.out.println("Cancelled selection");
			cancelSelection();
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
				setActionsVisibility(activeMaster, false);
				
				if (action.execute())
				{
					System.out.println("Action immediate executed");
					cancelSelection();	
				}
				
				return true;
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
					cancelSelection();
					
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
		actor.removeListener(selectionListener);
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
	public void act(float delta)
	{
		super.act(delta);
		
		if (activeMaster != null)
		{
			updateInteractActionPositions();
		}
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		toFront();
		
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
		
		updateInteractActionPositions();
		
		setActionsVisibility(activeMaster, true);
	}
	
	private void updateInteractActionPositions()
	{
		float x = activeMaster.getX();
		float y = activeMaster.getY();
		float verticalOffset = 5;
		
		for (InteractAction action : actions.get(activeMaster))
		{
			action.setPosition(x, y);
			
			y -= verticalOffset + action.getHeight();
		}
	}
	
	private void cancelSelection()
	{
		if (activeMaster != null && actions.containsKey(activeMaster))
		{
			setActionsVisibility(activeMaster, false);
		}
		
		activeMaster = null;
		activeAction = null;
	}
	
	private void setActionsVisibility(Actor masterActor, boolean visible)
	{
		for (InteractAction action : actions.get(masterActor))
		{
			action.setVisible(visible);
		}
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
