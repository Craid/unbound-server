package de.unbound.server.network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import de.unbound.game.World;
import de.unbound.game.model.entities.Entity;
import de.unbound.game.network.serialization.ByteBuilderHelper;
import de.unbound.game.network.serialization.PacketSerializer;
import de.unbound.server.view.PanelConnection;



public class TCPThreadRead extends Thread{ // equivalent to MessageThread
	
	private ConnectionHandler connectionHandler;
	private Socket skt;
	private BufferedReader br;
	private String userName;
	private ClientConnection client;
	private final String logName = "[TCP Reader] "; //für logs
	
	
	public TCPThreadRead(ConnectionHandler connectionHandler, Socket skt,ClientConnection c){
		try {
			this.connectionHandler = connectionHandler;
			this.skt = skt;
			this.client = c;
			br = new BufferedReader(new InputStreamReader(
					skt.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void getUserName() {
		System.out.println("Waiting for Username from " + skt.getPort());
		try {
			userName = br.readLine();
			
			connectionHandler.tellEveryone(userName + " has joined the Chat!\n");
			client.setPlayerName(userName);
			PanelConnection.updateRows();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		getUserName();
		String input = "";
		while (!skt.isClosed()) {
			if (connectionHandler.serverSocket.isClosed()){
				try {
					skt.close();
					break;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			try {

				input = br.readLine();

				
				client.packagesPerSecondReceived++;
				if (input.equalsIgnoreCase("EXIT")) {
					connectionHandler.outputSockets.remove(skt);
					connectionHandler.tellEveryone(userName + " has signed off.\n");
					int port = skt.getPort();
					ClientConnection connection = null;
					for (ClientConnection c : ConnectionHandler.getInstance().clients)
					{
						
						
						System.out.println(c.getClientIP().getHostAddress().trim().equalsIgnoreCase(skt.getInetAddress().getHostAddress().trim()));
						if (c.getClientIP().getHostAddress().trim().equalsIgnoreCase(skt.getInetAddress().getHostAddress().trim())) {
							if (port == c.getClientPortTCP()){
							System.out.println(logName+"Player "+c.playerID+" removed from Connection List");
							PanelConnection.removeConnectionFromTable(c);
							connection = c;
							}
						}
					}
					
					ConnectionHandler.getInstance().clients.remove(connection);
					skt.close();
					
				}
				if (input.equalsIgnoreCase("New Player")) {
					System.out.println("NEW PLAYER DETECTED"); //TODO i don't get it..
					System.out.println("NEW PLAYER DETECTED");
					System.out.println("NEW PLAYER DETECTED");
					System.out.println("NEW PLAYER DETECTED");
					System.out.println("NEW PLAYER DETECTED");
					System.out.println("NEW PLAYER DETECTED");
					System.out.println("NEW PLAYER DETECTED");
					System.out.println("NEW PLAYER DETECTED");
					System.out.println("NEW PLAYER DETECTED");
					System.out.println("NEW PLAYER DETECTED");
					//ConnectionHandler.getInstance().tcpSender.
					PrintWriter outMsg = ConnectionHandler.getInstance().outputSockets.get(skt);
					ArrayList<Entity> playerAndMainBase = new ArrayList<Entity>();
					Entity player = World.getInstance().getBattleField().getPlayers().get(0); //TODO Marwin: Vorsicht! 1. Player? Ändern!
					Entity mainBase = World.getInstance().getBattleField().getMainBase();
					playerAndMainBase.add(player);
					playerAndMainBase.add(mainBase);
					
					PacketSerializer serializer = new PacketSerializer();
					byte[] message = serializer.constructUDPPackage(playerAndMainBase);

							DataOutputStream dOut = new DataOutputStream(skt.getOutputStream());

							dOut.writeInt(message.length); // write length of the message
							dOut.write(message); 
							dOut.flush();
					
					
					}
				
					//connectionHandler.tellEveryone(userName + ": " + input + "\n");
					//client.packagesPerSecondSentTo++;
					//PanelConnection.updateRows();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 
		connectionHandler.outputSockets.remove(skt);
	}
}