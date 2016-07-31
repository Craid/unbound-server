package de.unbound.server.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import de.unbound.game.BattleField;
import de.unbound.game.World;
import de.unbound.game.model.entities.Entity;

public class UDPSender extends Thread{

	public DatagramSocket socket;
	private DatagramPacket packet;
	private int portNumber;
	private final String logName = "[UDP Sender] "; //für logs
	
	public UDPSender(int portNumber) {
		packet = new DatagramPacket(new byte[0], 0);
			try {
				this.socket = new DatagramSocket(portNumber); // Dieser Socket LISTENED auf diesen Port
				System.out.println(logName+"Started with Port: "+portNumber);
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			this.portNumber = portNumber;
	}
	
	
	public void sendData(byte[] data, InetAddress ipAddressReceiver, int portReceiver){
		packet.setData(data,0,data.length); //data;offset;länge des Packets
		packet.setAddress(ipAddressReceiver); // 188.58.54.66 example
		packet.setPort(portReceiver);// 11333 = Port
		System.out.println(logName+" Trying to Send the Data to a Client...");
		try {
			this.socket.send(packet);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendAllEntitiesToAllPlayers(){
		
	}
	
	
	public void sendEmptyDataToSelf(){ //in order to close socket
		byte[] data = "exit".getBytes();
		packet.setData(data,0,data.length); //data;offset;länge des Packets
		try {
			sendData(data, InetAddress.getLocalHost(), portNumber-1);
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
