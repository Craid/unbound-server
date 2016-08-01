package de.unbound.server.view;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglCanvas;

import de.unbound.ServerGame;
import de.unbound.server.network.ConnectionHandler;

public class FrameMain extends JFrame{

	JPanel gamePanel, northPanel, southPanel;
	JPanel connectionPanel, buttonPanel;
	AlternativePanelLog logPanel;
	ApplicationListener game;
	LwjglCanvas canvas;
	ConnectionHandler connectionHandler;
	
	public FrameMain(){
		initializeJFrame();
		attachGamePanel();
		attachPanels();
		this.setVisible(true);
		//System.out.println("Main Frame complete");
		initializeTCP();
	}
	
	public void initializeTCP(){
			connectionHandler = ConnectionHandler.getInstance();
	}
	
	public void initializeJFrame(){
		this.setSize(750, 800);
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final JFrame thisFrame = this;
		WindowListener exitListener = new WindowAdapter() {

		    @Override
		    public void windowClosing(WindowEvent e) {
		       int confirm = JOptionPane.showOptionDialog(
			             null, "Möchten Sie wirklich den Server beenden?", 
			             "Bestägigung", JOptionPane.YES_NO_OPTION, 
			             JOptionPane.QUESTION_MESSAGE, null, null, null);
		        if (confirm == JOptionPane.YES_OPTION) {
		        	try{
			        	thisFrame.setVisible(false);
			        	thisFrame.dispose();

			        	System.exit(1);
		        	}catch(Exception x){
		        		x.printStackTrace();
		        	}
		        }
		    }
		};
		this.addWindowListener(exitListener);
		
		this.setResizable(false);

	}
	public void attachGamePanel(){
		gamePanel = new JPanel();
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.forceExit = true;
		game = new ServerGame();
		canvas = new LwjglCanvas(game,config);
		this.add(canvas.getCanvas(),"Center");
	}
	public void attachPanels(){
		northPanel = new JPanel();
		southPanel = new JPanel();
		
		northPanel.setLayout(new BorderLayout());
		connectionPanel = new PanelConnection();
		buttonPanel = new PanelControls();
		northPanel.add(connectionPanel,"West");
		northPanel.add(buttonPanel,"East");
		
		this.add(northPanel,"North");
		
		southPanel.setLayout(new BorderLayout());
		logPanel = AlternativePanelLog.getInstance();
		southPanel.add(logPanel);
		
		this.add(southPanel,"South");
		//System.out.println("successfully attached all Panels");
	}
}
