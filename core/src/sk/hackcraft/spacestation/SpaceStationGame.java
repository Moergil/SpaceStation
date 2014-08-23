package sk.hackcraft.spacestation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import sk.hackcraft.spacestation.Selectable.Selector;
import sk.hackcraft.spacestation.Planet;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class SpaceStationGame extends ApplicationAdapter
{
	private Random random;
	
	private Stage gameStage, hudStage;
	private GameView actualGameView;

	private ShipsCreator shipsGenerator;
	private Timer timer;


	private List<Dock> docks = new ArrayList<Dock>();
	private List<Planet> planets = new ArrayList<Planet>();

	
	private Dock selectedDock;
	
	private SelectionBound selectionBound;
	
	private ShipsQueueMenu shipsQueueMenu;
	
	private SelectionListener selectionListener = new SelectionListener();

	private Music mp3Intro;
	
	private Station station;

	@Override
	public void create()
	{
		random = new Random();
		timer = new Timer();

		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		gameStage = new Stage(new FitViewport(400, 240));
		inputMultiplexer.addProcessor(gameStage);
		
		hudStage = new Stage(new FitViewport(400, 240));
		inputMultiplexer.addProcessor(hudStage);
		
		Gdx.input.setInputProcessor(inputMultiplexer);
		
		runIntro();
//		runGame();
	}
	
	private void runIntro()
	{
		// setting up intro
		final Intro intro = new Intro();
		intro.setPosition(0, 0);
		
		mp3Intro = Gdx.audio.newMusic(Gdx.files.internal("sounds/intro.mp3"));
		mp3Intro.play();
			
		hudStage.addActor(intro);

		timer.scheduleTask(new Timer.Task()
		{
			@Override
			public void run()
			{
				cleanupIntro(intro);
			}
		}, 5,5);
		
		hudStage.addListener(new InputListener()
		{
			@Override
			public boolean keyDown(InputEvent event, int keycode)
			{
				cleanupIntro(intro);
				return true;
			}
			
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
			{
				cleanupIntro(intro);
				return true;
			}
		});
	}
	
	private void cleanupIntro(Intro intro)
	{
		if (intro.setNextPage())
		{
			return;
		}
		
		timer.clear();
		mp3Intro.stop();
		
		intro.clear();
		intro = null;
		
		timer.scheduleTask(new Timer.Task()
		{
			@Override
			public void run()
			{
				hudStage.clear();
				runGame();
			}
		}, 0);
	}
	
	public void runGame()
	{
		Texture stationTexture = new Texture(Gdx.files.internal("sprite/station.png"));
		Sprite stationSprite = new Sprite(stationTexture);
		station = new Station(stationSprite);
		
		station.setPosition(50, 20);
		gameStage.addActor(station);
		
		Texture cornersAtlas = new Texture(Gdx.files.local("sprite/selector_corner.png"));
		selectionBound = new SelectionBound(cornersAtlas);
		
		// actual view of the player
		actualGameView = GameView.DOCKS;

		gameStage.addListener(new InputListener()
		{
			@Override
			public boolean keyDown(InputEvent event, int keycode)
			{
				if (keycode == Input.Keys.A)
				{
					nextGameView();
					return true;
				}
				
				if (keycode == Input.Keys.B)
				{
					for (Dock dock : station.getDocks())
					{
						if (dock.hasDockedShip())
						{
							final Ship ship = dock.getDockedShip();
							
							dock.undockShip();
							
							ship.depart(new Vector2(450, dock.getY()), 5);
							
							timer.scheduleTask(new Timer.Task()
							{
								@Override
								public void run()
								{
									ship.remove();
								}
							}, 5);
						}
					}
	
					return true;
				}
				
				if (keycode == Input.Keys.S)
				{
					addShip();

					return true;
				}
		
				return false;
			}
		});
		
		setInstantGameView(GameView.DOCKS);
		
		// debugging
		//gameStage.setDebugAll(true);
		
		float positionY[] = {110, 84, 58, 32};
		
		for (int i = 0; i < 4; i++)
		{
			Dock dock = new Dock(selectionBound);
			dock.setPosition(113, positionY[i]);
			
			station.addDock(dock);
			
			registerSelectionListener(dock);
			
			gameStage.addActor(dock);
		}
		
		//generating planets
		planets.add(new Planet(GoodsType.FOOD));
		planets.add(new Planet(GoodsType.ORE));
		planets.add(new Planet(GoodsType.MEDICINE));
		planets.add(new Planet(GoodsType.MATERIAL));
		planets.add(new Planet(GoodsType.ELECTRONICS));
		
		// ships generation		
		shipsGenerator = new ShipsCreator(selectionBound);
		
		shipsQueueMenu = new ShipsQueueMenu()
		{
			@Override
			public void initiateDocking(Ship ship, Dock dock)
			{
				ship.remove();
				
				gameStage.addActor(ship);
				ship.setPosition(450, dock.getY());
				
				flyShipToDock(ship, dock);
			}
		};

		hudStage.addActor(shipsQueueMenu);
		
		{
			float x = hudStage.getWidth() - shipsQueueMenu.getWidth();
			shipsQueueMenu.setPosition(x, 0);
			
			shipsQueueMenu.matchHeightToStage();
		}
		
		addShip();
		addShip();
		addShip();
	}
	
	private void registerSelectionListener(final Actor actor)
	{
		actor.addListener(selectionListener);
	}

	private void addShip()
	{
		final Ship ship = shipsGenerator.createGeneric();

		hudStage.addActor(ship);
		shipsQueueMenu.queueShip(ship);
		
		registerSelectionListener(ship);
	}
	
	@Override
	public void resize(int width, int height)
	{
		gameStage.getViewport().update(width, height, true);
		hudStage.getViewport().update(width, height, true);
	}

	@Override
	public void render()
	{
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		gameStage.act(Gdx.graphics.getDeltaTime());
		hudStage.act(Gdx.graphics.getDeltaTime());
		
		gameStage.draw();
		hudStage.draw();
	}

	private enum GameView
	{
		DOCKS(0),
		MAP(-400);
		
		private float offset;
		
		private GameView(float offset)
		{
			this.offset = offset;
		}
		
		public float getOffset()
		{
			return offset;
		}
	}

	private void nextGameView()
	{
		GameView nextGameView;
		
		switch (actualGameView)
		{

			case MAP:
				nextGameView = GameView.DOCKS;
				break;
			case DOCKS:
				nextGameView = GameView.MAP;
				break;
			default:
				return;
		}
		
		setGameView(nextGameView);
	}
	
	private void setGameView(GameView gameView)
	{
		float y = 0;
		float duration = 0.3f;
		System.out.println(gameView.getOffset());
		MoveToAction action = Actions.moveTo(gameView.getOffset(), y, duration, Interpolation.exp5);
		gameStage.addAction(action);
		actualGameView = gameView;
	}
	
	private void setInstantGameView(GameView gameView)
	{
		float y = 0;
		float duration = 0.0f;
		MoveToAction action = Actions.moveTo(gameView.getOffset(), y, duration);
		gameStage.addAction(action);
		actualGameView = gameView;
	}
	
	private void flyShipToDock(final Ship ship, final Dock dock)
	{
		Vector2 dockingPosition = dock.calculateShipDockingPosition(ship);
	
		float flyDuration = 5;
		ship.arrive(dockingPosition, flyDuration);
		
		timer.scheduleTask(new Timer.Task()
		{
			@Override
			public void run()
			{
				dock.dockShip(ship);
			}
		}, 6);
	}
	
	private void releaseShipFromDock(final Dock dock)
	{
		timer.scheduleTask(new Timer.Task()
		{
			@Override
			public void run()
			{
				Ship ship = dock.getDockedShip();
				dock.undockShip();
				
				Vector2 targetPosition = new Vector2(450, ship.getY());
				ship.depart(targetPosition, 5);
			}
		}, 1);
	}
	
	private class SelectionListener extends InputListener
	{
		@Override
		public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
		{
			Actor target = event.getTarget();
			
			if (target instanceof Dock)
			{
				Dock dock = (Dock)target;
				
				if (selectedDock != null)
				{
					selectedDock.getSelector().setSelected(false);
				}
				
				selectedDock = dock;
				selectedDock.getSelector().setSelected(true);
				
				return true;
			}
			else
			{
				if (selectedDock == null)
				{
					return false;
				}
				
				if (target instanceof Ship)
				{
					Ship ship = (Ship)target;
					
					if (shipsQueueMenu.contains(ship))
					{
						shipsQueueMenu.orderShipToDock(ship, selectedDock);
						selectedDock.getSelector().setSelected(false);
						selectedDock = null;
					}
				}
			}
			
			return false;
		}
	};
}