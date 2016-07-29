package de.unbound.server.network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class TCPSender extends Thread{
	private Socket socket;
	private DataOutputStream outputStream;
	private int portNumber;
	private final String logName = "[UDP Receiver] "; //für logs
	
	public TCPSender(int portNumber) {
			this.socket = new Socket(); // Dieser Socket LISTENED auf diesen Port 
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
