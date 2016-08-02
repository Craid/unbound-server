package de.unbound.game.mode.serversurvival;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.unbound.game.GameCamera;
import de.unbound.game.World;
import de.unbound.game.mode.AbstractGameUpdate;
import de.unbound.game.mode.serversurvival.util.NetworkUpdateHelper;
import de.unbound.game.model.entities.Entity;
import de.unbound.server.UnboundServerConstants;
import de.unbound.server.UnboundServerConstants.Status;
import de.unbound.server.network.ClientConnection;
import de.unbound.server.network.ConnectionHandler;
import de.unbound.utility.UnboundConstants;

public class ServerSurvivalGameUpdate extends AbstractGameUpdate{
	
	private BitmapFont font;
	private SpriteBatch batch;
	private NetworkUpdateHelper updateHelper;
	private int timer;
	private ConnectionHandler connectionHandler;
	
	public ServerSurvivalGameUpdate() {
		super(new ServerSurvivalCollisionDetection());
		init();
	}

	protected void init() {
		batch = new SpriteBatch();
		int width = UnboundConstants.WORLDWIDTH;
		int height = UnboundConstants.WORLDHEIGHT;
		camera = new GameCamera(width,height);
		camera.position.x = width/2;
		camera.position.y = height/2;
		font = new BitmapFont();
		timer = 0;
		
		connectionHandler = new ConnectionHandler(UnboundServerConstants.PORT);
		updateHelper = new NetworkUpdateHelper(battleField,connectionHandler);

		connectionHandler.startServer();
	}

	@Override
	public boolean isGameOver() {
		return battleField.getMainBase().isActive();
	}	
	
	@Override
	public void doBeforeUpdate() {
		//if(timer++>4){
			connectionHandler.udpSender.sendAllEntitiesToAllPlayers(battleField);
		//} 
		updateHelper.updateReceivedPlayers();
	}
	
	@Override
	public void onCollisionHandling(double deltaTime) {
		collisionDetection.update(deltaTime);
	}

	@Override
	public void doAfterUpdate() {
		render();
		for(Entity e : battleField.getPlayers()){
			if(!e.isActive()){
				e.setHp(e.getModel().getInitialHP());
				e.setActive(true);
				ClientConnection client = connectionHandler.getClientConnectionByPlayerID(e.getId());
				connectionHandler.tcpSender.tellOne("Respawn", client.getSocket());
			}
		}
	}

	
	@Override
	public void onGameEnd() {
		UnboundServerConstants.SERVER_STATUS = Status.GAMEOVER;
		connectionHandler.stopServer();
	}


	public void render() {
		Gdx.gl.glClearColor( 0, 0, 0.10f, 1 );
		Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );
		updateCameraPosition();
		batch.setProjectionMatrix(camera.combined); //ka warum... aber man muss es drinlassen
		//Damit die batch weiß, welcher Bereich angezeigt werden soll
		batch.begin();
		background.draw(batch);
		
		for(Entity e : battleField.getGameObjects()){
			e.render(batch);
		}
		
		batch.end();
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

	@Override
	public boolean isPaused() {
		return battleField.getPlayers().size() == 0 || UnboundServerConstants.SERVER_STATUS == Status.PAUSED;
	}

	@Override
	public void onGamePaused() {
		render();
		battleField.update(0);
		batch.begin();
		font.draw(batch, "Paused", Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
		batch.end();
	}
}