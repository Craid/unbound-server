package de.unbound.server.network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class TCPThreadAccept extends Thread{
	TCPConnectionHandler connectionHandler;
	ServerSocket srvSkt;

	public TCPThreadAccept(TCPConnectionHandler connectionHandler, ServerSocket srvSkt) {
		this.connectionHandler = connectionHandler;
		this.srvSkt = srvSkt;
	}

	public void run() {
		// akzeptiert alle einkommenden Verbindungen und schreibt es in die Hashmap vom TCPConnectionHandler
		while (!srvSkt.isClosed()) {
			try {
				Socket skt = srvSkt.accept();
				System.out.println("Accepted: " + skt.getInetAddress()
						+ ":" + skt.getPort());
				connectionHandler.outputSockets.put(skt,
						new PrintWriter(skt.getOutputStream()));
				// Key = Socket, Value = PrintWriter
				new TCPThreadRead(connectionHandler, skt).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
