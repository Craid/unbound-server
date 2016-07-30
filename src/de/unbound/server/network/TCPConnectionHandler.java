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

		public TCPConnectionHandler() throws IOException {
			ServerSocket serverSocket = new ServerSocket(1234);
			outputSockets = new HashMap<Socket, PrintWriter>();
			new AcceptThread(this, serverSocket).start();
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

		class AcceptThread extends Thread {

			TCPConnectionHandler ts;
			ServerSocket srvSkt;

			public AcceptThread(TCPConnectionHandler ts, ServerSocket srvSkt) {
				this.ts = ts;
				this.srvSkt = srvSkt;
			}

			public void run() {
				while (!srvSkt.isClosed()) {
					try {
						Socket skt = srvSkt.accept();
						System.out.println("Accepted: " + skt.getInetAddress()
								+ ":" + skt.getPort());
						ts.outputSockets.put(skt,
								new PrintWriter(skt.getOutputStream()));
						new MessageThread(ts, skt).start();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}

		class MessageThread extends Thread {

			private TCPConnectionHandler ts;
			private Socket skt;
			private BufferedReader br;
			private String userName;

			public MessageThread(TCPConnectionHandler ts, Socket skt) {
				try {
					this.ts = ts;
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
					ts.tellEveryone(userName + " has joined the Chat!\n");
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
							ts.outputSockets.remove(skt);
							tellEveryone(userName + " has signed off.\n");
							skt.close();
						} else {
							tellEveryone(userName + ": " + input + "\n");
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				} while (!skt.isClosed());
				ts.outputSockets.remove(skt);
			}
		}

		

	}
	

