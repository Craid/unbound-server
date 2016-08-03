package de.unbound.screen;

import de.unbound.UnboundGame;
import de.unbound.game.World;
import de.unbound.game.mode.GameMode;

public class ServerGameScreen extends AbstractGameScreen {

	private World world;
	private float second = 0;

	public ServerGameScreen(UnboundGame game, GameMode gameMode) {
		super(game);
		world = World.getInstance();
		world.setGameMode(gameMode);
		
		//hiermit k�nnen input processoren aktiviert oder deaktiviert werden
		//auf dem Server besser deaktiviert, ggf l�schen
		//initializeInputProcessor(); //methode entfernt
		
	}

	@Override
	public void render(float deltaTime) {
		
		world.update(deltaTime);
		second+=deltaTime;
		if(second>=25){
		second=0;
		}
	}

}