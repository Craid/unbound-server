package de.unbound.server.network;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

import de.unbound.game.World;
import de.unbound.game.model.entities.Entity;
import de.unbound.game.network.serialization.ByteBuilderHelper;
import de.unbound.game.network.serialization.PacketSerializer;

public class TCPSender{
	
	private final String logName = "[TCP Sender] "; //für logs
	private ConnectionHandler connectionHandler;
	private PacketSerializer serializer = new PacketSerializer();
	
	public TCPSender(ConnectionHandler connectionHandler) {
			this.connectionHandler = connectionHandler;
			this.serializer = new PacketSerializer();
	}
	
	public void tellOne(String msg, Socket skt){
		PrintWriter pw = connectionHandler.outputSockets.get(skt);
		pw.write(msg+"\n");
		pw.flush();
		
	}
	public void tellOne(byte[] msg, Socket skt){
		PrintWriter pw = connectionHandler.outputSockets.get(skt);
		String send = new String(msg)+"\n";
		pw.write(send);
		pw.flush();
		
	}
	public void sendPlayerAndMainBase(Socket skt){
		try{
			System.out.println(logName+"Trying to send player and Main Base");
			Entity player = World.getInstance().getWaveHandler().getOwnFactory().createPlayer();
			Entity mainBase = World.getInstance().getBattleField().getMainBase();
			ClientConnection client = connectionHandler.getClientConnection(skt.getInetAddress(), skt.getPort());
			client.playerID = player.getId();
			byte[] playerStream = serializer.constructEntityByteStream(player);
			byte[] baseStream = serializer.constructEntityByteStream(mainBase);
	 		
			PrintWriter pw = connectionHandler.outputSockets.get(skt);
			String send;
			System.out.println(logName+"This is the player Bytestream: "+new String(playerStream));
			
			
			
			
				byte[] sum = playerStream;
				ByteBuilderHelper bh = new ByteBuilderHelper();
				System.out.println(bh.intFromByteArray(sum,0)+" = Entity ID: "+ player.getId());
				System.out.println(sum[4]+" = Entity Class: "+player.getModel().getId());
				System.out.println(bh.floatFromByteArray(sum,5)+" = Position X: "+ player.getPosition().x);
				System.out.println(bh.floatFromByteArray(sum,9)+" = Position Y: "+ player.getPosition().y);
				System.out.println(bh.floatFromByteArray(sum,13)+" = Direction X: "+ player.getDirection().x);
				System.out.println(bh.floatFromByteArray(sum,17)+" = Direction Y: "+ player.getDirection().y);
				System.out.println(bh.floatFromByteArray(sum,21)+" = Velocity X: "+ player.getUpdateState().getMove().getVelocity().x);
				System.out.println(bh.floatFromByteArray(sum,25)+" = Velocity Y: "+ player.getUpdateState().getMove().getVelocity().y);
			
			
				sum = baseStream;
				System.out.println(bh.intFromByteArray(sum,0)+" = Entity ID");
				System.out.println(sum[4]+" = Entity Class");
				System.out.println(bh.floatFromByteArray(sum,5)+" = Position X");
				System.out.println(bh.floatFromByteArray(sum,9)+" = Position Y");
				System.out.println(bh.floatFromByteArray(sum,13)+" = Direction X");
				System.out.println(bh.floatFromByteArray(sum,17)+" = Direction Y");
				System.out.println(bh.floatFromByteArray(sum,21)+" = Velocity X");
				System.out.println(bh.floatFromByteArray(sum,25)+" = Velocity Y");
			
			
			
			
			
			
			
			
			
			
			
			
			send = "Player:"+new String(playerStream)+"\n";
			pw.write(send);
			pw.flush();
			client.tcpPackagesSentTo++;
			System.out.println(logName+"This is the main base Bytestream: "+new String(baseStream));
			send = "MainBase:"+new String(baseStream)+"\n";
			pw.write(send);
			pw.flush();
			client.tcpPackagesSentTo++;
			//PanelConnection.updateRows(connectionHandler);
			System.out.println(logName+"Successfully sent Player and Main Base");
		} 
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println(logName+"Trying to send player and Main Base");
		}
	}
	
	
	
	public void tellEveryone(String msg) {
		//Lade Hashmap...
		HashMap<Socket, PrintWriter> outputSockets = connectionHandler.outputSockets;
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
}
