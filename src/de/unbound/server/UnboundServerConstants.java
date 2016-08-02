package de.unbound.server;

public class UnboundServerConstants {
	
	public static Status SERVER_STATUS = Status.INITIAL;
	
	public static int PORT = 11300;
	
	public enum Status{
		INITIAL,RUNNING,GAMEOVER,PAUSED,STOPPED;
	} 

}
