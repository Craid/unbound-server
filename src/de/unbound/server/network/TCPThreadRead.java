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

	private void getInitialRequestAndRespond() {
		System.out.println("Waiting for Username and Socket Port from IP/Port: " + skt.getInetAddress().getHostAddress()+":"+skt.getPort());
		try {
			String input = br.readLine();
			System.out.println(logName+"[TCP Thread Read]"+input);
			String[] commands = input.split(":"); 
			client.tcpPackagesReceived++;
			client.playerName = commands[0];
			client.clientPortUDP = new Integer(commands[1]);

			tcpSender.sendPlayerAndMainBase(skt);
			client.tcpPackagesSentTo++;
			
			//connectionHandler.tellEveryone(userName + " has joined the game!\n");
			client.setPlayerName(userName);
			PanelConnection.updateRows(connectionHandler);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
//	private void getPlayerAndMainBaseRequest() {
//		System.out.println("Waiting for Player and Main Base Request " + skt.getPort());
//		try {
//			br.readLine(); // Expecting Request for Player and Main Base
//			client.tcpPackagesReceived++;
//
//			System.out.println(logName+"NEW PLAYER DETECTED"); 
//			tcpSender.sendPlayerAndMainBase(skt);
//			client.tcpPackagesSentTo++;
//			
//			PanelConnection.updateRows(connectionHandler);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//	private void getUDPPortNumberFromClient() {
//		System.out.println("Waiting for UDP Port Number " + skt.getPort());
//		try {
//			String udpNumberAsString = br.readLine();
//			client.clientPortUDP = new Integer(udpNumberAsString);
//			PanelConnection.updateRows(connectionHandler);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

	public void run() {
		getInitialRequestAndRespond(); // 1. Nachricht vom Client bearbeiten
		//getPlayerAndMainBaseRequest(); // 2. Nachricht vom Client bearbeiten
		//getUDPPortNumberFromClient(); // 3. Nachricht vom Client bearbeiten
		String input = "";
		client.setConnected(true);
		while (!skt.isClosed()&&client.isConnected()) {
			if (connectionHandler.serverSocket.isClosed()){
				try {
					skt.close();
					System.out.println("Socket closed");
					break;
					
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println(logName+"Something went wrong in TCPThreadRead");
				}
			}
			
			try {
				input = br.readLine(); //("Trying to read line");
				if (input.equalsIgnoreCase("EXIT")) { //das sollte aber noch drin bleiben, um die Verbindung korrekt zu schließen
					exitProcedure();	
				}
				//System.out.println(logName+" TCP Message from: "+skt.getInetAddress().getHostName()+":"+skt.getPort()+"->"+input);
				//checkInput(input); nicht mehr nötig, da wir die ersten 3 Nachrichten vom Client vor der Schleife abfangen
				client.appendCommands(input);
				
					client.tcpPackagesReceived++;
					PanelConnection.updateRows(connectionHandler);
				
				//tcpSender.tellOne("Ich habe deine TCP-Nachricht bekommen",skt);
				
			} catch (IOException e) {
				try {
					skt.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					//e1.printStackTrace();
					System.out.println(logName+"Trying to close the TCP Socket "+skt.getPort()+" was not successfull");
				}
				//e.printStackTrace();
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
		//connectionHandler.tellEveryone(userName + " has signed off.\n");
		client.setConnected(false);

	}
}