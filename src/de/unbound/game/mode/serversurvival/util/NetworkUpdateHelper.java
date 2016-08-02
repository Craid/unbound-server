package de.unbound.game.mode.serversurvival.util;

import de.unbound.game.BattleField;
import de.unbound.game.model.entities.Entity;
import de.unbound.game.network.serialization.PacketDeserializer.DeserializedEntity;
import de.unbound.server.network.ClientConnection;
import de.unbound.server.network.ConnectionHandler;

public class NetworkUpdateHelper {
	
	private BattleField battleField;
	private ConnectionHandler connectionHandler;
	
	public NetworkUpdateHelper(BattleField battleField, ConnectionHandler handler) {
		this.battleField = battleField;
		this.connectionHandler = handler;
	}

	public void updateReceivedPlayers() {
		for (ClientConnection c : connectionHandler.clients) {
			DeserializedEntity de = c.getDeserializedEntityFromLastPacket();
			if (de!=null){
				System.out.println("de is not null [Klasse: NetworkUpdater]");
			Entity player = battleField.getEntitybyId(c.getPlayerID());
			player.getPosition().x = de.posX;
			player.getPosition().y = de.posY;
			
			player.getDirection().x = de.dirX;
			player.getDirection().y = de.dirY;
			
			player.getUpdateState().getMove().getVelocity().x = de.velX;
			player.getUpdateState().getMove().getVelocity().y = de.velY;
			System.out.println("Do it" +de.posX+ " = de  positionX  e = "+player.getPosition().x+"[Klasse: NetworkUpdater]");
			}
			//System.out.println("de is null");
		}
	}
}
