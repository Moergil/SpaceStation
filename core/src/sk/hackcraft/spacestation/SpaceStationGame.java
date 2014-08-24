
package sk.hackcraft.spacestation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import sk.hackcraft.spacestation.StationView.StationViewListener;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
	
	private Stage gameStage;
	private GameView actualGameView;

	private ShipsCreator shipsGenerator;
	private Timer timer;

	private List<Planet> planets = new ArrayList<Planet>();

	private Dock selectedDock;
	
	private SelectionBound activeSelectionBound, targetSelectionBound;
	
	private ShipsQueueMenu shipsQueueMenu;

	private SoundMngr mngrSound;
	
	private Station station;
	private StationViewMaster stationViewMaster;
	
	private Interaction interaction;
	private Space space;
	
	private BitmapFont mainFont;

	@Override
	public void create()
	{
		random = new Random();
		timer = new Timer();
		
		mngrSound = new SoundMngr();

		gameStage = new Stage(new FitViewport(400, 240));
		Gdx.input.setInputProcessor(gameStage);
		
		// debugging
		gameStage.setDebugAll(false);
		
		mainFont = new BitmapFont(false);
		
		runIntro();
//		runGame();
	}
	
	private void runIntro()
	{
		// setting up intro
		final Intro intro = new Intro();
		intro.setPosition(0, 0);
		
		mngrSound.runIntro();
			
		gameStage.addActor(intro);

		timer.scheduleTask(new Timer.Task()
		{
			@Override
			public void run()
			{
				cleanupIntro(intro);
			}
		}, 15,15);
		
		gameStage.addListener(new InputListener()
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
				gameStage.clear();
				runGame();
			}
		}, 0);
	}
	
	public void runGame()
	{
		// TODO debug
		gameStage.addListener(new InputListener()
		{
			@Override
			public boolean mouseMoved(InputEvent event, float x, float y)
			{
				//System.out.printf("%.2f %.2f%n", x, y);
				return false;
			}
		});
		
		Texture stationTexture = new Texture(Gdx.files.internal("sprite/station.png"));
		Sprite stationSprite = new Sprite(stationTexture);
		station = new Station(stationSprite);
		
		mngrSound.runMusicGame();
		
		station.setPosition(50, 10);
		gameStage.addActor(station);
		
		Texture activeSelectionAtlas = new Texture(Gdx.files.local("sprite/active_selection.png"));
		Texture targetSelectionAtlas = new Texture(Gdx.files.local("sprite/target_selection.png"));
		activeSelectionBound = new SelectionBound(activeSelectionAtlas);
		targetSelectionBound = new SelectionBound(targetSelectionAtlas);
		
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
				
				if (keycode == Input.Keys.S)
				{
					addShip();

					return true;
				}
		
				return false;
			}
		});
		
		setInstantGameView(GameView.DOCKS);
		
		createDocks();
		createStorageFacilities();
		
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
		shipsGenerator = new ShipsCreator(activeSelectionBound);
		
		shipsQueueMenu = new ShipsQueueMenu()
		{
			@Override
			public void initiateDocking(Ship ship, Dock dock)
			{
				ship.setPosition(450, dock.getY());
				
				flyShipToDock(ship, dock);
			}
		};

		gameStage.addActor(shipsQueueMenu);
		
		{
			float x = gameStage.getWidth() - shipsQueueMenu.getWidth();
			shipsQueueMenu.setPosition(x, 0);
			
			shipsQueueMenu.matchHeightToStage();
		}
		
		setupInteractions();
		
		// TODO debug
		addShip();
		addShip();
		addShip();
	}
	
	private void createDocks()
	{
		float positionY[] = {107, 81, 55, 29};
		
		for (int i = 0; i < 4; i++)
		{
			Dock dock = new Dock(i, activeSelectionBound);
			dock.setPosition(121, positionY[i]);
			
			station.addDock(dock);
			
			gameStage.addActor(dock);
		}
	}
	
	private void createStorageFacilities()
	{
		CargoContainer containers[] = {
			new CargoContainer(GoodsType.ELECTRONICS, 100),
			new CargoContainer(GoodsType.FOOD, 100),
			new CargoContainer(GoodsType.MATERIAL, 100),
			new CargoContainer(GoodsType.MEDICINE, 100),
			new CargoContainer(GoodsType.ORE, 100),
		};
		
		float positionX[] = {
			120,
			73,
			70,
			61,
			80
		};
		
		float positionY[] = {
			140,
			83,
			33,
			140,
			208
		};
		
		for (int i = 0; i < 5; i++)
		{
			CargoContainer container = containers[i];
			StorageFacility sf = new StorageFacility(container);
			
			sf.setPosition(positionX[i], positionY[i]);
			
			station.addStorageFacility(sf);
			
			gameStage.addActor(sf);
		}
	}
	
	private void setupInteractions()
	{
		
		interaction = new Interaction(gameStage, activeSelectionBound, targetSelectionBound);
		gameStage.addActor(interaction);
		
		for (final Dock dock : station.getDocks())
		{
			interaction.addMasterActor(dock, new Interaction.ActiveCheck()
			{
				@Override
				public boolean isActive()
				{
					return dock.hasDockedShip() && !dock.isTransferringCargo();
				}
			});
			
			interaction.addInteractAction(dock, "Undock", new InteractAction()
			{
				@Override
				public boolean isActive()
				{
					return dock.hasDockedShip();
				}
				
				@Override
				public boolean execute()
				{
					releaseShipFromDock(dock);
					return true;
				}
			});
			
			interaction.addInteractAction(dock, "Unload", new InteractAction()
			{
				@Override
				public boolean isActive()
				{
					return dock.hasDockedShip();
				}
				
				@Override
				public boolean executeWithTarget(Actor target)
				{
					if (target instanceof StorageFacility)
					{
						final StorageFacility sf = (StorageFacility)target;
						
						if (!sf.canTransferCargo(dock))
						{
							return false;
						}
						
						final CargoContainer facilityCC = sf.getCargoContainer();
						final CargoContainer shipCC = dock.getDockedShip().getCargoContainer();
						
						dock.setCargoTransfer(true);
						sf.setCargoTransfer(true);
						
						timer.scheduleTask(new Timer.Task()
						{
							private int transferred;
							
							@Override
							public void run()
							{
								if (shipCC.transferUnit(facilityCC))
								{
									timer.scheduleTask(this, 0.1f);
									System.out.println("Transferring from " + dock.getName());
									
									transferred++;
								}
								else
								{
									dock.setCargoTransfer(false);
									sf.setCargoTransfer(false);
									System.out.println("Completed, transferred " + transferred);
									
									System.out.printf("Storage facility: %d/%d", facilityCC.getCargoAmount(), facilityCC.getCargoCapacity());
									System.out.printf("Ship: %d/%d", shipCC.getCargoAmount(), shipCC.getCargoCapacity());
								}
							}
						}, 0.1f);
						
						return true;
					}
					
					return false;
				}
				
				@Override
				public Set<? extends Actor> getTargets()
				{
					GoodsType type = dock.getDockedShip().getCargoContainer().getCargoType();
					return station.getFreeStorageFacilities(type);
				}
			});
		}
		
		for (StorageFacility sf : station.getStorageFacilities())
		{
			interaction.addSelectionListener(sf);
		}
	}

	private void addShip()
	{
		final Ship ship = shipsGenerator.createGeneric();

		gameStage.addActor(ship);
		
		stationViewMaster.shipArrived(ship);
		
		interaction.addSelectionListener(ship);
	}
	
	@Override
	public void resize(int width, int height)
	{
		gameStage.getViewport().update(width, height, true);
	}

	@Override
	public void render()
	{
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		gameStage.act(Gdx.graphics.getDeltaTime());
		
		gameStage.draw();
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
		final Ship ship = dock.getDockedShip();
		dock.undockShip();
		
		timer.scheduleTask(new Timer.Task()
		{
			@Override
			public void run()
			{
				Vector2 targetPosition = new Vector2(350, ship.getY());
				ship.depart(targetPosition, 5, timer);
			}
		}, 1);
	}
	
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
		public void shipArrived(final Ship ship)
		{
			shipsQueueMenu.queueShip(ship);
			
			interaction.addMasterActor(ship, new Interaction.ActiveCheck()
			{
				@Override
				public boolean isActive()
				{
					return shipsQueueMenu.contains(ship);
				}
			});
			
			interaction.addInteractAction(ship, "Dock", new InteractAction()
			{
				@Override
				public boolean isActive()
				{
					return !station.getFreeDocks().isEmpty();
				}
				
				@Override
				public Set<? extends Actor> getTargets()
				{
					return station.getFreeDocks();
				}
				
				@Override
				public boolean isOneTime()
				{
					return true;
				}
				
				@Override
				public boolean executeWithTarget(Actor target)
				{
					if (target instanceof Dock)
					{						
						Dock dock = (Dock)target;
						dock.setReserved();
						shipsQueueMenu.orderShipToDock(ship, dock);
						
						return true;
					}
					
					return false;
				}
			});
		}
	}

}