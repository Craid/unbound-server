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
	private ConnectionHandler connectionHandler;
	private int portNumber;
	
	private boolean running = false;
	
	public UDPThreadReceiver(ConnectionHandler connectionHandler, int portNumber) {
		this.connectionHandler = connectionHandler;
		try {
			this.socket = new DatagramSocket(portNumber); 
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
			try {
				socket.receive(lastPacket); // Diese Methode "blockt" solange, bis ein Packet ankommt!
				ClientConnection c = getCorrespondentConnection(lastPacket);
				
				checkInput(lastPacket);
			} catch (Exception e){
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
			//c.udpPackagesSentTo++;
		}
		if (message.trim().equalsIgnoreCase("exit")){
			running = false;
		}
		if (message.length()>11){
			getCorrespondentConnection(input).setLastPlayerPacket(input);
		}
	}

	public ClientConnection getCorrespondentConnection(DatagramPacket packet){
		InetAddress ip = packet.getAddress();
		int port = packet.getPort();
		for (ClientConnection client : connectionHandler.clients)
		{
		
			if (client.getClientIP().getHostAddress().trim().equalsIgnoreCase(ip.getHostAddress().trim())) {
				if (client.getClientPortUDP() == port){
					client.setLastPlayerPacket(packet);
					client.udpPackagesReceived++;
					PanelConnection.updateRows(connectionHandler);
					return client;
				}
			}
		}
		return null;
		
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
