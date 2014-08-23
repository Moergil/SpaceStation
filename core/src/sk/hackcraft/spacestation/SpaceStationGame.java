package sk.hackcraft.spacestation;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.FloatAction;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class SpaceStationGame extends ApplicationAdapter
{
	private Stage stage;

	private Texture testTexture;

	@Override
	public void create()
	{
		stage = new Stage(new FitViewport(400, 240));
		Gdx.input.setInputProcessor(stage);

		testTexture = new Texture(Gdx.files.internal("badlogic.jpg"));

		Sprite sprite = new Sprite(testTexture);
		Ship ship = new Ship(sprite);
		ship.setCenterPosition(0, 0);
		stage.addActor(ship);

		// ship.setTargetPosition(new Vector2(100, 100), 5);
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

		stage.getCamera().position.set(0, 0, 0);

		stage.act(0.016f);
		stage.draw();

		/*
		 * Batch batch = stage.getBatch();
		 * 
		 * batch.begin(); batch.draw(testTexture, 0, 0); batch.end();
		 */

	}

	private enum GameView
	{
		DOCKS, MAP;
	}

	private void transitionTo(GameView gameView)
	{
		switch (gameView)
		{
			case DOCKS:
			{
				FloatAction action = new FloatAction(200, -200);
				action.setDuration(2);
				stage.addAction(action);
			}
				break;
			case MAP:
			{
				FloatAction action = new FloatAction(-200, 200);
				action.setDuration(2);
				stage.addAction(action);
			}
				break;
		}
	}
}
