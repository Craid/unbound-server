package de.unbound.server.view;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;

import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.SpringLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;

import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JLabel;

import de.unbound.server.network.ConnectionHandler;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PanelControls extends JPanel {
	private static JTextField txtPort;
	boolean toggleServer = true;
	final static JButton btnStopServer = new JButton("Stop Server");
	final static JButton btnStartServer = new JButton("Start Server");
	/**
	 * Create the panel.
	 */
	

	public PanelControls() {
		
		
		btnStartServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ConnectionHandler.getInstance().setPortNumber(PanelControls.getTxtPort());
				ConnectionHandler.getInstance().startServer();
				PanelControls.toggleButtons();
			}
		});
		btnStartServer.setSize(new Dimension(150, 0));
		
		
		btnStopServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ConnectionHandler.getInstance().stopServer();
				PanelControls.toggleButtons();
			
			}
		});
		btnStopServer.setMaximumSize(new Dimension(91, 23));
		btnStopServer.setSize(new Dimension(150, 0));
		btnStopServer.setEnabled(!toggleServer);
		txtPort = new JTextField();
		txtPort.setText("11300");
		txtPort.setHorizontalAlignment(SwingConstants.LEFT);
		txtPort.setColumns(10);
		
		JLabel lblPort = new JLabel("Port");
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
						.addComponent(btnStopServer, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btnStartServer, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblPort)
							.addGap(5)
							.addComponent(txtPort, 0, 0, Short.MAX_VALUE)))
					.addContainerGap(43, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnStartServer)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnStopServer, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(txtPort, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblPort))
					.addContainerGap(29, Short.MAX_VALUE))
		);
		setLayout(groupLayout);

	}

	public static Integer getTxtPort() {
		int port = Integer.parseInt(txtPort.getText());
		return port;
	}

	public static void setTxtPort(JTextField txtPort) {
		txtPort = txtPort;
	}
	
	public static void toggleButtons(){
		btnStartServer.setEnabled(!btnStartServer.isEnabled());
		btnStopServer.setEnabled(!btnStopServer.isEnabled());
	}
	
}
