package de.unbound.server.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;


public class UDPReceiver extends Thread{

	private DatagramSocket socket;
	private int portNumber;
	private final String logName = "[UDP Receiver] "; //für logs
	
	public UDPReceiver(int portNumber) {
			try {
				this.socket = new DatagramSocket(portNumber); // Dieser Socket LISTENED auf diesen Port
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	}
	
	
	public void run(){
		while (true){
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			System.out.println(logName+ "Trying to read a Packet...");
			try {
				socket.receive(packet); // Diese Methode "blockt" solange, bis ein Packet ankommt!
				System.out.println(logName+"I got a Packet!");
			} catch (IOException e) {
				// Irgendetwas ist schief gelaufen..
				e.printStackTrace();
			}
			String message = new String(packet.getData()); // hier versuchen wir aus dem Packet den String zu lesen
			System.out.println("[GAME SERVER] CLIENT > ["+ packet.getAddress().getHostAddress()+":"+packet.getPort()+"]: "+ message);
			if (message.trim().equalsIgnoreCase("ping")){
				System.out.println("[GAME SERVER] Returning pong...");
				sendData((Integer.toString(packet.getPort())).getBytes(), packet.getAddress(), packet.getPort()); // getAddress/Port holt die IP Addresse/Port vom Sender!
				//sendData("pong".getBytes(), packet.getAddress(), packet.getPort()); // getAddress/Port holt die IP Addresse/Port vom Sender!
				
			}
		}
	}
	
	
	
	
	
	
	public int getPortNumber(){
		return portNumber;
	}
	
	
}
