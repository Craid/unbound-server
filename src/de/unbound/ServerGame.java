package de.unbound;

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
