
package sk.hackcraft.spacestation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import sk.hackcraft.spacestation.StationView.StationViewListener;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
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
	
	private Stage gameStage, hudStage;
	private GameView actualGameView;

	private ShipsCreator shipsGenerator;
	private Timer timer;

	private ArrayList<Planet> planets = new ArrayList<Planet>();

	private Dock selectedDock;
	
	private SelectionBound selectionBound;
	
	private ShipsQueueMenu shipsQueueMenu;
	
	private SelectionListener selectionListener = new SelectionListener();

	private SoundMngr mngrSound;
	
	private Station station;
	private StationViewMaster stationViewMaster;
	
	private Space space;
	
	private TaskAndPointsManager tpManager;

	@Override
	public void create()
	{
		
 
		timer = new Timer();
		
		mngrSound = new SoundMngr();

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
		
		mngrSound.runIntro();
			
		hudStage.addActor(intro);

		timer.scheduleTask(new Timer.Task()
		{
			@Override
			public void run()
			{
				cleanupIntro(intro);
			}
		}, 16,16);
		
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
		mngrSound.stopIntro();
		
		
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
		
		mngrSound.runMusicGame();
		
		station.setPosition(50, 20);
		gameStage.addActor(station);
		
		Texture cornersAtlas = new Texture(Gdx.files.local("sprite/active_selection.png"));
		selectionBound = new SelectionBound(cornersAtlas);
		
		// actual view of the player
		actualGameView = GameView.DOCKS;
		
		stationViewMaster = new StationViewMaster();

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
		
		//EPH Mala stanica
		Texture smallStationTexture = new Texture(Gdx.files.internal("sprite/station_small.png"));
		Sprite smallStationSprite = new Sprite(smallStationTexture);
		
		Texture stationSmallLightsTexture = new Texture(Gdx.files.internal("sprite/station_small_lights.png"));
		
		Vector2 stationSmallPosition = new Vector2(420, 15);
		StationSmall smallStation = new StationSmall(smallStationSprite, stationSmallPosition);
		smallStation.setLights(stationSmallLightsTexture);
		gameStage.addActor(smallStation);
		
		//EPH: zatial jedina planeta, aby ju bolo aspon vidno
		//generating planets
		Texture planetTexture = new Texture(Gdx.files.internal("sprite/planet1.png"));
		Sprite planetSprite = new Sprite(planetTexture);
		Vector2 position = new Vector2(430, 160);
		Vector2 size = new Vector2(68, 68);
		Planet planet = new Planet(planetSprite, size, position, GoodsType.FOOD, 20);
		planets.add(planet);
		gameStage.addActor(planet);
		/*planets.add(new Planet(GoodsType.ORE));
		planets.add(new Planet(GoodsType.MEDICINE));
		planets.add(new Planet(GoodsType.MATERIAL));
		planets.add(new Planet(GoodsType.ELECTRONICS));*/
		//planets.add(new Planet(GoodsType.ORE,20));
		//planets.add(new Planet(GoodsType.MEDICINE,20));
		//planets.add(new Planet(GoodsType.MATERIAL,20));
		//planets.add(new Planet(GoodsType.ELECTRONICS,20));
		
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
		
		// TODO debug
		addShip();
		addShip();
		addShip();
		
		//testovanie
		this.tpManager = new TaskAndPointsManager(this);
		this.tpManager.startGeneratingTasks();
	}
	
	public Timer getTimer(){
		
		return this.timer;
	}
	
	public ArrayList<Planet> getPlanets(){
		return this.planets;
	}
	
	private void registerSelectionListener(final Actor actor)
	{
		actor.addListener(selectionListener);
	}

	private void addShip()
	{
		final Ship ship = shipsGenerator.createGeneric();

		hudStage.addActor(ship);
		
		registerSelectionListener(ship);
		
		stationViewMaster.shipArrived(ship);
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
				ship.setIdle();
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
	
	private class StationViewMaster implements StationView
	{
		private StationViewListener listener;
		
		public void releaseShip(Ship ship)
		{
			listener.shipDeparted(ship);
		}
		
		@Override
		public void setListener(StationViewListener listener)
		{
			this.listener = listener;
		}
		
		@Override
		public void shipArrived(Ship ship)
		{
			shipsQueueMenu.queueShip(ship);
		}
	}

}