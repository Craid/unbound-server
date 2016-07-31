package de.unbound.server.network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;

public class TCPSender{
	private Socket socket;
	private ConnectionHandler connectionHandler;
	private final String logName = "[TCP Sender] "; //für logs
	
	public TCPSender(ConnectionHandler connectionHandler) {
			this.connectionHandler = connectionHandler;
	}
	
	
	public void tellEveryone(String msg) {
		//Lade Hashmap...
		HashMap<Socket, PrintWriter> outputSockets = connectionHandler.outputSockets;
		
		System.out.println("Sending: " + msg.trim() + " to " + outputSockets.size() + " Clients.");

		if (outputSockets.size() != 0){
			synchronized (outputSockets) {
				for (Socket s : outputSockets.keySet()) {
					PrintWriter outMsg = outputSockets.get(s);
					outMsg.println(msg);
					outMsg.flush();
				}
			}
		}
	}
}
