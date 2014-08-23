package sk.hackcraft.spacestation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.FloatAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sun.media.jfxmedia.events.NewFrameEvent;

public class SpaceStationGame extends ApplicationAdapter
{

	private Random random;
	
	private Stage stage;
	private GameView actualGameView;

	private ShipsGenerator shipsGenerator;
	private Timer timer;

	private List<Dock> docks = new ArrayList<Dock>();

	@Override
	public void create()
	{
		
		Intro intro = new Intro();
		intro.setPosition(0, 0);
			
		stage = new Stage(new FitViewport(400, 240));
		Gdx.input.setInputProcessor(stage);
		stage.addActor(intro);
		
		timer = new Timer();
		timer.scheduleTask(new Timer.Task()
		{
			@Override
			public void run()
			{
				System.out.println("tick");
//				timer.stop();
				createGame();
			}
		}, 5);
		
		stage.addListener(new InputListener()
		{
			@Override
			public boolean keyDown(InputEvent event, int keycode)
			{
				timer.stop();
				createGame();
				return true;
			}
		});
		
		
	}
	public void createGame()
	{
		random = new Random();
		
		// actual view of the player
		actualGameView = GameView.DOCKS;

		// setting up graphics stage
		stage = new Stage(new FitViewport(400, 240));
		Gdx.input.setInputProcessor(stage);

		stage.addListener(new InputListener()
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
					for (Dock dock : docks)
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
		
				return false;
			}
		});
		
		setInstantGameView(GameView.DOCKS);
		
		// debugging
		stage.setDebugAll(true);
		
		for (int i = 0; i < 4; i++)
		{
			Dock dock = new Dock();
			dock.setPosition(100, 50 + i * 50);
			docks.add(dock);
			
			stage.addActor(dock);
		}
		
		// ships generation
		shipsGenerator = new ShipsGenerator();
		
		timer = new Timer();
		timer.scheduleTask(new Timer.Task()
		{
			@Override
			public void run()
			{
				final Ship ship = shipsGenerator.generate();
				ship.setPosition(450, 0);
				
				stage.addActor(ship);
				
				System.out.println("ship generated");
				
				final Dock dock = docks.get(random.nextInt(docks.size()));
				
				if (dock.hasDockedShip())
				{
					ship.remove();
					return;
				}
				
				ship.setCenterPosition(450, dock.getY());
				
				Vector2 position = dock.calculateShipDockingPosition(ship);
				ship.arrive(position, 5);
				
				timer.scheduleTask(new Timer.Task()
				{
					@Override
					public void run()
					{
						dock.dockShip(ship);
					}
				}, 6);
			}
		}, 0, 5);
	}

	@Override
	public void resize(int width, int height)
	{
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void render()
	{
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(Gdx.graphics.getDeltaTime());
		
		stage.draw();
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
		stage.addAction(action);
		actualGameView = gameView;
	}
	
	private void setInstantGameView(GameView gameView)
	{
		float y = 0;
		float duration = 0.0f;
		MoveToAction action = Actions.moveTo(gameView.getOffset(), y, duration);
		stage.addAction(action);
		actualGameView = gameView;
	}
	
	private void flyShipToDock(Ship ship, Dock dock)
	{
		Vector2 dockingPosition = dock.getDockingAdapterPosition();
		
		Action flyToDockAction = Actions.moveTo(dockingPosition.x, dockingPosition.y, 5, Interpolation.exp10);
		ship.addAction(flyToDockAction);
	}
	
	private void releaseShipFromDock(Dock dock)
	{
		Vector2 dockingPosition = dock.getDockingAdapterPosition();
		
		Action flyToDockAction = Actions.moveTo(dockingPosition.x + 300, dockingPosition.y, 5, Interpolation.exp10);
		
		Ship ship = dock.getDockedShip();
		ship.addAction(flyToDockAction);
	}
}
