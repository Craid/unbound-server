package de.unbound.server.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;


public class UDPReceiver extends Thread{

	private DatagramSocket socket;
	private int portNumber;
	private boolean running = false;
	private final String logName = "[UDP Receiver] "; //für logs
	
	public UDPReceiver(int portNumber) {
			try {
				this.socket = new DatagramSocket(portNumber); // Dieser Socket LISTENED auf diesen Port
				System.out.println(logName+"Started with Port: "+portNumber);
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	}
	
	
	public void run(){
		running = true;
		while (running){
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			System.out.println(logName+ "Locking... Waiting for a Packet...");
			try {
				socket.receive(packet); // Diese Methode "blockt" solange, bis ein Packet ankommt!
				System.out.println(logName+"I got a Packet!");
			} catch (IOException e) {
				// Irgendetwas ist schief gelaufen..
				e.printStackTrace();
			}
			String message = new String(packet.getData()); // hier versuchen wir aus dem Packet den String zu lesen
			System.out.println(logName+"Got a Packet from: > ["+ packet.getAddress().getHostAddress()+":"+packet.getPort()+"]: \n"+"Message: "+ message);
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
