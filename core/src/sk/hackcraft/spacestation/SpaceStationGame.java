
package sk.hackcraft.spacestation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.swing.text.StyleContext.SmallAttributeSet;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
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
	private Random random;
	private Stage gameStage;
	private GameView actualGameView;

	private ShipsCreator shipsCreator;
	private Timer timer;

	private ArrayList<Planet> planets = new ArrayList<Planet>();

	private Dock selectedDock;
	
	private SelectionBound activeSelectionBound, targetSelectionBound;
	
	private ShipsQueueMenu shipsQueueMenu;

	private Station station;
	private StationViewMaster stationViewMaster;
	
	private StationSmall smallStation;
	
	private Interaction interaction;

	private BitmapFont mainFont;
	private TaskAndPointsManager tpManager;
	
	private Map<Ship, DistantShip> distantShips = new HashMap<Ship, DistantShip>();
	
	private GameElapsedTimeLabel gameElapsedTimeLabel;
	private ScoreLabel scorelabel;

	private BackgroundStars backgroundStars;

	@Override
	public void create()
	{
		random = new Random();
		timer = new Timer();

		gameStage = new Stage(new FitViewport(400, 240));
		Gdx.input.setInputProcessor(gameStage);
		
		// debugging
		//gameStage.setDebugAll(true);
		
		mainFont = new BitmapFont(false);
		
		Music music = Gdx.audio.newMusic(Gdx.files.internal("sounds/Room_Of_Wires_-_01_-_Asylum_Sneaker.mp3"));
		
		music.setLooping(true);
		music.play();
		
		//runIntro();
		runGame();
	}
	
	private void runIntro()
	{
		// setting up intro
		final Intro intro = new Intro();
		intro.setPosition(0, 0);
			
		gameStage.addActor(intro);

		timer.scheduleTask(new Timer.Task()
		{
			@Override
			public void run()
			{
				cleanupIntro(intro);
			}
		}, 2,10);
		
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
		
		// background initialisation
		backgroundStars = new BackgroundStars();
		addNextBackgroundImage("sprite/stars1.png", 1f);
		addNextBackgroundImage("sprite/stars2.png", 0.99f);
		addNextBackgroundImage("sprite/stars3.png", 0.98f);
		addNextBackgroundImage("sprite/stars4.png", 0.97f);
		addNextBackgroundImage("sprite/stars5.png", 0.96f);
		finishAddingBackgroundImages();
		//END background init
		
		Texture stationTexture = new Texture(Gdx.files.internal("sprite/station.png"));
		Sprite stationSprite = new Sprite(stationTexture);
		station = new Station(stationSprite);
		Texture stationLightsTexture = new Texture(Gdx.files.internal("sprite/station_lights.png"));
		station.setTextures(stationLightsTexture, 40);
		Texture stationDoorsTexture = new Texture(Gdx.files.internal("sprite/station_doors.png"));
		station.setTextures(stationDoorsTexture, 55);
		
		gameElapsedTimeLabel = new GameElapsedTimeLabel(System.currentTimeMillis(), mainFont);
		gameElapsedTimeLabel.setPosition(5, gameStage.getHeight() - 5);
		gameStage.addActor(gameElapsedTimeLabel);
		
		scorelabel = new ScoreLabel(mainFont);
		scorelabel.setPosition(200, gameStage.getHeight() - 5);
		gameStage.addActor(scorelabel);
		
		station.setPosition(50, 10);
		gameStage.addActor(station);
		
		Texture activeSelectionAtlas = new Texture(Gdx.files.local("sprite/active_selection.png"));
		Texture targetSelectionAtlas = new Texture(Gdx.files.local("sprite/target_selection.png"));
		activeSelectionBound = new SelectionBound(activeSelectionAtlas);
		targetSelectionBound = new SelectionBound(targetSelectionAtlas);
		
		// interaction mechanics
		interaction = new Interaction(gameStage, activeSelectionBound, targetSelectionBound);
		gameStage.addActor(interaction);
		
		// actual view of the player
		actualGameView = GameView.DOCKS;
		
		stationViewMaster = new StationViewMaster();

		gameStage.addListener(new InputListener()
		{
			@Override
			public boolean keyDown(InputEvent event, int keycode)
			{
				if (keycode == Input.Keys.TAB)
				{
					nextGameView();
					return true;
				}
		
				return false;
			}
			
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
			{
				if (!event.isHandled())
				{
					interaction.cancelActualInteraction();
					return true;
				}
				else
				{
					return super.touchDown(event, x, y, pointer, button);
				}
			}
		});
		
		createDocks();
		createStorageFacilities();
		
		//EPH Mala stanica
		Texture smallStationTexture = new Texture(Gdx.files.internal("sprite/station_small.png"));
		Sprite smallStationSprite = new Sprite(smallStationTexture);
		
		Texture stationSmallLightsTexture = new Texture(Gdx.files.internal("sprite/station_small_lights.png"));
		
		Vector2 stationSmallPosition = new Vector2(420, 15);
		smallStation = new StationSmall(smallStationSprite, stationSmallPosition);
		smallStation.setLights(stationSmallLightsTexture);
		gameStage.addActor(smallStation);
		
		//generating planets
		
		// Fertilizers
				Texture planetTextureFood = new Texture(Gdx.files.internal("sprite/planet1.png"));
				Sprite planetSpriteFood = new Sprite(planetTextureFood);
				Vector2 positionFood = new Vector2(460, 160);
				Vector2 sizeFood = new Vector2(planetSpriteFood.getWidth(), planetSpriteFood.getHeight());
				Planet planetFood = new Planet(planetSpriteFood, sizeFood, positionFood, 100);
				planets.add(planetFood);
				gameStage.addActor(planetFood);
				interaction.addSelectionListener(planetFood);

		// Hydrogen
				Texture planetTextureOre = new Texture(Gdx.files.internal("sprite/planet3.png"));
				Sprite planetSpriteOre = new Sprite(planetTextureOre);
				Vector2 positionOre = new Vector2(630, 40);
				Vector2 sizeOre = new Vector2(planetSpriteOre.getWidth(), planetSpriteOre.getHeight());
				Planet planetOre = new Planet(planetSpriteOre, sizeOre, positionOre, 100);
				planets.add(planetOre);
				gameStage.addActor(planetOre);
				interaction.addSelectionListener(planetOre);
				
		// Water
				Texture planetTextureMedi = new Texture(Gdx.files.internal("sprite/planet5.png"));
				Sprite planetSpriteMedi = new Sprite(planetTextureMedi);
				Vector2 positionMedi = new Vector2(550,150);
				Vector2 sizeMedi = new Vector2(planetSpriteMedi.getWidth(), planetSpriteMedi.getHeight());
				Planet planetMedi = new Planet(planetSpriteMedi, sizeMedi, positionMedi, 100);
				planets.add(planetMedi);
				gameStage.addActor(planetMedi);
				interaction.addSelectionListener(planetMedi);
				
		// Metal
				Texture planetTextureMate = new Texture(Gdx.files.internal("sprite/planet4.png"));
				Sprite planetSpriteMate = new Sprite(planetTextureMate);
				Vector2 positionMate = new Vector2(500,60);
				Vector2 sizeMate = new Vector2(planetSpriteMate.getWidth(), planetSpriteMate.getHeight());
				Planet planetMate = new Planet(planetSpriteMate, sizeMate, positionMate, 100);
				planets.add(planetMate);
				gameStage.addActor(planetMate);
				interaction.addSelectionListener(planetMate);
				
		// Goods
				Texture planetTextureElec = new Texture(Gdx.files.internal("sprite/planet2.png"));
				Sprite planetSpriteElec = new Sprite(planetTextureElec);
				Vector2 positionElec = new Vector2(630,120);
				Vector2 sizeElec = new Vector2(planetSpriteElec.getWidth(), planetSpriteElec.getHeight());
				Planet planetElec = new Planet(planetSpriteElec, sizeElec, positionElec, 100);
				planets.add(planetElec);
				gameStage.addActor(planetElec);
				interaction.addSelectionListener(planetElec);
		

		// ships generation		
		shipsCreator = new ShipsCreator(activeSelectionBound);
		
		shipsQueueMenu = new ShipsQueueMenu()
		{
			@Override
			public void initiateDocking(final Ship ship, final Dock dock)
			{				
				timer.scheduleTask(new Timer.Task()
				{
					@Override
					public void run()
					{
						ship.setPosition(450, dock.getY());
						
						ship.addAction(Actions.fadeIn(0.3f));

						flyShipToDock(ship, dock);
					}
				}, 0.3f);
			}
			
			@Override
			public void initiatePlanetAcquire(final Ship ship, final Planet planet)
			{
				CargoContainer to = ship.getCargoContainer();
				CargoContainer from = planet.getCargoContainer(to.getCargoType());
				
				initiate(ship, planet, from, to);
			}
			
			@Override
			public void initiatePlanetDelivery(Ship ship, Planet planet)
			{
				CargoContainer from = ship.getCargoContainer();
				CargoContainer to = planet.getCargoContainer(from.getCargoType());

				initiate(ship, planet, from, to);
			}
			
			private void initiate(final Ship ship, Planet planet, CargoContainer from, CargoContainer to)
			{
				final DistantShip dShip = distantShips.get(ship);
				
				final float flyTimeThere = 5, flyTimeBack = 5;
				
				flyDistantShipTo(dShip, planet, flyTimeThere);
				
				new TransferCargoTask(from, to, flyTimeThere)
				{
					@Override
					protected void finished()
					{
						timer.scheduleTask(new Timer.Task()
						{
							@Override
							public void run()
							{
								shipsQueueMenu.queueShip(ship);
							}
						}, flyTimeBack);
						
						flyDistantShipTo(dShip, smallStation, flyTimeBack);
					}
				}.run();

				
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
		addShip(GoodsType.WATER);
		addShip(GoodsType.HYDROGEN);
		addShip(GoodsType.FERTILIZERS);
		addShip(GoodsType.METALS);
		addShip(GoodsType.GOODS);
		
		this.tpManager = new TaskAndPointsManager(this);
		this.tpManager.startGeneratingTasks();
		
		setGameView(GameView.DOCKS, 0);
	}
	
	public Timer getTimer(){
		
		return this.timer;
	}
	
	public ArrayList<Planet> getPlanets(){
		return this.planets;
	}
	
	
	
	public ScoreLabel getScorelabel()
	{
		return scorelabel;
	}

	private void createDocks()
	{
		float positionY[] = {107, 81, 55, 29};
		
		for (int i = 0; i < 4; i++)
		{
			Dock dock = new Dock(i);
			dock.setPosition(121, positionY[i]);
			
			station.addDock(dock);
			
			gameStage.addActor(dock);
		}
	}
	
	private void createStorageFacilities()
	{
		CargoContainer containers[] = {
			new CargoContainer(GoodsType.GOODS, 100),
			new CargoContainer(GoodsType.FERTILIZERS, 100),
			new CargoContainer(GoodsType.METALS, 100),
			new CargoContainer(GoodsType.WATER, 100),
			new CargoContainer(GoodsType.HYDROGEN, 100),
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
			container.setCargoAmount(10);
			
			StorageFacility sf = new StorageFacility(container);
			
			sf.setCenterPosition(positionX[i], positionY[i]);
			
			station.addStorageFacility(sf);
			
			gameStage.addActor(sf);
		}
	}
	
	private void setupInteractions()
	{		
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

	private void addShip(GoodsType goodsType)
	{
		final Ship ship = shipsCreator.createShip(goodsType);

		gameStage.addActor(ship);
		
		stationViewMaster.shipArrived(ship);
		
		interaction.addSelectionListener(ship);
		
		DistantShip distantShip = shipsCreator.createDistantShip(ship);
		gameStage.addActor(distantShip);

		distantShips.put(ship, distantShip);
		
		flyDistantShipTo(distantShip, smallStation, 0);
	}
	
	private void flyDistantShipTo(DistantShip ship, Actor target, float duration)
	{
		int offsetX = random.nextInt(30) - 5;
		int offsetY = random.nextInt(30) - 5;
		
		float x = target.getCenterX() + offsetX;
		float y = target.getCenterY() + offsetY;
		
		ship.addAction(Actions.moveTo(x, y, duration));
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
		MAP(400);
		
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
		
		setGameView(nextGameView, 0.3f);
	}
	
	private void setGameView(GameView gameView, float duration)
	{
		float y = 0;

		System.out.println(gameView.getOffset());
		gameStage.addAction(Actions.moveTo(-gameView.getOffset(), y, duration, Interpolation.exp5));
		
		Actor actorsToMove[] = {shipsQueueMenu, gameElapsedTimeLabel, scorelabel};
		
		for (Actor actor : actorsToMove)
		{
			y = actor.getY();
			float localX = actor.getX() - actualGameView.getOffset();
			float x = localX + gameView.getOffset();
			actor.addAction(Actions.moveTo(x, y, duration, Interpolation.exp5));
		}
		
		//vsuvka od Epholla
		backgroundStars.gameViewMoved(gameView.getOffset());

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
		
		timer.scheduleTask(new Timer.Task()
		{
			@Override
			public void run()
			{
				shipsQueueMenu.queueShip(ship);
			}
		}, 6);
	}
	
	private class StationViewMaster implements StationView
	{
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
			
			interaction.addInteractAction(ship, "Deliver", new InteractAction()
			{
				@Override
				public boolean isActive()
				{
					return shipsQueueMenu.contains(ship);
				}
				
				@Override
				public Set<? extends Actor> getTargets()
				{
					return new HashSet<Actor>(planets);
				}
				
				@Override
				public boolean executeWithTarget(Actor target)
				{
					if (target instanceof Planet)
					{
						Planet planet = (Planet)target;
						shipsQueueMenu.sendShipToPlanet(ship, planet, Intent.DELIVER);
						
						return true;
					}
					else
					{
						return false;
					}
				}
			});
			
			interaction.addInteractAction(ship, "Acquire", new InteractAction()
			{
				@Override
				public boolean isActive()
				{
					return shipsQueueMenu.contains(ship);
				}
				
				@Override
				public Set<? extends Actor> getTargets()
				{
					return new HashSet<Actor>(planets);
				}
				
				@Override
				public boolean executeWithTarget(Actor target)
				{
					if (target instanceof Planet)
					{
						Planet planet = (Planet)target;
						shipsQueueMenu.sendShipToPlanet(ship, planet, Intent.ACQUIRE);
						
						return true;
					}
					else
					{
						return false;
					}
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
			
			new TransferCargoTask(from, to, 0)
			{
				@Override
				protected void initiated()
				{
					dock.setCargoTransfer(true);
					sf.setCargoTransfer(true);
				}
				
				@Override
				protected void finished()
				{
					dock.setCargoTransfer(false);
					sf.setCargoTransfer(false);
				}
			}.run();
			
			return true;
		}
		
		@Override
		public Set<? extends Actor> getTargets()
		{
			GoodsType type = dock.getDockedShip().getCargoContainer().getCargoType();
			return station.getFreeStorageFacilities(type);
		}
	}
	
	private class TransferCargoTask implements Runnable
	{
		private float unitTransferDelay = 0.3f;
		
		private CargoContainer from, to;
		private float initialSecondsDelay;
		
		public TransferCargoTask(CargoContainer from, CargoContainer to, float initialSecondsDelay)
		{
			this.from = from;
			this.to = to;
			
			this.initialSecondsDelay = initialSecondsDelay;
		}
		
		@Override
		public void run()
		{
			initiated();
			
			timer.scheduleTask(new Timer.Task()
			{
				@Override
				public void run()
				{
					if (from.transferUnitTo(to))
					{
						timer.scheduleTask(this, unitTransferDelay);
					}
					else
					{
						finished();
					}
				}
			}, initialSecondsDelay);
		}
		
		protected void initiated() {}
		protected void finished() {}
	}


	public Station getStation()
	{
		return station;
	}
	
	

	
	private void addNextBackgroundImage(String imageName, float moveAmount)
	{
		Texture texture = new Texture(Gdx.files.internal(imageName));
		
		Sprite sprite = new Sprite(texture);
		backgroundStars.addSprite(sprite, moveAmount);
		
	}
	
	private void finishAddingBackgroundImages()
	{
		gameStage.addActor(backgroundStars);
		for (BackgroundImage sprite: backgroundStars.getBackgroundImageActors())
		{
			gameStage.addActor(sprite);
		}
	}

	
}