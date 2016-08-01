package de.unbound.game.mode.serversurvival;

import de.unbound.game.factories.EntityFactory;
import de.unbound.game.model.entities.Entity;
import de.unbound.game.model.state.update.UpdateStateImmobile;
import de.unbound.game.model.state.update.UpdateStateMobile;
import de.unbound.game.model.state.update.UpdateStatePlayer;
import de.unbound.game.model.state.update.UpdateStateProjectile;
import de.unbound.utility.UnboundConstants;

public class ServerSurvivalEntityFactory extends EntityFactory {
	
	private int id;
	
	public ServerSurvivalEntityFactory(String race, boolean hostile) {
		super(race, hostile);
		id = 0;
	}
	
	protected void updateTypeAttributes(Entity e,String type) {
		if(type.contains(UnboundConstants.MobileEntity.Player.name()))
			e.setUpdateState(new UpdateStatePlayer(e));
		else if(type.contains(UnboundConstants.MobileEntity.Projectile.name()))
			e.setUpdateState(new UpdateStateProjectile(e));
		else if(!e.isImmobile())
			e.setUpdateState(new UpdateStateMobile(e));
		else
			e.setUpdateState(new UpdateStateImmobile(e));
		e.setId(id++);
	}

}
