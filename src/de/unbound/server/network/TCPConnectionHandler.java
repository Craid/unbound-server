package de.unbound.server.network;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import de.unbound.game.World;

public class TCPConnectionHandler {

	TCPConnectionHandler connectionHandler;
	TCPSender sender;
	UDPReceiver udpReceiver;
	UDPSender udpSender;
	ServerSocket serverSocket;
	int portNumber;
	HashMap<Socket, PrintWriter> outputSockets;
	public ArrayList<ClientConnection> clients;
	private final String logName = "[Connection Handler] "; //für logs
	public static TCPConnectionHandler instance;
	
	public static TCPConnectionHandler getInstance(){
		if(instance == null)
			instance = new TCPConnectionHandler();
		return instance;
	}
	

	public TCPConnectionHandler(){
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
		udpReceiver = new UDPReceiver(portNumber+1);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		outputSockets = new HashMap<Socket, PrintWriter>();
		new TCPThreadAccept(this, serverSocket).start(); // empfängt alle requests und schreibt sie in die Hashmap
		System.out.println("Unbound Server started. Waiting for clients to connect...");
		sender = new TCPSender(this);
		
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
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
	}
	

	
	public void tellEveryone(String msg){
		sender.tellEveryone(msg);
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
	
}
