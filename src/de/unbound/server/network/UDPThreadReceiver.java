package de.unbound.server.network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import de.unbound.server.view.PanelConnection;


public class UDPThreadReceiver extends Thread{
	
	private final String logName = "[UDP Receiver] "; //für logs
	public DatagramSocket socket;
	public DatagramPacket lastPacket;
	private ClientConnection c;
	private ConnectionHandler connectionHandler;
	private int portNumber;
	
	private boolean running = false;
	
	public UDPThreadReceiver(ConnectionHandler connectionHandler, int portNumber) {
			try {
				this.socket = new DatagramSocket(portNumber); 
				System.out.println(logName+"Started with Port: "+portNumber);
			} catch (SocketException e) {
				e.printStackTrace();
			} 
	}
	
	public void run(){
		running = true;
		byte[] data = new byte[2048];
		while (running){
			
			lastPacket = new DatagramPacket(data, data.length);
			try {
				socket.receive(lastPacket); // Diese Methode "blockt" solange, bis ein Packet ankommt!
				checkIfPlayerAlreadyInList(lastPacket.getAddress(), lastPacket.getPort());
				checkInput(lastPacket);
				} 	
					catch (Exception e)
					{
				
					}
			}
		System.out.println(logName+"Exiting the UDP Receive Loop!");
		socket.close();
		System.out.println(logName+"Closed!");
	}
	

	public void checkInput(DatagramPacket input){
		String message = new String(input.getData());
		
		if (message.trim().equalsIgnoreCase("ClientUDP")){
			connectionHandler.udpSender.sendData("Hi it's the Server!".getBytes(), input.getAddress(), input.getPort());
			c.udpPackagesSentTo++;
		}
		if (message.trim().equalsIgnoreCase("exit")){
			running = false;
		}
		if (message.length()>11){
			c.setLastPlayerPacket(input);
			System.out.println("Last Packet is a player. Player set!");
		}
	}
	public void checkIfPlayerAlreadyInListOLD(InetAddress ip,int port){
		boolean checkIfAlreadyInList = false;
		for (ClientConnection c : connectionHandler.clients)
		{
			
			
			//System.out.println(c.getClientIP().getHostAddress().trim().equalsIgnoreCase(skt.getInetAddress().getHostAddress().trim()));
			if (c.getClientIP().getHostAddress().trim().equalsIgnoreCase(ip.getHostAddress().trim())) {
				//if (port == c.getClientPortTCP()){
				//System.out.println("Player is already known to the System");
				//System.exit(1);
				this.c = c;
				c.udpPackagesReceived++;
				c.clientPortUDP = port;
				checkIfAlreadyInList = true;
				PanelConnection.updateRows(connectionHandler);
				//}
			}
			
		}
		if (!checkIfAlreadyInList){
			//System.exit(1);
				//System.out.println("Player is new to the System without a TCP Connection");
				ClientConnection newConnection = new ClientConnection(); //bspw. 242.12.42.11:22802
				newConnection.clientIP = ip;
				newConnection.clientPortUDP = port;
				newConnection.playerID = connectionHandler.getLowestIdFromConnectionList(); //Player ID
				newConnection.setClientPortUDP(port);
				connectionHandler.clients.add(newConnection); // 
				
				//PanelConnection.insertNewValueToTable(newConnection);
				PanelConnection.updateRows(connectionHandler);
				
		}
	}
	public void checkIfPlayerAlreadyInList(InetAddress ip,int port){
		System.out.println(ip.getHostName()+":"+port+" -> IP PORT UDP");
		if (this.c == null){
			System.out.println("c is null");
			for (ClientConnection client : connectionHandler.clients)
			{
				if (client.getClientIP().getHostAddress().trim().equalsIgnoreCase(ip.getHostAddress().trim())) {
					if (client.getClientPortUDP() == port){
						this.c = client;
						c.clientPortUDP = port;
						PanelConnection.updateRows(connectionHandler);
						break;
					}
				}
			}
		this.c.udpPackagesReceived++;
		
			
		}
	}
	
	public void toggleRunning(){
		running = !running;
	}
	public void setRunning(boolean active){
		running = active;
	}
	
	public int getPortNumber(){
		return portNumber;
	}
	
}
