package sk.hackcraft.spacestation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.sun.javafx.tk.quantum.MasterTimer;

public class Interaction extends Actor
{
	private final SelectionBound selectedBound;
	private final SelectionBound possibleSelectBound;
	private final BitmapFont font;
	
	private Actor activeMaster;
	private InteractAction activeAction;
	
	private Map<Actor, Set<InteractAction>> actions = new HashMap<Actor, Set<Interaction.InteractAction>>();
	
	private final InputListener selectionListener = new InputListener()
	{
		public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
		{
			Actor target = event.getTarget();
			
			return actorTouched(target);
		}
	};
	
	public Interaction(SelectionBound selectedBound, SelectionBound possibleSelectBound, BitmapFont font)
	{
		this.selectedBound = selectedBound;
		this.possibleSelectBound = possibleSelectBound;
		this.font = font;
	}
	
	public boolean actorTouched(Actor actor)
	{
		System.out.println("Touched");
		
		if (activeMaster == null && actions.containsKey(actor))
		{
			initiateSelection(actor);
			return true;
		}
		else if (activeMaster == actor)
		{
			cancelSelection();
			return true;
		}
		else if (actions.containsKey(actor))
		{
			cancelSelection();
			initiateSelection(actor);
			return true;
		}
		else if (activeMaster != null)
		{
			if (actions.get(activeMaster).contains(actor))
			{
				InteractAction action = (InteractAction)actor;
				
				changeAction(action);
				
				if (action.execute())
				{
					cancelSelection();
					return true;
				}
			}
			else if (activeAction != null && activeAction.getTargets().contains(actor))
			{
				activeAction.executeWithTarget(actor);
				cancelSelection();
				return true;
			}
		}
		
		return false;
	}
	
	public void addSelectionListener(Actor actor)
	{
		actor.addListener(selectionListener);
	}
	
	public void addMasterActor(Actor actor)
	{
		actions.put(actor, new HashSet<Interaction.InteractAction>());
		addSelectionListener(actor);
	}
	
	public void addInteractAction(Actor masterActor, InteractAction action)
	{
		actions.get(masterActor).add(action);
		addSelectionListener(action);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		if (activeMaster != null)
		{
			selectedBound.draw(activeMaster, batch);
			
			float x = activeMaster.getX() + 30;
			float y = activeMaster.getY() - 10;
			float verticalOffset = font.getCapHeight() + 3;
			for (InteractAction action : actions.get(activeMaster))
			{
				font.draw(batch, action.getName(), x, y);
				y += verticalOffset;
			}
		}
		else
		{
			for (Actor actor : actions.keySet())
			{
				possibleSelectBound.draw(actor, batch);
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
	}
	
	private void cancelSelection()
	{
		activeMaster = null;
		activeAction = null;
	}
	
	private void changeAction(InteractAction action)
	{
		this.activeAction = action;
	}
	
	public static abstract class InteractAction extends Actor
	{
		private Set<Actor> targets;
		
		public InteractAction(String name)
		{
			this(name, new HashSet<Actor>());
		}
		
		public InteractAction(String name, Set<Actor> targets)
		{
			setName(name);
			this.targets = targets;
		}
		
		public abstract boolean isActive();
		
		public boolean execute() { return false; }
		public void executeWithTarget(Actor target) {}
		
		public Set<Actor> getTargets()
		{
			return targets;
		}
	}
}
