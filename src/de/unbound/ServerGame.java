package de.unbound;

import de.unbound.game.logic.AbstractGameUpdate;
import de.unbound.game.logic.ServerSurvivalGameUpdate;
import de.unbound.game.wave.ServerEndlessWaveHandler;
import de.unbound.game.wave.WaveHandler;
import de.unbound.screen.ServerGameScreen;
import de.unbound.screen.ServerStartScreen;


public class ServerGame extends UnboundGame {

	@Override
	public void create() {
		screen = new ServerStartScreen(this);
		//WaveHandler waveHandler = ServerEndlessWaveHandler.createServerEndlessWaveHandlerPreset();
		//AbstractGameUpdate gameMode = new ServerSurvivalGameUpdate();
		//screen = new ServerGameScreen(this, waveHandler, gameMode);
	}

}
