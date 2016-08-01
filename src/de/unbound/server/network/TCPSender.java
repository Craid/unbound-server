package de.unbound.server.network;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

import de.unbound.game.World;
import de.unbound.game.model.entities.Entity;
import de.unbound.game.network.serialization.PacketSerializer;
import de.unbound.server.view.PanelConnection;

public class TCPSender{
	private ConnectionHandler connectionHandler;
	private final String logName = "[TCP Sender] "; //für logs
	
	public TCPSender(ConnectionHandler connectionHandler) {
			this.connectionHandler = connectionHandler;
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
		//creates new Player and sends it to Client!
		//also sends the main base to client
		PrintWriter outMsg = ConnectionHandler.getInstance().outputSockets.get(skt);
		try{
		//ArrayList<Entity> playerAndMainBase = new ArrayList<Entity>();
		//Entity player = World.getInstance().getBattleField().getPlayers().get(0); //TODO Marwin: Vorsicht! 1. Player? Ändern!
		Entity player = World.getInstance().getWaveHandler().getOwnFactory().createPlayer();
		Entity mainBase = World.getInstance().getBattleField().getMainBase();
		//playerAndMainBase.add(player);
		//playerAndMainBase.add(mainBase);
//		player.setPosition(new Vector2(new Vector2(UnboundConstants.WORLDWIDTH*World.getInstance().getBattleField().getScaleX()/ 2, UnboundConstants.SINGLEGRIDHEIGHT*2)));
		ClientConnection client = ConnectionHandler.getInstance().getClientConnection(skt.getInetAddress(), skt.getPort());
		client.playerID = player.getId();
		PanelConnection.updateRows();
		PacketSerializer serializer = new PacketSerializer();
		byte[] playerStream = serializer.constructEntityByteStream(player);
		byte[] baseStream = serializer.constructEntityByteStream(mainBase);
 		
		PrintWriter pw = connectionHandler.outputSockets.get(skt);
		String send;
		send = "Player:"+new String(playerStream)+"\n";
		System.out.println("Player length: " + send.getBytes().length);
		pw.write(send);
		pw.flush();
		client.tcpPackagesSentTo++;
		send = "MainBase:"+new String(baseStream)+"\n";
		pw.write(send);
		pw.flush();
		client.tcpPackagesSentTo++;
		PanelConnection.updateRows();
		} catch(Exception e){}
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
