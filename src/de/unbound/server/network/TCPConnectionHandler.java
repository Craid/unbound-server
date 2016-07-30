package de.unbound.server.network;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;

public class TCPConnectionHandler {

	TCPConnectionHandler connectionHandler;
	TCPSender sender;
	ServerSocket srvSkt;

	HashMap<Socket, PrintWriter> outputSockets;

	public TCPConnectionHandler(int portNumber) throws IOException {
		ServerSocket serverSocket = new ServerSocket(portNumber);
		outputSockets = new HashMap<Socket, PrintWriter>();
		new TCPThreadAccept(this, serverSocket).start();
		System.out.println("Unbound Server started. Waiting for clients to connect...");
		sender = new TCPSender(this);
	}
	public void tellEveryone(String msg){
		sender.tellEveryone(msg);
	}

	public static void main(String[] args){
		try {
			new TCPConnectionHandler(11300);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
