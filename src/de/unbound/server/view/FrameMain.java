package de.unbound.server.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglCanvas;

import de.unbound.ServerGame;
import de.unbound.TestGame;
import de.unbound.UnboundGame;

public class FrameMain extends JFrame{

	JPanel gamePanel, northPanel, southPanel;
	JPanel connectionPanel, buttonPanel, logPanel;
	ApplicationListener game;
	LwjglCanvas canvas;
	
	public FrameMain(){
		initializeJFrame();
		attachGamePanel();
		attachPanels();
		this.setVisible(true);
		PanelLog.log("Main Frame complete");
	}
	
	public void initializeJFrame(){
		this.setSize(600, 800);
		this.setLayout(new BorderLayout());
		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		WindowListener exitListener = new WindowAdapter() {

		    @Override
		    public void windowClosing(WindowEvent e) {
		        int confirm = JOptionPane.showOptionDialog(
		             null, "Are You Sure to Close Application?", 
		             "Exit Confirmation", JOptionPane.YES_NO_OPTION, 
		             JOptionPane.QUESTION_MESSAGE, null, null, null);
		        if (confirm == 0) {
		        	try{
		        	game.dispose();
		        	canvas.getApplicationListener().dispose();
		        	canvas.exit();
		        	Gdx.app.exit();
		        	Thread.sleep(1000);
		        	System.exit(0);
		        	//ich checks net... es schlieﬂt net alles
		        	}catch(Exception x){}
		        }
		        else
		        	;
		    }
		};
		this.addWindowListener(exitListener);
		
		this.setResizable(false);

	}
	public void attachGamePanel(){
		gamePanel = new JPanel();
		game = new ServerGame();
		canvas = new LwjglCanvas(game);
		this.add(canvas.getCanvas(),"Center");
	}
	public void attachPanels(){
		northPanel = new JPanel();
		southPanel = new JPanel();
		
		northPanel.setLayout(new BorderLayout());
		connectionPanel = new PanelConnection();
		buttonPanel = new PanelButton();
		northPanel.add(connectionPanel,"West");
		northPanel.add(buttonPanel,"East");
		
		this.add(northPanel,"North");
		
		southPanel.setLayout(new BorderLayout());
		logPanel = new PanelLog();
		southPanel.add(logPanel);
		
		this.add(southPanel,"South");
		PanelLog.log("successfully attached all Panels");
	}
}
