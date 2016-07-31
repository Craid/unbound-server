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
	
	public byte[] constructUDPPackage(ArrayList<Entity> entities){
		//timestamp am Anfang
		//dann in einer schleife: constructUDPPackage (einzelne entität)
		return null;
	}
	public byte[] constructEntityByteStream(Entity entity){

		ArrayList<Entity> list = World.getInstance().getBattleField().getPlayers();
		Entity player = list.get(1);
		// Entity ->
		// (int)	id		4 byte
		byte[] byteID = intToByteArray(player.getId());
		// (byte)	type	1 byte
		//byte[] byteType = intToByteArray(player.getModel().get); ??
		// (float)	positionX	4 byte
		byte[] bytePosX = floatToByteArray(player.getPosition().x);
		// (float)	positionY	4 byte
		byte[] bytePosY = floatToByteArray(player.getPosition().y);
		// (float)	direction	4 byte
		byte[] byteDirX = floatToByteArray(player.getDirection().x);
		// (float)	direction	4 byte
		byte[] byteDirY = floatToByteArray(player.getDirection().y);
		// (float)	velocityX	4 byte
		byte[] byteVelX = floatToByteArray(player.getModel().getAcceleration()); // eig sollte velocity auch ein vektor sein
		// (float)	velocityY	4 byte
		//         <<<<	fehlt >>>>
		
		// Zusammenschweißen
		byte[] sum = new byte[24];
		sum[23] = byteID[4];
		return null;
	}
	
	public static void main(String[] args){

		// (int)	id		4 byte
		byte[] byteID = intToByteArray(343);
		// (byte)	type	1 byte
		//byte[] byteType = intToByteArray(player.getModel().get); ??
		// (float)	positionX	4 byte
		byte[] bytePosX = floatToByteArray(1.41f);
		// (float)	positionY	4 byte
		byte[] bytePosY = floatToByteArray(2.564f);
		// (float)	direction	4 byte
		byte[] byteDirX = floatToByteArray(0.240f);
		// (float)	direction	4 byte
		byte[] byteDirY = floatToByteArray(0.444f);
		// (float)	velocityX	4 byte
		byte[] byteVelX = floatToByteArray(1242.23f); // eig sollte velocity auch ein vektor sein
		// (float)	velocityY	4 byte
		//         <<<<	fehlt >>>>
		
		// Zusammenschweißen
		byte[] sum = new byte[24];
		sum[23] = byteID[3];

		sum[0] = byteID[0];
		sum[1] = byteID[1];
		sum[2] = byteID[2];
		sum[3] = byteID[3];
		System.out.println(sum[0]);
		System.out.println(fromByteArray(byteID));
		System.out.println(fromByteArray(sum));
	}
	public static int fromByteArray(byte[] bytes) {
	     return ByteBuffer.wrap(bytes).getInt();
	}
	
	
	public static byte [] longToByteArray (long value)
	{
	    return ByteBuffer.allocate(8).putLong(value).array();
	}
	public static byte [] floatToByteArray (float value)
	{  
	     return ByteBuffer.allocate(4).putFloat(value).array();
	}
	public static byte [] intToByteArray (int value)
	{  
	     return ByteBuffer.allocate(4).putInt(value).array();
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
	public void sendEmptyDataToSelf(){ //in order to close socket
		byte[] data = "exit".getBytes();
		packet.setData(data,0,data.length); //data;offset;länge des Packets
		try {
			packet.setAddress(InetAddress.getLocalHost());
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} // 188.58.54.66 example
		packet.setPort(portNumber-1);// 11333 = Port
		System.out.println(logName+" Trying to Send the Data to a Client...");
		try {
			this.socket.send(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void close(){
		this.socket.close();
		System.out.println(logName+"Closed!");
	}
	
}
