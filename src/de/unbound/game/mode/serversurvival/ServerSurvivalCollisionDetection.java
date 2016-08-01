package de.unbound.game.mode.serversurvival;

import de.unbound.game.collisionhandler.BodyCollisionHandler;
import de.unbound.game.collisionhandler.VisionCollisionHandler;
import de.unbound.game.mode.CollisionDetection;
import de.unbound.game.model.entities.Entity;
import de.unbound.utility.UnboundConstants;

public class ServerSurvivalCollisionDetection extends CollisionDetection {

	public ServerSurvivalCollisionDetection() {
		super(new  BodyCollisionHandler(), new VisionCollisionHandler());
	}

	public void update(double deltaTime) {
		updateEnemies();
		updateOwn();
		for(Entity e : battleField.getGameObjects())
			limit(e);
	}

	/**
	 * Already checks player and projectiles. 
	 * That's why they are missing in updateOwn()
	 */
	private void updateEnemies() {
		for (Entity me : battleField.getEnemies()) {
			checkVisionAndBodyCollision(me, battleField.getCollectors());
			checkVisionAndBodyCollision(me, battleField.getFriendlyProjectiles());
			checkVisionAndBodyCollision(me, battleField.getImmobileEntities());
			checkVisionAndBodyCollision(me, battleField.getPlayers());
			limit(me);
		}
		for (Entity projectile : battleField.getEnemyProjectiles()) {
			if(outOfRange(projectile.getPosition().y, UnboundConstants.WORLDHEIGHT) || outOfRange(projectile.getPosition().x, UnboundConstants.WORLDWIDTH))
				projectile.setActive(false);
			checkBodyCollision(projectile, battleField.getCollectors());
			checkBodyCollision(projectile, battleField.getImmobileEntities());
			checkBodyCollision(projectile, battleField.getPlayers());
		}
	}

	/**
	 * see updateEnemies()
	 */
	private void updateOwn() {
		for(Entity c : battleField.getCollectors())
			checkBodyCollision(c,battleField.getImmobileEntities());
		for(Entity im : battleField.getImmobileEntities())
			checkVision(im, battleField.getEnemies());
	}

}