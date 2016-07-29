package de.unbound.server.network;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.SocketException;

public class TCPReceiver extends Thread {

	private ServerSocket socket;
	private int portNumber;

	public TCPReceiver(int portNumber) {

		try {
			this.socket = new ServerSocket(portNumber);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Dieser Socket LISTENED auf diesen Port

	}

}
