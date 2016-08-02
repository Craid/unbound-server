package de.unbound.game.mode.serversurvival.util;
import de.unbound.game.World;
import de.unbound.game.network.TCPConnector;
import de.unbound.utility.UnboundConstants;

public class TCPGameCommandHandler {
	
	private TCPConnector tcpConnector;

	public TCPGameCommandHandler(TCPConnector tcpC){
		tcpConnector = tcpC;
	}
	
	public void handleInput(){
		String[] commands = tcpConnector.receiver.getCommands();
		for(String command : commands){
			System.out.println("[Executing Server Command]:"+command);
			handleCommand(command);
		}
	}

	private void handleCommand(String command) {
		if (command.equalsIgnoreCase("Respawn")){
			World.getInstance().getBattleField().getPlayers().get(0).setPosition(UnboundConstants.SPAWNPOINT);;
		}
		//TODO CreateTower:PlayerId=x
		//TODO CreateProjectile:PlayerID=x
	}
	
}
