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
	private static AlternativePanelLog instance;

	private AlternativePanelLog() {
		this.setPreferredSize(new Dimension(600, 150));
		date = new Date();
		area = new JTextArea();
		log("initialized LogPanel");
		area.setPreferredSize(new Dimension(580, 150));
		area.setSelectionColor(Color.BLACK);
		this.add(area);

		try {
			PipedOutputStream pOut = new PipedOutputStream();
			System.setOut(new PrintStream(pOut));
			PipedInputStream pIn = new PipedInputStream(pOut);
			final BufferedReader reader = new BufferedReader(
					new InputStreamReader(pIn));

			Thread d = new Thread() {
				public void run() {
					while (true) {
						try {
							String line = reader.readLine();
							if (line != null) {
								log(line);
								// Write line to component
							}
						} catch (IOException ex) {
							// Handle ex
						}
					}

				}
			};
			d.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void log(String text) {

		if (area.getText().length() >= 3000)
			area.setText(area.getText().substring(0, 1000));

		try {
			date.setTime(System.currentTimeMillis());
			String formattedDate = sdf.format(date);
			String newLog = formattedDate + " | " + text;
			area.setText(newLog + "\n" + area.getText());
		} catch (Exception e) {

		}
	}

	public static AlternativePanelLog getInstance() {
		if (instance == null)
			instance = new AlternativePanelLog();
		return instance;
	}
}