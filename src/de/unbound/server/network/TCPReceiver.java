package de.unbound.server.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.SocketException;

public class TCPReceiver extends Thread {

	private ServerSocket serverSocket;
	private Socket socket;
	private DataInputStream inputStream;
	private DataOutputStream outputStream;
	private int portNumber;

	public TCPReceiver(int portNumber) {

		try {
			this.serverSocket = new ServerSocket(portNumber);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Dieser Socket LISTENED auf diesen Port

	}
	
	public void initialize(){
		
		
		
	}
	
	public int getPortNumber(){
		return portNumber;
	}

}
