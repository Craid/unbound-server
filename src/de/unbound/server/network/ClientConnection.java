package de.unbound.server.network;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import de.unbound.game.network.serialization.PacketDeserializer;

public class ClientConnection {

InetAddress clientIP;
	int clientPortTCP;
	int clientPortUDP;
	String statusTCP;
	String statusUDP;
	int playerID;
	String playerName;
	DatagramPacket lastPlayerPacket;
	PacketDeserializer entityDeserializer;
	Socket clientSocket;
	String commands;

	float latency;
	long tcpPackagesReceived;
	long tcpPackagesSentTo;
	long udpPackagesReceived;
	long udpPackagesSentTo;

	public ClientConnection() {
		init();
	}

	public ClientConnection(Socket socket, InetAddress clientIP, int clientPortTCP) {
		init();
		this.clientIP = clientIP;
		this.clientPortTCP = clientPortTCP;
		byte[] data = new byte[37];
		this.lastPlayerPacket = new DatagramPacket(data, data.length);
		this.clientSocket = socket;
	}

	public void init() {
		commands = "";
		statusTCP = "Pending...";
		statusUDP = "Pending...";
		clientPortTCP = 0;
		clientPortUDP = 10000;
		tcpPackagesReceived = 0;
		tcpPackagesSentTo = 0;
		playerName = "Pending...";
		playerID = 0;
		entityDeserializer = new PacketDeserializer();
		try {
			clientIP = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public String[] getCommands() {
		String[] allCommands = new String[0];
		if (commands.length() != 0) {
			synchronized (commands) {
				allCommands = new String(commands).split("\n");
				commands = "";
			}
		}
		return allCommands;
	}

	public void appendCommands(String commands) {
		synchronized (this.commands) {
			this.commands += commands + "\n";
		}
	}
	
	

	public InetAddress getClientIP() {
		return clientIP;
	}

//	public DeserializedEntity getDeserializedEntityFromLastPacket() {
//		if (getLastPlayerPacket() != null) {
//			byte[] sum = getLastPlayerPacket().getData();
//			DeserializedEntity de = entityDeserializer.getDeserializedEntityFromByteArray(sum, 8).get(1);
//			Entity e = World.getInstance().getBattleField().getEntitybyId(this.getPlayerID());
//			if (e.getId() == de.id) {
//				return de;
//			}
//		}
//		return null;
//	}

	
	
	public void setClientIP(InetAddress clientIP) {
		this.clientIP = clientIP;
	}

	public int getClientPortTCP() {
		return clientPortTCP;
	}

	public void setClientPortTCP(int clientPortTCP) {
		this.clientPortTCP = clientPortTCP;
	}

	public int getClientPortUDP() {
		return clientPortUDP;
	}

	public void setClientPortUDP(int clientPortUDP) {
		this.clientPortUDP = clientPortUDP;
	}

	public String getPlayerName() {
		return playerName;
	}
	public void setSocket(Socket skt){
		this.clientSocket = skt;
	}
	public Socket getSocket(){
		return this.clientSocket;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public DatagramPacket getLastPlayerPacket() {
		return lastPlayerPacket;
	}

	public void setLastPlayerPacket(DatagramPacket lastPlayerPacket) {
		this.lastPlayerPacket = lastPlayerPacket;
	}

	public String getStatusUDP() {
		return statusUDP;
	}

	public void setStatusUDP(String status) {
		this.statusUDP = status;
	}

	public String getStatusTCP() {
		return statusTCP;
	}

	public void setStatusTCP(String status) {
		this.statusTCP = status;
	}

	public int getPlayerID() {
		return playerID;
	}

	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}

	public float getLatency() {
		return latency;
	}

	public void setLatency(float latency) {
		this.latency = latency;
	}

	public long getTcpPackagesReceived() {
		return tcpPackagesReceived;
	}

	public void setTcpPackagesReceived(long tcpPackagesReceived) {
		this.tcpPackagesReceived = tcpPackagesReceived;
	}

	public long getTcpPackagesSentTo() {
		return tcpPackagesSentTo;
	}

	public void setTcpPackagesSentTo(long tcpPackagesSentTo) {
		this.tcpPackagesSentTo = tcpPackagesSentTo;
	}

	public long getUdpPackagesReceived() {
		return udpPackagesReceived;
	}

	public void setUdpPackagesReceived(long udpPackagesReceived) {
		this.udpPackagesReceived = udpPackagesReceived;
	}

	public long getUdpPackagesSentTo() {
		return udpPackagesSentTo;
	}

	public void setUdpPackagesSentTo(long udpPackagesSentTo) {
		this.udpPackagesSentTo = udpPackagesSentTo;
	}

}
