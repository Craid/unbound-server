package de.unbound.server.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import de.unbound.game.network.serialization.PacketSerializer;
import de.unbound.server.view.PanelConnection;


public class UDPThreadReceiver extends Thread{

	public DatagramSocket socket;
	private int portNumber;
	private boolean running = false;
	private int countToCheck = 0;
	private ClientConnection c;
	public DatagramPacket lastPacket;
	private final String logName = "[UDP Receiver] "; //für logs
	private static PacketSerializer entitySerializer = new PacketSerializer();
	
	public UDPThreadReceiver(int portNumber) {
			try {
				this.socket = new DatagramSocket(portNumber); // Dieser Socket LISTENED auf diesen Port
				System.out.println(logName+"Started with Port: "+portNumber);
			} catch (SocketException e) {
				e.printStackTrace();
			} 
	}
	
	public void run(){
		running = true;
		while (running){
			byte[] data = new byte[2048];
			lastPacket = new DatagramPacket(data, data.length);
			System.out.println(logName+ "Locking... Waiting for a Packet...");
			try {
				socket.receive(lastPacket); // Diese Methode "blockt" solange, bis ein Packet ankommt!
				checkIfPlayerAlreadyInList(lastPacket.getAddress(), lastPacket.getPort());
				checkInput(lastPacket);
				countToCheck++;
				String message = new String(lastPacket.getData());
				if (message.trim().equalsIgnoreCase("exit")) break;
				
				System.out.println(logName+"I got a Packet!");
				//byte[] player = entitySerializer.getPlayerAsByteArray(); // das ist nur aus testzwecken hier
				//ConnectionHandler.getInstance().udpSender.sendData(player,packet.getAddress(), packet.getPort());
			} catch (IOException e) {
				// Irgendetwas ist schief gelaufen..
				e.printStackTrace();
			}
			String message = new String(lastPacket.getData()); // hier versuchen wir aus dem Packet den String zu lesen
			System.out.println(logName+"Got a Packet from: > ["+ lastPacket.getAddress().getHostAddress()+":"+lastPacket.getPort()+"]: \n"+"Message: "+ message);
			if (message.trim().equalsIgnoreCase("exit")){
				//System.out.println(logName+"Returning pong...");
				//sendData((Integer.toString(packet.getPort())).getBytes(), packet.getAddress(), packet.getPort()); // getAddress/Port holt die IP Addresse/Port vom Sender!
				//sendData("pong".getBytes(), packet.getAddress(), packet.getPort()); // getAddress/Port holt die IP Addresse/Port vom Sender!
				
				
			
				running = false;
				System.out.println(logName+"Exiting the UDP Receive Loop!");
			}
		}
		socket.close();
		System.out.println(logName+"Closed!");
	}
	

	public void checkInput(DatagramPacket input){
		String message = new String(lastPacket.getData());
		
		if (message.trim().equalsIgnoreCase("ClientUDP")){
			ConnectionHandler.getInstance().udpSender.sendData("Hi it's the Server!".getBytes(), lastPacket.getAddress(), lastPacket.getPort());
			c.udpPackagesSentTo++;
		}
		
	}
	
	public void checkIfPlayerAlreadyInList(InetAddress ip,int port){
		boolean checkIfAlreadyInList = false;
		for (ClientConnection c : ConnectionHandler.getInstance().clients)
		{
			
			
			//System.out.println(c.getClientIP().getHostAddress().trim().equalsIgnoreCase(skt.getInetAddress().getHostAddress().trim()));
			if (c.getClientIP().getHostAddress().trim().equalsIgnoreCase(ip.getHostAddress().trim())) {
				//if (port == c.getClientPortTCP()){
				System.out.println("Player is already known to the System");
				//System.exit(1);
				this.c = c;
				c.udpPackagesReceived++;
				c.clientPortUDP = port;
				checkIfAlreadyInList = true;
				PanelConnection.updateRows();
				//}
			}
			
		}
		if (!checkIfAlreadyInList){
			//System.exit(1);
				System.out.println("Player is new to the System without a TCP Connection");
				ClientConnection newConnection = new ClientConnection(); //bspw. 242.12.42.11:22802
				newConnection.clientIP = ip;
				newConnection.clientPortUDP = port;
				newConnection.playerID = ConnectionHandler.getInstance().getLowestIdFromConnectionList(); //Player ID
				newConnection.setClientPortUDP(port);
				ConnectionHandler.getInstance().clients.add(newConnection); // 
				
				//PanelConnection.insertNewValueToTable(newConnection);
				PanelConnection.updateRows();
				
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
