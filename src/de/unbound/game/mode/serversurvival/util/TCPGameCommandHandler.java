package de.unbound.game.mode.serversurvival.util;
import java.util.ArrayList;

import de.unbound.game.World;
import de.unbound.game.network.TCPConnector;
import de.unbound.server.network.ClientConnection;
import de.unbound.utility.UnboundConstants;

public class TCPGameCommandHandler {
	
	private ArrayList<ClientConnection> clients;

	public TCPGameCommandHandler(ArrayList<ClientConnection> clients){
		this.clients = clients;
	}
	
	public void handleInput(){
		for (ClientConnection c : clients){
			String[] commands = c.getCommands();
				for(String command : commands){
					//System.out.println("[Executing Server Command]:"+command+"From Player: "+c.getPlayerName()+" | PlayerID: "+c.getPlayerID());
					handleCommand(command,c);
			}
		}
	}

	private void handleCommand(String input, ClientConnection client) {
		String[] command = input.split(":");
		//if(command[0].equals("Tower"))
		//{float x = Float.parseFloat(command[1]);
		//float y = Float.parseFloat(command[2]);}
		
		if (command[0].equalsIgnoreCase("Tower")){
			World.getInstance().createTower(new Float(command[1]), new Float(command[2]));
		}
		if (command[0].equalsIgnoreCase("Projectile")){
			World.getInstance().createProjectile(World.getInstance().getBattleField().getEntitybyId(client.getPlayerID()));
			//World.getInstance().getGameUpdate().appendCommands("Projectile");
		}
	}
	
}
