package de.unbound.server.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class PanelConnection extends JPanel{

	public PanelConnection(){
		this.setPreferredSize(new Dimension(450,100));
		this.setBackground(Color.black);
		this.setLayout(new BorderLayout());
		initializeTable();
	}

	
	public void initializeTable(){
		String[] columnNames = {"Status",
                "IPPort",
                "Name",
                "Latency",
                "Packages Sent",
                "Packages Received"};
//Its data is initialized and stored in a two-dimensional Object array:

Object[][] data = {
{"not active", "xxx.xxx.xxx.xxx:yyyyy",
"Generic Player", new Integer(0), new Long(0), new Long(0)}
,
{"not active", "xxx.xxx.xxx.xxx:yyyyy",
"Generic Player", new Integer(0), new Long(0), new Long(0)}
,
{"not active", "xxx.xxx.xxx.xxx:yyyyy",
"Generic Player", new Integer(0), new Long(0), new Long(0)}
,
{"not active", "xxx.xxx.xxx.xxx:yyyyy",
"Generic Player", new Integer(0), new Long(0), new Long(0)}
,
{"not active", "xxx.xxx.xxx.xxx:yyyyy",
"Generic Player", new Integer(0), new Long(0), new Long(0)}
,
{"not active", "xxx.xxx.xxx.xxx:yyyyy",
"Generic Player", new Integer(0), new Long(0), new Long(0)}
,
{"not active", "xxx.xxx.xxx.xxx:yyyyy",
"Generic Player", new Integer(0), new Long(0), new Long(0)}
,
{"not active", "xxx.xxx.xxx.xxx:yyyyy",
"Generic Player", new Integer(0), new Long(0), new Long(0)}
};
//Then the Table is constructed using these data and columnNames:


JTable table = new JTable(data, columnNames);
JScrollPane scrollPane = new JScrollPane(table);
table.setFillsViewportHeight(false);
//this.add(table.getTableHeader());
this.add(scrollPane);
	}
	
}
