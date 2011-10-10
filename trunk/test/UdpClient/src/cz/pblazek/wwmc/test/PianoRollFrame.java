/*
 * Copyright (C) 2011 The Wireless WiFi MIDI Controller Open Source Project
 * 
 * Licensed under the GNU General Public License, Version 3
 */
package cz.pblazek.wwmc.test;

import java.awt.Color;
import java.awt.Container;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;

import cz.pblazek.wwmc.test.key.Key;
import cz.pblazek.wwmc.test.key.PianoKeyEnum;

/**
 * @author rtep.kezalb@gmail.com
 * 
 */
public class PianoRollFrame extends JFrame {

	private static final long serialVersionUID = -3820997856110340757L;

	private static final String WINDOW_TITLE = "Test UDP Client 1.0 (Wireless WiFi MIDI Controller)";

	private static final Image WINDOW_ICON = Toolkit.getDefaultToolkit().getImage("res/app_wwmc.png");

	private static final int WINDOW_WIDTH = 655;

	private static final int WINDOW_HEIGHT = 302;

	public PianoRollFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle(PianoRollFrame.WINDOW_TITLE);
		setIconImage(PianoRollFrame.WINDOW_ICON);
		setResizable(false);
		Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
		setBounds(center.x - PianoRollFrame.WINDOW_WIDTH / 2, center.y - PianoRollFrame.WINDOW_HEIGHT / 2, PianoRollFrame.WINDOW_WIDTH,
				PianoRollFrame.WINDOW_HEIGHT);
		Container container = getContentPane();
		container.setBackground(Color.BLACK);
		container.setLayout(null);
		createKeys();
	}

	//

	public void sendToPiano(String tone) {
		PianoKeyEnum pianoKeyEnum = PianoKeyEnum.findByTone(tone);
		if (pianoKeyEnum != null) {
			AsyncWorker asyncWorker = new AsyncWorker(pianoKeyEnum);
			asyncWorker.start();
		}
	}

	private void createKeys() {
		getContentPane().add(PianoKeyEnum.PK_C00.getKey()[0]);
		getContentPane().add(PianoKeyEnum.PK_C00.getKey()[1]);
		getContentPane().add(PianoKeyEnum.PK_CIS00.getKey()[0]);
		getContentPane().add(PianoKeyEnum.PK_D00.getKey()[0]);
		getContentPane().add(PianoKeyEnum.PK_D00.getKey()[1]);
		getContentPane().add(PianoKeyEnum.PK_DIS00.getKey()[0]);
		getContentPane().add(PianoKeyEnum.PK_E00.getKey()[0]);
		getContentPane().add(PianoKeyEnum.PK_E00.getKey()[1]);
		getContentPane().add(PianoKeyEnum.PK_F00.getKey()[0]);
		getContentPane().add(PianoKeyEnum.PK_F00.getKey()[1]);
		getContentPane().add(PianoKeyEnum.PK_FIS00.getKey()[0]);
		getContentPane().add(PianoKeyEnum.PK_G00.getKey()[0]);
		getContentPane().add(PianoKeyEnum.PK_G00.getKey()[1]);
		getContentPane().add(PianoKeyEnum.PK_GIS00.getKey()[0]);
		getContentPane().add(PianoKeyEnum.PK_A00.getKey()[0]);
		getContentPane().add(PianoKeyEnum.PK_A00.getKey()[1]);
		getContentPane().add(PianoKeyEnum.PK_AIS00.getKey()[0]);
		getContentPane().add(PianoKeyEnum.PK_H00.getKey()[0]);
		getContentPane().add(PianoKeyEnum.PK_H00.getKey()[1]);
		getContentPane().add(PianoKeyEnum.PK_C01.getKey()[0]);
	}

	private class AsyncWorker extends Thread {

		private PianoKeyEnum pianoKeyEnum;

		public AsyncWorker(PianoKeyEnum pianoKeyEnum) {
			super();
			this.pianoKeyEnum = pianoKeyEnum;
		}

		@Override
		public void run() {
			super.run();
			try {
				for (Key key : this.pianoKeyEnum.getKey()) {
					key.setBackground(Key.KEY_COLOR_BACK);
				}
				TimeUnit.MILLISECONDS.sleep(200);
				for (Key key : this.pianoKeyEnum.getKey()) {
					key.setBackground(key.getColor());
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
