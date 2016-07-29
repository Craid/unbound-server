package de.unbound.server.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPSender extends Thread{

	private DatagramSocket socket;
	private int portNumber;
	private final String logName = "[UDP Receiver] "; //für logs
	
	public UDPSender(int portNumber) {
			try {
				this.socket = new DatagramSocket(portNumber); // Dieser Socket LISTENED auf diesen Port
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	}
	
	
	public void sendData(byte[] data, InetAddress ipAddressReceiver, int portReceiver){
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddressReceiver, portReceiver); // 11333 = Port
		System.out.println("[GAME SERVER] Trying to Send the Data to a Client...");
		try {
			this.socket.send(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
