package de.unbound.server.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglCanvas;

import de.unbound.ServerGame;
import de.unbound.server.UnboundServerConstants;
import de.unbound.server.UnboundServerConstants.Status;

public class FrameMain extends JFrame {

	boolean toggleServer = true;

	private static final long serialVersionUID = 1L;

	JPanel gamePanel;
	ApplicationListener game;
	LwjglCanvas canvas;

	private JButton btnStopServer, btnStartServer;
	private JTextField txtPort;

	public FrameMain() {
		initializeJFrame();
		attachPanels();
		this.setVisible(true);
	}

	public void initializeJFrame() {
		this.setSize(900, 800);
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final JFrame thisFrame = this;
		WindowListener exitListener = new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				thisFrame.setVisible(false);
				thisFrame.dispose();

				System.exit(1);
			}
		};
		this.addWindowListener(exitListener);

		this.setResizable(false);

	}

	public void attachGamePanel() {
		if (gamePanel == null) {
			gamePanel = new JPanel();
			LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
			config.forceExit = true;
			game = new ServerGame();
			LwjglCanvas canvas = new LwjglCanvas(game, config);
			canvas.getCanvas().setPreferredSize(new Dimension(252, 650));
			gamePanel.add(canvas.getCanvas());
			this.add(gamePanel, "East");
		}
	}

	public void attachPanels() {
		JPanel northPanel = new JPanel();
		JPanel centerPanel = new JPanel();

		northPanel.setLayout(new BorderLayout());
		JPanel connectionPanel = new PanelConnection();
		JPanel buttonPanel = createButtonPanel();
		northPanel.add(connectionPanel, "West");
		northPanel.add(buttonPanel, "East");

		this.add(northPanel, "North");

		centerPanel.setLayout(new BorderLayout());
		AlternativePanelLog logPanel = new AlternativePanelLog();
		centerPanel.add(logPanel);

		this.add(centerPanel, "Center");
		// System.out.println("successfully attached all Panels");
	}

	private JPanel createButtonPanel() {
		JPanel panel = new JPanel();
		btnStartServer = new JButton("Start Server");
		btnStartServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					UnboundServerConstants.PORT = getTxtPort();
					attachGamePanel();
					toggleButtons();
					UnboundServerConstants.SERVER_STATUS = Status.RUNNING;
				} catch (Exception exc) {
				}

			}
		});

		btnStopServer = new JButton("Stop server");
		btnStopServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				toggleButtons();
				UnboundServerConstants.SERVER_STATUS = Status.STOPPED;
			}
		});
		btnStopServer.setEnabled(!toggleServer);

		txtPort = new JTextField();
		txtPort.setText("11300");
		txtPort.setHorizontalAlignment(SwingConstants.LEFT);
		txtPort.setColumns(10);

		JLabel lblPort = new JLabel("Port");

		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JPanel port = new JPanel();
		port.setLayout(new FlowLayout());
		port.add(lblPort);
		port.add(txtPort);

		panel.add(btnStartServer);
		panel.add(btnStopServer);
		panel.add(port);

		return panel;
	}

	public Integer getTxtPort() {
		int port = Integer.parseInt(txtPort.getText());
		return port;
	}

	public void toggleButtons() {
		btnStartServer.setEnabled(!btnStartServer.isEnabled());
		btnStopServer.setEnabled(!btnStopServer.isEnabled());

	}
}
