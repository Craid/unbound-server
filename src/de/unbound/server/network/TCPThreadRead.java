package de.unbound.server.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;



public class TCPThreadRead extends Thread{ // equivalent to MessageThread
	
	private TCPConnectionHandler connectionHandler;
	private Socket skt;
	private BufferedReader br;
	private String userName;
	
	
	public TCPThreadRead(TCPConnectionHandler connectionHandler, Socket skt){
		try {
			this.connectionHandler = connectionHandler;
			this.skt = skt;
			br = new BufferedReader(new InputStreamReader(
					skt.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void getUserName() {
		System.out.println("Waiting for Username from " + skt.getPort());
		try {
			userName = br.readLine();
			connectionHandler.tellEveryone(userName + " has joined the Chat!\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		getUserName();
		String input = "";
		do {
			try {
				input = br.readLine();
				if (input.equals("EXIT")) {
					connectionHandler.outputSockets.remove(skt);
					connectionHandler.tellEveryone(userName + " has signed off.\n");
					skt.close();
				} else {
					connectionHandler.tellEveryone(userName + ": " + input + "\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} while (!skt.isClosed());
		connectionHandler.outputSockets.remove(skt);
	}
}