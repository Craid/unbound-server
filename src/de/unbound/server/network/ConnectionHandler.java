package de.unbound.server.network;

import de.unbound.game.World;

public class ConnectionHandler {

	static ConnectionHandler instance;
	private TCPConnectionHandler tcpReceiver;
	private TCPSender tcpSender;
	private UDPReceiver udpReceiver;
	private UDPSender udpSender;
		
	public static ConnectionHandler getInstance(){
		if(instance == null)
			instance = new ConnectionHandler();
		return instance;
	}
	
	
}
