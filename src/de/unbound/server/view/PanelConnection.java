package de.unbound.server.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import de.unbound.server.network.ClientConnection;

public class PanelConnection extends JPanel{
public static JTable table;
	
	public PanelConnection(){
		this.setPreferredSize(new Dimension(420,100));
		this.setBackground(Color.black);
		this.setLayout(new BorderLayout());
		initializeTable();
	}

	
	public void initializeTable(){
		String[] columnNames = {"Status",
                "IPPort",
                "PlayerID",
                "Name",
                "Latency",
                "Packages Sent",
                "Packages Received"};
//Its data is initialized and stored in a two-dimensional Object array:

Object[][] data = {
{"not active", "xxx.xxx.xxx.xxx:yyyyy",
"1","Generic Player", new Integer(0), new Long(0), new Long(0)}
};
//Then the Table is constructed using these data and columnNames:


this.table = new JTable(data, columnNames);
JScrollPane scrollPane = new JScrollPane(table);
table.setFillsViewportHeight(false);
//this.add(table.getTableHeader());
this.add(scrollPane);
	}
	
	public static void insertNewValueToTable(ClientConnection client){
		Object[] data = new Object[7];
		data[0] = client.getStatusTCP();
		data[1] = "lol";
		//data[1] = client.getClientIP().getHostAddress()+":"+client.getClientPortTCP();
		data[2] = client.getPlayerID();
		data[3] = client.getPlayerName();
		data[4] = client.getLatency();
		data[5] = client.getPackagesPerSecondSentTo();
		data[6] = client.getPackagesPerSecondReceived();
	}
	
}
