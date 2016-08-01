package de.unbound.server.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import de.unbound.game.World;
import de.unbound.game.network.serialization.ByteBuilderHelper;
import de.unbound.game.network.serialization.PacketDeserializer;
import de.unbound.game.network.serialization.PacketDeserializer.DeserializedEntity;
import de.unbound.game.network.serialization.PacketSerializer;

public class UDPSender extends Thread{

	public DatagramSocket socket;
	private DatagramPacket packet;
	private int portNumber;
	private final String logName = "[UDP Sender] "; //für logs
	PacketSerializer serializer;
	ByteBuilderHelper byteBuilder;
	
	public UDPSender(DatagramSocket sharedSocketWithReceiver) {
		packet = new DatagramPacket(new byte[0], 0);
		this.serializer = new PacketSerializer();
		this.byteBuilder = new ByteBuilderHelper();
			
				this.socket = sharedSocketWithReceiver; // Dieser Socket LISTENED auf diesen Port
				System.out.println(logName+"Started with Port: "+sharedSocketWithReceiver.getLocalPort());
		
			this.portNumber = sharedSocketWithReceiver.getLocalPort();
	}
	public void sendAllEntitiesToAllPlayers(){
		//Package bauen
		byte[] allEntitiesAndTimeStamp = serializer.constructUDPPackage(World.getInstance().getBattleField().getGameObjects());
		//PacketDeserializer d = new PacketDeserializer();
		//for(DeserializedEntity e : d.getDeserializedEntityFromByteArray(allEntitiesAndTimeStamp, 8))
		//	System.out.println(e.posX + " : " + e.posY);
		//2147483647 = max int
		
		
		//System.out.println("\n\n\n\n\n");
		
		this.packet.setData(allEntitiesAndTimeStamp);
		// for each alle clients
		for (ClientConnection c : ConnectionHandler.getInstance().clients){
			sendData(allEntitiesAndTimeStamp,c.getClientIP(),c.getClientPortUDP());
		}
		// send
	}
	
	public void sendData(byte[] data, InetAddress ipAddressReceiver, int portReceiver){
		packet.setData(data,0,data.length); //data;offset;länge des Packets
		packet.setAddress(ipAddressReceiver); // 188.58.54.66 example
		packet.setPort(portReceiver);// 11333 = Port
		//System.out.println(logName+" Trying to Send the Data to a Client...");
		try {
			this.socket.send(packet);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	
	public void sendEmptyDataToSelf(){ //in order to close socket
		byte[] data = "exit".getBytes();
		packet.setData(data,0,data.length); //data;offset;länge des Packets
		try {
			sendData(data, InetAddress.getLocalHost(), portNumber);
		} catch (UnknownHostException e2) {
			e2.printStackTrace();
		}
		System.out.println(logName+" Send Data to self...");
	}
	
	public void close(){
		this.socket.close();
		System.out.println(logName+"Closed!");
	}
	
}
