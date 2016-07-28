package de.unbound.screen;

import de.unbound.UnboundGame;
import de.unbound.game.World;
import de.unbound.game.logic.AbstractGameUpdate;
import de.unbound.game.wave.WaveHandler;
import de.unbound.server.view.PanelLog;

public class ServerGameScreen extends AbstractGameScreen {

	private World world;
	private float second = 0;

	public ServerGameScreen(UnboundGame game, WaveHandler waveHandler,
			AbstractGameUpdate gameMode) {
		super(game);
		world = World.getInstance();
		world.setGameRules(waveHandler, gameMode);
		
		//hiermit können input processoren aktiviert oder deaktiviert werden
		//auf dem Server besser deaktiviert, ggf löschen
		//initializeInputProcessor(); //methode entfernt
		
	}

	@Override
	public void render(float deltaTime) {
		
		world.update(deltaTime);
		second+=deltaTime;
		if(second>=1){
		PanelLog.log("Amount of Entities = " + World.getInstance().getBattleField().getGameObjects().size());
		//PanelLog.log(String.valueOf("Update! DeltaTime: "+deltaTime));
		second=0;
		}
	}

}