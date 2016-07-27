package de.unbound.server.view;

import java.awt.Color;
import java.awt.Dimension;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JPanel;
import javax.swing.JTextArea;

public class LogPanel extends JPanel{

	static JTextArea area;
	static Date date;
	static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	
	
	public LogPanel(){
		this.setPreferredSize(new Dimension(600,150));
		date = new Date();
		area = new JTextArea();
		log("initialized LogPanel");
		area.setPreferredSize(new Dimension(580,150));
		area.setSelectionColor(Color.BLACK);
		this.add(area);
		
		
		
	}
	public static void log(String text){
		
	    if (area.getText().length() >= 3000 ) area.setText(area.getText().substring(0, 1000));
		
		try{
		date.setTime(System.currentTimeMillis());
		String formattedDate = sdf.format(date);
		String newLog = formattedDate+" | " +text;
		area.setText(newLog+"\n"+area.getText());
		}catch (Exception e){
			
		}
	}
}
