package de.unbound.game.mode.serversurvival.util;

import de.unbound.game.BattleField;
import de.unbound.game.model.entities.Entity;
import de.unbound.game.network.serialization.PacketDeserializer;
import de.unbound.game.network.serialization.PacketDeserializer.DeserializedEntity;
import de.unbound.server.network.ClientConnection;
import de.unbound.server.network.ConnectionHandler;

public class NetworkUpdateHelper {
	
	private BattleField battleField;
	private ConnectionHandler connectionHandler;
	private PacketDeserializer entityDeserializer;
	
	public NetworkUpdateHelper(BattleField battleField, ConnectionHandler handler) {
		this.battleField = battleField;
		this.connectionHandler = handler;
		entityDeserializer = new PacketDeserializer();
	}

	public void updateReceivedPlayers() {
		for (ClientConnection c : connectionHandler.clients) {
			byte[] data = c.getLastPlayerPacket().getData();
			try{
				DeserializedEntity de = entityDeserializer.getDeserializedEntityFromByteArray(data, 8).get(0);
				
				Entity player = battleField.getEntitybyId(c.getPlayerID());
				player.getPosition().x = de.posX;
				player.getPosition().y = de.posY;
			
				player.getDirection().x = de.dirX;
				player.getDirection().y = de.dirY;
			
				player.getUpdateState().getMove().getVelocity().x = de.velX;
				player.getUpdateState().getMove().getVelocity().y = de.velY;
			}catch(Exception e){}
		}
	}
}
