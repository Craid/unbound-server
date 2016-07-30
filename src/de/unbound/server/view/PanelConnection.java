package de.unbound.server.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import de.unbound.server.network.ClientConnection;
import de.unbound.server.network.TCPConnectionHandler;

public class PanelConnection extends JPanel{
public static JTable table;
	
	public PanelConnection(){
		this.setPreferredSize(new Dimension(570,100));
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


//this.table = new JTable(data, columnNames);
//this.table.setModel(new DefaultTableModel());





DefaultTableModel model = new DefaultTableModel();
model.addColumn("Status");
model.addColumn("IPPort");
model.addColumn("PlayerID");
model.addColumn("Name");
model.addColumn("Latency");
model.addColumn("Packages Sent");
model.addColumn("Packages Received");
table = new JTable(model);

table.getColumnModel().getColumn(1).setPreferredWidth(120);


JScrollPane scrollPane = new JScrollPane(table);
table.setFillsViewportHeight(false);
//this.add(table.getTableHeader());
this.add(scrollPane);
	}
	
	public static void insertNewValueToTable(ClientConnection client){
		Object[] data = new Object[7];
		data[0] = client.getStatusTCP();
		data[1] = client.getClientIP().getHostAddress()+":"+String.valueOf(client.getClientPortTCP());
		data[2] = client.getPlayerID();
		data[3] = client.getPlayerName();
		data[4] = client.getLatency();
		data[5] = client.getPackagesPerSecondSentTo();
		data[6] = client.getPackagesPerSecondReceived();
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.addRow(data);
	}
	public static void removeConnectionFromTable(ClientConnection client){
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		 System.out.println(model.getValueAt(0, 2));
		for( int i = model.getRowCount() - 1; i >= 0; i-- )
		{
			int playerId = Integer.parseInt(String.valueOf(model.getValueAt(i, 2)));
			if (playerId == client.getPlayerID()){
		    model.removeRow(i);
		   
			}
		}
	}
	public static void updateRows(){
		DefaultTableModel dm = (DefaultTableModel) table.getModel();
		int rowCount = dm.getRowCount();
		//Remove rows one by one from the end of the table
		for (int i = rowCount - 1; i >= 0; i--) {
		    dm.removeRow(i);
		}
		for (ClientConnection client : TCPConnectionHandler.getInstance().clients){
			Object[] data = new Object[7];
			data[0] = client.getStatusTCP();
			data[1] = client.getClientIP().getHostAddress()+":"+String.valueOf(client.getClientPortTCP());
			data[2] = client.getPlayerID();
			data[3] = client.getPlayerName();
			data[4] = client.getLatency();
			data[5] = client.getPackagesPerSecondSentTo();
			data[6] = client.getPackagesPerSecondReceived();
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			model.addRow(data);
		}
	}
	
	public boolean existsInTable(JTable table, Object[] entry) {

		//http://stackoverflow.com/questions/15639611/
		//how-to-check-if-a-value-exists-in-jtable-which-i-am-trying-to-add
	    // Get row and column count
	    int rowCount = table.getRowCount();
	    int colCount = table.getColumnCount();

	    // Get Current Table Entry
	    String curEntry = "";
	    for (Object o : entry) {
	        String e = o.toString();
	        curEntry = curEntry + " " + e;
	    }

	    // Check against all entries
	    for (int i = 0; i < rowCount; i++) {
	        String rowEntry = "";
	        for (int j = 0; j < colCount; j++)
	            rowEntry = rowEntry + " " + table.getValueAt(i, j).toString();
	        if (rowEntry.equalsIgnoreCase(curEntry)) {
	            return true;
	        }
	    }
	    return false;
	}
	
}
