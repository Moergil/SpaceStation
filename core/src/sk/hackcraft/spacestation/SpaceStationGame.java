
package sk.hackcraft.spacestation;

import java.util.ArrayList;
import java.util.Set;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
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
	
	private Stage gameStage;
	private GameView actualGameView;

	private ShipsCreator shipsGenerator;
	private Timer timer;

	private ArrayList<Planet> planets = new ArrayList<Planet>();

	private Dock selectedDock;
	
	private SelectionBound activeSelectionBound, targetSelectionBound;
	
	private ShipsQueueMenu shipsQueueMenu;

	private SoundMngr mngrSound;
	
	private Station station;
	private StationViewMaster stationViewMaster;
	
	private Interaction interaction;
	private Space space;

	private BitmapFont mainFont;
	private TaskAndPointsManager tpManager;

	@Override
	public void create()
	{
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
		
		//generating planets
		
		//Food
				Texture planetTextureFood = new Texture(Gdx.files.internal("sprite/planet1.png"));
				Sprite planetSpriteFood = new Sprite(planetTextureFood);
				Vector2 positionFood = new Vector2(460, 160);
				Vector2 sizeFood = new Vector2(68, 68);
				Planet planetFood = new Planet(planetSpriteFood, sizeFood, positionFood, GoodsType.FOOD, 20);
				planets.add(planetFood);
				gameStage.addActor(planetFood);
				
				
				
		//Ore
				Texture planetTextureOre = new Texture(Gdx.files.internal("sprite/planet1.png"));
				Sprite planetSpriteOre = new Sprite(planetTextureOre);
				Vector2 positionOre = new Vector2(630, 0);
				Vector2 sizeOre = new Vector2(68, 68);
				Planet planetOre = new Planet(planetSpriteOre, sizeOre, positionOre, GoodsType.ORE, 20);
				planets.add(planetOre);
				gameStage.addActor(planetOre);
				
		//Medicine
				Texture planetTextureMedi = new Texture(Gdx.files.internal("sprite/planet1.png"));
				Sprite planetSpriteMedi = new Sprite(planetTextureMedi);
				Vector2 positionMedi = new Vector2(550,150);
				Vector2 sizeMedi = new Vector2(68, 68);
				Planet planetMedi = new Planet(planetSpriteMedi, sizeMedi, positionMedi, GoodsType.MEDICINE, 20);
				planets.add(planetMedi);
				gameStage.addActor(planetMedi);
				
		//Material
				Texture planetTextureMate = new Texture(Gdx.files.internal("sprite/planet1.png"));
				Sprite planetSpriteMate = new Sprite(planetTextureMate);
				Vector2 positionMate = new Vector2(500,60);
				Vector2 sizeMate = new Vector2(68, 68);
				Planet planetMate = new Planet(planetSpriteMate, sizeMate, positionMate, GoodsType.MATERIAL, 20);
				planets.add(planetMate);
				gameStage.addActor(planetMate);
				
		//Electronics
				Texture planetTextureElec = new Texture(Gdx.files.internal("sprite/planet1.png"));
				Sprite planetSpriteElec = new Sprite(planetTextureElec);
				Vector2 positionElec = new Vector2(630,100);
				Vector2 sizeElec = new Vector2(68, 68);
				Planet planetElec = new Planet(planetSpriteElec, sizeElec, positionElec, GoodsType.MATERIAL, 20);
				planets.add(planetElec);
				gameStage.addActor(planetElec);
				
	
		
		

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
			
			interaction.addInteractAction(dock, "Unload", new TransferCargoInteraction(dock, TransferDirection.TO_STORAGE_FACILITY));
			interaction.addInteractAction(dock, "Load", new TransferCargoInteraction(dock, TransferDirection.TO_SHIP_IN_DOCK));
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
	
	public enum TransferDirection
	{
		TO_STORAGE_FACILITY,
		TO_SHIP_IN_DOCK;
	}

	private class TransferCargoInteraction extends InteractAction
	{
		private final Dock dock;
		private final TransferDirection direction;
		
		public TransferCargoInteraction(Dock dock, TransferDirection direction)
		{
			this.dock = dock;
			this.direction = direction;
		}
		
		@Override
		public boolean isActive()
		{
			return dock.hasDockedShip();
		}
		
		@Override
		public boolean executeWithTarget(Actor target)
		{
			if (!(target instanceof StorageFacility))
			{
				return false;
			}
			
			final StorageFacility sf = (StorageFacility)target;
			
			if (!sf.canTransferCargo(dock))
			{
				return false;
			}
			
			final CargoContainer from, to;
			
			switch (direction)
			{
				case TO_STORAGE_FACILITY:
					from = dock.getDockedShip().getCargoContainer();
					to = sf.getCargoContainer();
					break;
				case TO_SHIP_IN_DOCK:
					from = sf.getCargoContainer();
					to = dock.getDockedShip().getCargoContainer();
					break;
				default:
					return false;
			}

			dock.setCargoTransfer(true);
			sf.setCargoTransfer(true);
			
			timer.scheduleTask(new Timer.Task()
			{
				private int transferred;
				
				@Override
				public void run()
				{
					if (from.transferUnit(to))
					{
						timer.scheduleTask(this, 0.1f);
						System.out.println("Transferring from " + dock.getName());
						
						transferred++;
					}
					else
					{
						dock.setCargoTransfer(false);
						sf.setCargoTransfer(false);
						System.out.println("Completed transfer " + direction + ", transferred " + transferred);

						System.out.printf("From: %d/%d%n", from.getCargoAmount(), from.getCargoCapacity());
						System.out.printf("To: %d/%d%n", to.getCargoAmount(), to.getCargoCapacity());
					}
				}
			}, 0.1f);
			
			return true;
		}
		
		@Override
		public Set<? extends Actor> getTargets()
		{
			GoodsType type = dock.getDockedShip().getCargoContainer().getCargoType();
			return station.getFreeStorageFacilities(type);
		}
	}
}