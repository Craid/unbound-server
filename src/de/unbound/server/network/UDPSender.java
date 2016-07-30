package de.unbound.server.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

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
