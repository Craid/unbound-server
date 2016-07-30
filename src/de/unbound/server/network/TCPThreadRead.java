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
	private final String logName = "[TCP Reader] "; //für logs
	
	
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
		while (!skt.isClosed()) {
			if (connectionHandler.serverSocket.isClosed()){
				try {
					skt.close();
					break;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			try {
				input = br.readLine();
				System.out.println("lool");
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
		} 
		connectionHandler.outputSockets.remove(skt);
	}
}