package de.unbound.game.mode.serversurvival;

import de.unbound.game.mode.AbstractGameUpdate;
import de.unbound.game.model.entities.Entity;

public class ServerSurvivalGameUpdate extends AbstractGameUpdate{

	@Override
	public boolean isGameOver() {
		return battleField.getMainBase().isActive();
	}	
	
	@Override
	public void doBeforeUpdate() {
		//get updated entites!
		//add them to battlefield!
	}

	@Override
	public void doAfterUpdate() {
		//Do nothing normally rendering
	}

	@Override
	public void onGameEnd() {
		//send game Over Message
	}

	@Override
	public void onCollisionHandling(double deltaTime) {
		collisionDetection.update(deltaTime);
	}

	@Override
	public void updateWaveHandler(double deltaTime) {
	}

	@Override
	public void renderEntity(Entity e) {
		//render nothing! intentionally left blank
	}

}
