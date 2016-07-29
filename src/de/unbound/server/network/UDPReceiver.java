package de.unbound.server.network;

import java.net.DatagramSocket;
import java.net.SocketException;


public class UDPReceiver extends Thread{

	private DatagramSocket socket;
	private int portNumber;
	
	public UDPReceiver(int portNumber) {
			try {
				this.socket = new DatagramSocket(portNumber); // Dieser Socket LISTENED auf diesen Port
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	}
	
	
	
}
