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



public class TCPConnectionHandler extends Thread {

	TCPConnectionHandler ts;
	ServerSocket srvSkt;



		HashMap<Socket, PrintWriter> outputSockets;

		public TCPConnectionHandler(int portNumber) throws IOException {
			ServerSocket serverSocket = new ServerSocket(portNumber);
			outputSockets = new HashMap<Socket, PrintWriter>();
			new TCPThreadAccept(this, serverSocket).start();
			System.out.println("Server started. Waiting for clients to connect...");
		}

		public void tellEveryone(String msg) {
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
	

