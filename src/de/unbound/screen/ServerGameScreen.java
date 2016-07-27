package de.unbound.screen;

import de.unbound.UnboundGame;
import de.unbound.game.World;
import de.unbound.game.logic.AbstractGameUpdate;
import de.unbound.game.wave.WaveHandler;
import de.unbound.server.view.LogPanel;

public class ServerGameScreen extends AbstractGameScreen {

	private World world;

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
		LogPanel.log(String.valueOf("Update! DeltaTime: "+deltaTime));
	}

}