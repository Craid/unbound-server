package de.unbound.server.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import de.unbound.game.BattleField;
import de.unbound.game.network.serialization.ByteBuilderHelper;
import de.unbound.game.network.serialization.PacketDeserializer;
import de.unbound.game.network.serialization.PacketSerializer;

public class UDPSender{

	private final String logName = "[UDP Sender] "; //für logs
	public DatagramSocket socket;
	private DatagramPacket packet;
	private int portNumber;
	private ConnectionHandler connectionHandler;
	PacketSerializer serializer;
	ByteBuilderHelper byteBuilder;
	PacketDeserializer tempDes;
	
	public UDPSender(ConnectionHandler connectionHandler, DatagramSocket sharedSocketWithReceiver) {
		packet = new DatagramPacket(new byte[0], 0);
		this.serializer = new PacketSerializer();
		this.byteBuilder = new ByteBuilderHelper();
		this.connectionHandler = connectionHandler;
			
			this.socket = sharedSocketWithReceiver;
			System.out.println(logName+"Started with Port: "+sharedSocketWithReceiver.getLocalPort());
			this.portNumber = sharedSocketWithReceiver.getLocalPort();
			
		tempDes = new PacketDeserializer();
	}
	
	
	
	
	public void sendData(byte[] data, InetAddress ipAddressReceiver, int portReceiver){
		packet.setData(data,0,data.length);
		packet.setAddress(ipAddressReceiver);
		packet.setPort(portReceiver);
		try {
			this.socket.send(packet);
		} catch (IOException e) {
			System.out.println(logName+"Could not send data!");
			e.printStackTrace();
		}

	}
	
	public void sendAllEntitiesToAllPlayers(BattleField battleField){
		//Package bauen
		byte[] allEntitiesAndTimeStamp = serializer.constructUDPPackage(battleField.getGameObjects());
		
		this.packet.setData(allEntitiesAndTimeStamp);
		
		for (ClientConnection c : connectionHandler.clients){
			sendData(allEntitiesAndTimeStamp,c.getClientIP(),c.getClientPortUDP());
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
		System.out.println(logName+"Send Data to self...");
	}
	
	public void close(){
		this.socket.close();
		System.out.println(logName+"Closed!");
	}
	
}
