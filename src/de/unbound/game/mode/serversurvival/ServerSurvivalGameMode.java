package de.unbound.game.mode.serversurvival;

import de.unbound.game.mode.GameMode;

public class ServerSurvivalGameMode extends GameMode{
	public ServerSurvivalGameMode(){
		super(ServerSurvivalWaveHandler.createServerSurvivalWaveHandlerPreset(),new ServerSurvivalGameUpdate());
	}
}
