/*
 * Copyright (C) 2011 The Wireless WiFi MIDI Controller Open Source Project
 * 
 * Licensed under the GNU General Public License, Version 3
 */
package cz.pblazek.wwmc.test;

import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author rtep.kezalb@gmail.com
 * 
 */
public class Launcher {

	private PianoRollFrame frame;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new Launcher().frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Launcher() {
		this.frame = new PianoRollFrame();
		this.frame.addWindowListener(new WindowAdapter() {

			private SendWorker sendWorker;

			@Override
			public void windowOpened(WindowEvent event) {
				super.windowOpened(event);
				this.sendWorker = new SendWorker(Launcher.this.frame);
				this.sendWorker.open();
			}

			@Override
			public void windowClosing(WindowEvent event) {
				this.sendWorker.close();
				this.sendWorker = null;
				super.windowClosing(event);
			}

		});

	}

}
