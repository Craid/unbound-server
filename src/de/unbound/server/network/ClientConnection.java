package de.unbound.server.network;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import de.unbound.game.World;
import de.unbound.game.model.entities.Entity;
import de.unbound.game.network.serialization.PacketDeserializer;
import de.unbound.game.network.serialization.PacketDeserializer.DeserializedEntity;

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

float latency;
long tcpPackagesReceived;
long tcpPackagesSentTo;
long udpPackagesReceived;
long udpPackagesSentTo;




public ClientConnection(){
	init();
}

public ClientConnection(InetAddress clientIP, int clientPortTCP){
	init();
	this.clientIP = clientIP;
	this.clientPortTCP = clientPortTCP;
}
public void init(){
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

public InetAddress getClientIP() {
	return clientIP;
}

public DeserializedEntity getDeserializedEntityFromLastPacket(){
	if (getLastPlayerPacket()!=null){
	byte[] sum = getLastPlayerPacket().getData();
	DeserializedEntity de = entityDeserializer.getDeserializedEntityFromByteArray(sum, 8).get(1);
	entityDeserializer.getTimeStampFromByteArray(sum);
	System.out.println("up until here");
	Entity e = World.getInstance().getBattleField().getEntitybyId(this.getPlayerID());
	//System.out.println("danach");
	//e.setPosition(new Vector2(helper.floatFromByteArray(sum,5),helper.floatFromByteArray(sum,9)));
	if (e.getId()==de.id){ // TODO Das ist der böse code
	//e.getPosition().x = de.posX;
	//e.getPosition().y = de.posY;
	//e.getDirection().x = de.dirX;
	//e.getDirection().y = de.dirY;
	//e.getUpdateState().getMove().getVelocity().x = de.velX;
	//e.getUpdateState().getMove().getVelocity().y = de.velY;
		return de;
	}
	}
	return null;
}

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
