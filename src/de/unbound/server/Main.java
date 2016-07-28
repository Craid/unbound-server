package de.unbound.server;

import javax.swing.SwingUtilities;

import de.unbound.server.view.FrameMain;

public class Main {
public static void main(String[] args){
	SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
            new FrameMain();
        }
    });
}
}
