package de.unbound.server.network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import de.unbound.server.view.PanelConnection;


public class TCPThreadAccept extends Thread{
	
	private final String logName = "[TCP Accepter] "; //für logs
	private ConnectionHandler connectionHandler;
	ServerSocket srvSkt;


	public TCPThreadAccept(ConnectionHandler connectionHandler, ServerSocket srvSkt) {
		this.connectionHandler = connectionHandler;
		this.srvSkt = srvSkt;
	}

	public void run() {
		// akzeptiert alle einkommenden Verbindungen und schreibt es in die Hashmap vom TCPConnectionHandler
		while (!srvSkt.isClosed()) {
			try {
				Socket skt = srvSkt.accept();
				ClientConnection newConnection = new ClientConnection(skt.getInetAddress(),skt.getPort()); //bspw. 242.12.42.11:22802
				connectionHandler.clients.add(newConnection); // 
				PanelConnection.insertNewValueToTable(newConnection);
				System.out.println("Accepted: " + skt.getInetAddress()+ ":" + skt.getPort());
				connectionHandler.outputSockets.put(skt,new PrintWriter(skt.getOutputStream()));
				// Key = Socket, Value = PrintWriter
				new TCPThreadRead(connectionHandler, skt,newConnection).start();
				
			} catch (IOException e) {
				
			}
		}
	}

	
}