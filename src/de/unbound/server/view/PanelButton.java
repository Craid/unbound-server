package de.unbound.server.view;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

public class PanelButton extends JPanel{
	
	private static final long serialVersionUID = 1L;

	public PanelButton(){
		this.setPreferredSize(new Dimension(150,100));
		this.setBackground(new Color(0,240,0,200));
	}
}
