package de.unbound.server.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglCanvas;

import de.unbound.ServerGame;
import de.unbound.TestGame;
import de.unbound.UnboundGame;

public class MainFrame extends JFrame{

	JPanel gamePanel, northPanel, southPanel;
	JPanel connectionPanel, buttonPanel, logPanel;
	
	public MainFrame(){
		initializeJFrame();
		attachGamePanel();
		attachPanels();
		this.setVisible(true);
		LogPanel.log("Main Frame complete");
	}
	
	public void initializeJFrame(){
		this.setSize(600, 800);
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(3);
		this.setResizable(false);

	}
	public void attachGamePanel(){
		gamePanel = new JPanel();
		LwjglCanvas canvas = new LwjglCanvas(new ServerGame());
		this.add(canvas.getCanvas(),"Center");
	}
	public void attachPanels(){
		northPanel = new JPanel();
		southPanel = new JPanel();
		
		northPanel.setLayout(new BorderLayout());
		connectionPanel = new ConnectionPanel();
		buttonPanel = new ButtonPanel();
		northPanel.add(connectionPanel,"West");
		northPanel.add(buttonPanel,"East");
		
		this.add(northPanel,"North");
		
		southPanel.setLayout(new BorderLayout());
		logPanel = new LogPanel();
		southPanel.add(logPanel);
		
		this.add(southPanel,"South");
		LogPanel.log("successfully attached all Panels");
	}
}
