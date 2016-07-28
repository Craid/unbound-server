package de.unbound.server;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import de.unbound.ServerGame;

public class ServerStarter {
	
	public static void main(String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.useGL30 = true;
		config.useHDPI = true;
		config.width = 550;
		config.height = 300;
		config.title = "Unbound";
		config.resizable = false;
		config.forceExit = true;
		
		new LwjglApplication(new ServerGame(), config);
	}

}
