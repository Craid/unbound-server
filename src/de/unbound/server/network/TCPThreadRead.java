package de.unbound.server.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import de.unbound.server.view.PanelConnection;



public class TCPThreadRead extends Thread{ // equivalent to MessageThread
	
	private TCPConnectionHandler connectionHandler;
	private Socket skt;
	private BufferedReader br;
	private String userName;
	private ClientConnection client;
	private final String logName = "[TCP Reader] "; //für logs
	
	
	public TCPThreadRead(TCPConnectionHandler connectionHandler, Socket skt,ClientConnection c){
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
					// TODO Auto-generated catch block
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
					for (ClientConnection c : TCPConnectionHandler.getInstance().clients)
					{
						
						
						System.out.println(c.getClientIP().getHostAddress().trim().equalsIgnoreCase(skt.getInetAddress().getHostAddress().trim()));
						if (c.getClientIP().getHostAddress().trim().equalsIgnoreCase(skt.getInetAddress().getHostAddress().trim())) {
							if (port == c.getClientPortTCP()){
							System.out.println("it is done");
							PanelConnection.removeConnectionFromTable(c);
							connection = c;
							}
						}
					}
					
					TCPConnectionHandler.getInstance().clients.remove(connection);
					skt.close();
					
				} else {
					connectionHandler.tellEveryone(userName + ": " + input + "\n");
					client.packagesPerSecondSentTo++;
					PanelConnection.updateRows();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 
		connectionHandler.outputSockets.remove(skt);
	}
}