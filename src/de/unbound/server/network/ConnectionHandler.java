package de.unbound.server.network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class ConnectionHandler {

	ConnectionHandler connectionHandler;
	int 	portNumber;
	public ServerSocket 	serverSocket;
	public TCPThreadAccept tcpAccepter; //die neu erstellten Sockets werden in der HashMap gespeichert!
	public TCPSender 		tcpSender; //kann an alle Teilnehmer aus der HashMap TCP-Packets über Sockets verschicken
	public UDPThreadReceiver 	udpReceiver; //empfängt alle UDP-Packages
	public UDPSender 		udpSender; //kann an einzelne Endgeräte oder alle Teilnehmer Packages versenden
	
	HashMap<Socket, PrintWriter> outputSockets;
	public ArrayList<ClientConnection> clients;
	private final String logName = "[Connection Handler] "; //für logs
	
	public static ConnectionHandler instance;
	
	public static ConnectionHandler getInstance(){
		if(instance == null)
			instance = new ConnectionHandler();
		return instance;
	}
	

	private ConnectionHandler(){
		this.portNumber = 11300; //default value
	}
	
	public void startServer(){
		clients = new ArrayList<ClientConnection>();
		startTCP();
		startUDP();
	}
	public void stopServer(){
		stopTCP();
		stopUDP();
		clients.clear();
	}
	public void startUDP(){
		udpReceiver = new UDPThreadReceiver(portNumber+1);
		udpReceiver.start();
		udpSender = new UDPSender(portNumber+2);
	}
	public void stopUDP(){
		udpReceiver.setRunning(false);
		udpSender.sendEmptyDataToSelf();
		udpSender.close();

	}
	
	public void startTCP(){
		try {
			serverSocket = new ServerSocket(portNumber);
			System.out.println(logName+"Started with Port: "+portNumber);
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
	
	public void setPortNumber(int port){
		this.portNumber = port;
	}
	public int getLowestIdFromConnectionList(){
		int i = 1;
		for (ClientConnection c : clients){
			if (c.playerID<=i) i++;
		}
		return i;
	}
}
