package de.unbound.server.view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglCanvas;

import de.unbound.ServerGame;
import de.unbound.TestGame;
import de.unbound.UnboundGame;

public class MainFrame extends JFrame{

	JPanel gamePanel, logPanel, buttonPanel, serverStatusPanel;
	
	public MainFrame(){
		initializeJFrame();
		attachGamePanel();
		attachPanels();
		this.setVisible(true);
	}
	
	public void initializeJFrame(){
		this.setSize(600, 800);
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(3);

	}
	public void attachGamePanel(){
		gamePanel = new JPanel();
		LwjglCanvas canvas = new LwjglCanvas(new TestGame());
		this.add(canvas.getCanvas(),"Center");
	}
	public void attachPanels(){
		logPanel = new JPanel();
		logPanel.setPreferredSize(new Dimension(600,100));
		serverStatusPanel = new JPanel();
		serverStatusPanel.setPreferredSize(new Dimension(600,100));
		this.add(logPanel,"South");
		this.add(serverStatusPanel,"North");
	}
}
