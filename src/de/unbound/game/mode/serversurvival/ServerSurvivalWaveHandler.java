package de.unbound.game.mode.serversurvival;

import de.unbound.game.BattleField;
import de.unbound.game.factories.EntityFactory;
import de.unbound.game.mode.WaveHandler;
import de.unbound.game.model.entities.WaveOrder;

public class ServerSurvivalWaveHandler extends WaveHandler {
	
	/**
	 * Timeout in seconds
	 */
	private static final int WAVETIMEOUTS = 5;
	private double cummulativeTime;

	public ServerSurvivalWaveHandler(EntityFactory ownFactory, EntityFactory enemyFactory) {
		super(ownFactory, enemyFactory);
		cummulativeTime = 2; //First Wave in 3 Seconds, next in steps of WAVETIMEOUTS
		level = 1;
	}
	
	/**
	 * Erstelle ein Spiel in dem die Spieler mit den Prelaten gegen die Enten kämpfen
	 * @return
	 */
	public static ServerSurvivalWaveHandler createServerSurvivalWaveHandlerPreset() {
		return new ServerSurvivalWaveHandler(new ServerSurvivalEntityFactory("Prelate", false),new ServerSurvivalEntityFactory("Duck", true));
	}
	
	@Override
	public void update(double deltaTime) {
		cummulativeTime += deltaTime;
		if(cummulativeTime > WAVETIMEOUTS){
			cummulativeTime -= WAVETIMEOUTS;
			level++;
			setCurrentOrder(createWaveAccordingToDifficulity());
		}
	}

	/**
	 * Hier wird die Welle geskriptet
	 * 
	 * @return
	 */
	private WaveOrder createWaveAccordingToDifficulity() {
		int boss = level/10;
		int commander = level/5;
		int scavenger = level%5;
		int pawn = (level%5) * 3;
		return new WaveOrder(boss, pawn, scavenger, commander);
	}
	
	@Override
	public void initializeMap(BattleField battleField) {
		getEnemyFactory().createSpawner();
		getOwnFactory().createMap(this.getSeed());
		battleField.update(0); //initial update to write Entities to list
	}
	
}