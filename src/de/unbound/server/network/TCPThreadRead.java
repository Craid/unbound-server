package de.unbound.server.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import de.unbound.server.view.PanelConnection;



public class TCPThreadRead extends Thread{ // equivalent to MessageThread
	
	private final String logName = "[TCP Reader] "; //für logs
	private ConnectionHandler connectionHandler;
	private Socket skt;
	private BufferedReader br;
	private String userName;
	private ClientConnection client;
	private TCPSender tcpSender;
	
	
	public TCPThreadRead(ConnectionHandler connectionHandler, Socket skt,ClientConnection c){
		try {
			this.connectionHandler = connectionHandler;
			this.skt = skt;
			this.client = c;
			this.tcpSender = connectionHandler.tcpSender;
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
			PanelConnection.updateRows(connectionHandler);
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
				System.out.println(logName+" TCP Message from: "+skt.getInetAddress().getHostName()+":"+skt.getPort()+"->"+input);
				checkInput(input);
				client.tcpPackagesReceived++;
				PanelConnection.updateRows(connectionHandler);
				
					tcpSender.tellOne("ich schicke einfach mal so ne Nachricht",skt);
					//client.packagesPerSecondSentTo++;
					//PanelConnection.updateRows();
				
			} catch (IOException e) {
				try {
					skt.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					System.out.println(logName+"Trying to close the TCP Socket "+skt.getPort()+" was not successfull");
				}
				e.printStackTrace();
				System.out.println(logName+"Trying to read was not successfull");
				exitProcedure();	
				
			}
		} 
		connectionHandler.outputSockets.remove(skt);
	}
	public void checkInput(String input){

		if (input.matches("[0-9]+") && input.length() > 2) {
			client.clientPortUDP = new Integer(input);
		}
		
		if (input.equalsIgnoreCase("EXIT")) {
			exitProcedure();	
		}
		if (input.equalsIgnoreCase("New Player")) {
			System.out.println(logName+"NEW PLAYER DETECTED"); 
			
			tcpSender.sendPlayerAndMainBase(skt);

			}
	}
	public void exitProcedure(){
		connectionHandler.outputSockets.remove(skt);
		connectionHandler.tellEveryone(userName + " has signed off.\n");
		int port = skt.getPort();
		ClientConnection connection = null;
		for (ClientConnection c : connectionHandler.clients)
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
		
		connectionHandler.clients.remove(connection);
		try {
			skt.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PanelConnection.updateRows(connectionHandler);
	}
}