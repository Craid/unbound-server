package de.unbound.server.network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import de.unbound.server.view.PanelConnection;


public class TCPThreadAccept extends Thread{
	TCPConnectionHandler connectionHandler;
	ServerSocket srvSkt;
	private final String logName = "[TCP Accepter] "; //f�r logs

	public TCPThreadAccept(TCPConnectionHandler connectionHandler, ServerSocket srvSkt) {
		this.connectionHandler = connectionHandler;
		this.srvSkt = srvSkt;
	}

	public void run() {
		// akzeptiert alle einkommenden Verbindungen und schreibt es in die Hashmap vom TCPConnectionHandler
		while (!srvSkt.isClosed()) {
			try {
				Socket skt = srvSkt.accept();
				ClientConnection newConnection = new ClientConnection(skt.getInetAddress(),skt.getPort()); //bspw. 242.12.42.11:22802
				newConnection.playerID = TCPConnectionHandler.getInstance().clients.size()+1; //Player ID
				TCPConnectionHandler.getInstance().clients.add(newConnection); // 
				
				PanelConnection.insertNewValueToTable(newConnection);
				
				
				System.out.println("Accepted: " + skt.getInetAddress()
						+ ":" + skt.getPort());
				connectionHandler.outputSockets.put(skt,
						new PrintWriter(skt.getOutputStream()));
				// Key = Socket, Value = PrintWriter
				new TCPThreadRead(connectionHandler, skt).start();
				
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
	}
}
