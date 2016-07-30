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
	private final String logName = "[TCP Sender] "; //für logs
	
	public TCPSender(int portNumber) {
			this.socket = new Socket(); 
	}
	
	
	public void sendData(byte[] data, InetAddress ipAddressReceiver, int portReceiver){
		// needs to be implemented
	}
}
