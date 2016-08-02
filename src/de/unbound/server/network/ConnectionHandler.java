package de.unbound.server.network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import de.unbound.server.view.PanelConnection;

public class ConnectionHandler {

	private final String logName = "[Connection Handler] "; //für logs
	int 	portNumberTCP;
	int 	portNumberUDP;
	ConnectionHandler connectionHandler;
	public ServerSocket 		serverSocket;
	public TCPThreadAccept 		tcpAccepter; //die neu erstellten Sockets werden in der HashMap gespeichert!
	public TCPSender 			tcpSender; //kann an alle Teilnehmer aus der HashMap TCP-Packets über Sockets verschicken
	public UDPThreadReceiver 	udpReceiver; //empfängt alle UDP-Packages
	public UDPSender 			udpSender; //kann an einzelne Endgeräte oder alle Teilnehmer Packages versenden
	
	HashMap<Socket, PrintWriter> outputSockets;
	public ArrayList<ClientConnection> clients;
	

	public ConnectionHandler(int port){
		this.portNumberTCP = port; //default value
		this.portNumberUDP = port+1;
		clients = new ArrayList<ClientConnection>();
	}
	
	public void startServer(){
		startTCP();
		startUDP();
	}
	public void stopServer(){
		stopTCP();
		stopUDP();
		clients.clear();
		PanelConnection.updateRows(connectionHandler);
	}
	public void startUDP(){
		udpReceiver = new UDPThreadReceiver(this,portNumberUDP);
		udpReceiver.start();
		udpSender = new UDPSender(this,udpReceiver.socket);
	}
	public void stopUDP(){
		udpReceiver.setRunning(false);
		udpSender.sendEmptyDataToSelf();
		udpSender.close();
	}
	
	public void startTCP(){
		try {
			serverSocket = new ServerSocket(portNumberTCP);
			System.out.println(logName+"Started with Port: "+portNumberTCP);
		} catch (IOException e) {
			e.printStackTrace();
		}
		outputSockets = new HashMap<Socket, PrintWriter>();
		new TCPThreadAccept(this, serverSocket).start(); // empfängt alle requests und schreibt sie in die Hashmap
		System.out.println("Unbound Server started. Waiting for clients to connect...");
		tcpSender = new TCPSender(this);
		
	}
	public void stopTCP(){
		int countSockets = 0;
		try {
			if (outputSockets.size() != 0){
				synchronized (outputSockets) {
					for (Socket s : outputSockets.keySet()) {
						s.close();
						countSockets++;
						System.out.println(s.getInetAddress()+" has been closed");
					}
				}
			}
			serverSocket.close();
			System.out.println(countSockets + " Sockets have been closed");
			System.out.println("The Server has stopped");

		} catch (IOException e) {
			e.printStackTrace();
			
		}
	}
	
	public ClientConnection getClientConnectionByPlayerID(int id){
		for (ClientConnection c : clients){
			if (c.getPlayerID()==id){
				return c;
			}
		}
		return null;
		
	}
	
	public ClientConnection getClientConnection(InetAddress ip,int port){
		System.out.println(logName+"looking for connections...");
		for (ClientConnection c : clients)
		{
			if (c.getClientIP().getHostAddress().trim().equalsIgnoreCase(ip.getHostAddress().trim())) {
				if (port == c.getClientPortTCP()){
					System.out.println("Player is already known to the System");
					PanelConnection.updateRows(connectionHandler);
					return c;
				}
			}
		}
		return null;
	}
	
	public void tellEveryone(String msg){
		tcpSender.tellEveryone(msg);
	}

	public ArrayList<Socket> getOutputSockets(){
		ArrayList<Socket> sockets = new ArrayList<Socket>();
		if (outputSockets.size() != 0){
			synchronized (outputSockets) {
				for (Socket s : outputSockets.keySet()) {
					System.out.println(s.getInetAddress()+" has been closed");
					sockets.add(s);
				}
			}
		}
		return sockets;
	}
	
	public int getLowestIdFromConnectionList(){ //TODO delete
		int i = 1;
		for (ClientConnection c : clients){
			if (c.playerID<=i) i++;
		}
		return 111;
	}
	
	public int getPortNumberTCP() {
		return portNumberTCP;
	}


	public int getPortNumberUDP() {
		return portNumberUDP;
	}

}
