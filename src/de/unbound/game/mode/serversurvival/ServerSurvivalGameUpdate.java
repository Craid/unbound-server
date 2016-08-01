package de.unbound.game.mode.serversurvival;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import de.unbound.game.GameCamera;
import de.unbound.game.World;
import de.unbound.game.mode.AbstractGameUpdate;
import de.unbound.game.model.entities.Entity;
import de.unbound.utility.UnboundConstants;

public class ServerSurvivalGameUpdate extends AbstractGameUpdate{
	
	private BitmapFont font;
	private SpriteBatch batch;
	private SpriteBatch hudBatch;
	private Sprite bathTub;
	
	public ServerSurvivalGameUpdate() {
		initAbstract(new ServerSurvivalCollisionDetection());
		init();
	}

	protected void init() {
		batch = new SpriteBatch();
		hudBatch = new SpriteBatch();
		int width = UnboundConstants.WORLDWIDTH*battleField.getScaleX();
		int height = UnboundConstants.WORLDHEIGHT*battleField.getScaleY();
		camera = new GameCamera(width,height);
		camera.position.x = width/2;
		camera.position.y = height/2;
		font = new BitmapFont();
		bathTub = new Sprite(new Texture(Gdx.files.internal("img/bathtub.png")));
		bathTub.setPosition(-560, -260);
		bathTub.setOrigin(0, 0);
		bathTub.setScale(1.33f, 1.08f);
	}

	@Override
	public boolean isGameOver() {
		return battleField.getMainBase().isActive();
	}	
	
	@Override
	public void doBeforeUpdate() {
		//Do nothing
	}

	@Override
	public void onCollisionHandling(double deltaTime) {
		collisionDetection.update(deltaTime);
	}

	@Override
	public void doAfterUpdate() {
		render();
	}

	@Override
	public void onGameEnd() {
		renderGameOver();
	}


	public void render() {
		Gdx.gl.glClearColor( 0, 0, 0.10f, 1 );
		Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );
		updateCameraPosition();
		batch.setProjectionMatrix(camera.combined); //ka warum... aber man muss es drinlassen
		//Damit die batch weiß, welcher Bereich angezeigt werden soll
		batch.begin();
		bathTub.draw(batch);
		
		for(Entity e : battleField.getGameObjects()){
			e.render(batch);
		}
		
		batch.end();
	}
	
	private void renderGameOver() {
		render();
		hudBatch.begin();
		String temp = "GameOver!";
		font.draw(hudBatch, temp, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
		hudBatch.end();
	}

	private void updateCameraPosition(){
		camera.update();
	}

	@Override
	public void updateWaveHandler(double deltaTime) {
		world.getWaveHandler().update(deltaTime);
		if(world.getWaveHandler().hasNewOrder())
			world.getWaveHandler().getEnemyFactory().createWave(world.getWaveHandler().getCurrentOrder());
	}

	@Override
	public void renderEntity(Entity e) {
		Sprite sprite = e.getModel().getGraphic();
		sprite.setPosition(e.getPosition().x-(sprite.getWidth()/2), e.getPosition().y-(sprite.getHeight()/2));
		sprite.setRotation(e.getDirection().angle());
		if(World.getInstance().isOnScreen(e))
			sprite.draw(batch);
	}
}