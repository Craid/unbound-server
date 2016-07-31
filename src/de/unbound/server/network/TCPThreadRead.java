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
	private final String logName = "[TCP Reader] "; //f�r logs
	
	
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
					System.out.println("Socket closed");
					break;
					
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("Something went wrong in TCPThreadRead");
				}
			}
			
			try {
				//System.out.println("Trying to read line");
				input = br.readLine();
				System.out.println(logName+" TCP Package Message: "+input);
				
				client.packagesPerSecondReceived++;
				if (input.equalsIgnoreCase("EXIT")) {
					exitProcedure();	
				}
				if (input.equalsIgnoreCase("New Player")) {
					System.out.println(logName+"NEW PLAYER DETECTED"); //TODO i don't get it..
					//ConnectionHandler.getInstance().tcpSender.
					PrintWriter outMsg = ConnectionHandler.getInstance().outputSockets.get(skt);
					ArrayList<Entity> playerAndMainBase = new ArrayList<Entity>();
					Entity player = World.getInstance().getBattleField().getPlayers().get(0); //TODO Marwin: Vorsicht! 1. Player? �ndern!
					Entity mainBase = World.getInstance().getBattleField().getMainBase();
					playerAndMainBase.add(player);
					playerAndMainBase.add(mainBase);
					
					PacketSerializer serializer = new PacketSerializer();
					byte[] message = serializer.constructUDPPackage(playerAndMainBase);

							DataOutputStream dOut = new DataOutputStream(skt.getOutputStream());

							dOut.writeInt(message.length); // write length of the message
							dOut.write(message); 
							dOut.flush();
							System.out.println("Sent Player and Main Base");
					
					
					}
				
					connectionHandler.tellEveryone("jou ALPHA");
					//client.packagesPerSecondSentTo++;
					//PanelConnection.updateRows();
				
			} catch (IOException e) {
				try {
					skt.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					System.out.println("close is wrong");
				}
				e.printStackTrace();
				System.out.println("read is wrong");
				exitProcedure();	
				
			}
		} 
		connectionHandler.outputSockets.remove(skt);
	}
	
	public void exitProcedure(){
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
		try {
			skt.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}