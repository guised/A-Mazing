package com.babeex.winmaze;

import java.awt.EventQueue;

import com.babeex.winmaze.gui.MainGui;

public class App {

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					MainGui gui = new MainGui();
					gui.initialise();
					gui.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
