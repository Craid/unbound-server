package de.unbound.server.view;

import java.awt.Color;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JPanel;
import javax.swing.JTextArea;

public class AlternativePanelLog extends JPanel {

	private static final long serialVersionUID = 1L;
	JTextArea area;
	Date date;
	static final SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss.SSS");
	private PipedInputStream pIn;

	public AlternativePanelLog() {
		this.setPreferredSize(new Dimension(650, 600));
		date = new Date();
		area = new JTextArea();
		log("initialized LogPanel");
		area.setPreferredSize(new Dimension(640, 600));
		area.setSelectionColor(Color.BLACK);
		this.add(area);

		try {
			PipedOutputStream pOut = new PipedOutputStream();
			System.setOut(new PrintStream(pOut));
			pIn = new PipedInputStream(pOut);
			

			Thread d = new Thread() {
				public void run() {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(pIn));
					while (true) {
						try {
							String line = reader.readLine();
							if (line != null) {
								log(line);
								// Write line to component
							}
						} catch (IOException ex) {
							try {
								reader.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			};
			d.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void log(String text) {

		if (area.getText().length() >= 25000)
			area.setText(area.getText().substring(0, 15000));

		try {
			date.setTime(System.currentTimeMillis());
			String formattedDate = sdf.format(date);
			String newLog = formattedDate + " | " + text;
			area.setText(newLog + "\n" + area.getText());
		} catch (Exception e) {

		}
	}

}