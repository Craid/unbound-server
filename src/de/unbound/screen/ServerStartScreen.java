package de.unbound.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.unbound.UnboundGame;
import de.unbound.game.logic.LocalGameUpdate;
import de.unbound.game.wave.LocaleEndlessWaveHandler;

public class ServerStartScreen extends AbstractGameScreen{

	OrthographicCamera camera;
	UnboundGame game;
	SpriteBatch batch;
	BitmapFont font;
	
	public ServerStartScreen(UnboundGame game) {
		super(game);
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		this.game = game;
		batch = new SpriteBatch();
		font = new BitmapFont();
	}
	
		@Override
	    public void render(float delta) {

	        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
	        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

	        camera.update();
	        batch.setProjectionMatrix(camera.combined);
	        batch.begin();
	        
	        font.draw(batch, "Server Startscreen", 0, 50);
	        font.draw(batch, "(Vorerst) Klicke mit der Maus/dem Touchpad 1x um das Spiel zu starten", 0, 100);
	        batch.end();

	        if (Gdx.input.isTouched()) { 
	            game.setScreen(new ServerGameScreen(game,LocaleEndlessWaveHandler.createLocaleEndlessWaveHandlerPreset(),new LocalGameUpdate()));
	            dispose();
	        }
	    }
	}


