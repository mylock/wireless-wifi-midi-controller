/*
 * Wireless WiFi MIDI Controller
 * Copyright (C) 2011 Petr Blazek
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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

	public void sendToPianoRoll(byte[] data) {
		int midiNum = (data[0] << 24) + ((data[1] & 0xff) << 16) + ((data[2] & 0xff) << 8) + (data[3] & 0xff);
		PianoKeyEnum pianoKeyEnum = PianoKeyEnum.findByMidiNum(midiNum);
		if (pianoKeyEnum != null) {
			AsyncWorker asyncWorker = new AsyncWorker(pianoKeyEnum);
			asyncWorker.start();
		}
	}

	/*
	 * FUTURE
	 * 
	 * public void sendToPianoRoll(byte[] data) { int midiNum = (data[0] << 24) + ((data[1] & 0xff) << 16) + ((data[2] & 0xff) << 8) + (data[3] & 0xff);
	 * AsyncWorker asyncWorker = new AsyncWorker(midiNum); asyncWorker.start(); }
	 */

	private void createKeys() {
		getContentPane().add(PianoKeyEnum.PK_C04.getKeys()[0]);
		getContentPane().add(PianoKeyEnum.PK_C04.getKeys()[1]);
		getContentPane().add(PianoKeyEnum.PK_CIS04.getKeys()[0]);
		getContentPane().add(PianoKeyEnum.PK_D04.getKeys()[0]);
		getContentPane().add(PianoKeyEnum.PK_D04.getKeys()[1]);
		getContentPane().add(PianoKeyEnum.PK_DIS04.getKeys()[0]);
		getContentPane().add(PianoKeyEnum.PK_E04.getKeys()[0]);
		getContentPane().add(PianoKeyEnum.PK_E04.getKeys()[1]);
		getContentPane().add(PianoKeyEnum.PK_F04.getKeys()[0]);
		getContentPane().add(PianoKeyEnum.PK_F04.getKeys()[1]);
		getContentPane().add(PianoKeyEnum.PK_FIS04.getKeys()[0]);
		getContentPane().add(PianoKeyEnum.PK_G04.getKeys()[0]);
		getContentPane().add(PianoKeyEnum.PK_G04.getKeys()[1]);
		getContentPane().add(PianoKeyEnum.PK_GIS04.getKeys()[0]);
		getContentPane().add(PianoKeyEnum.PK_A04.getKeys()[0]);
		getContentPane().add(PianoKeyEnum.PK_A04.getKeys()[1]);
		getContentPane().add(PianoKeyEnum.PK_AIS04.getKeys()[0]);
		getContentPane().add(PianoKeyEnum.PK_H04.getKeys()[0]);
		getContentPane().add(PianoKeyEnum.PK_H04.getKeys()[1]);
		getContentPane().add(PianoKeyEnum.PK_C05.getKeys()[0]);
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
				for (Key key : this.pianoKeyEnum.getKeys()) {
					key.setBackground(Key.KEY_COLOR_BACK);
				}
				TimeUnit.MILLISECONDS.sleep(200);
				for (Key key : this.pianoKeyEnum.getKeys()) {
					key.setBackground(key.getColor());
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	/*
	 * FUTURE
	 * 
	 * private class AsyncWorker extends Thread {
	 * 
	 * private static final int DATA_TYPE_DIFFERENCE = 500;
	 * 
	 * private int midiNum;
	 * 
	 * public AsyncWorker(int midiNum) { super(); this.midiNum = midiNum; }
	 * 
	 * @Override public void run() { super.run(); if ((this.midiNum - AsyncWorker.DATA_TYPE_DIFFERENCE) < 0) { System.out.println(this.midiNum);
	 * paintKeys(this.midiNum, true); } else { System.out.println(this.midiNum); paintKeys((this.midiNum - AsyncWorker.DATA_TYPE_DIFFERENCE), false); } }
	 * 
	 * //
	 * 
	 * private void paintKeys(int midiNum, boolean isTouched) { PianoKeyEnum pianoKeyEnum = PianoKeyEnum.findByMidiNum(midiNum); if (pianoKeyEnum != null) {
	 * Key[] keys = pianoKeyEnum.getKeys(); for (Key key : keys) { key.setBackground((isTouched) ? Key.KEY_COLOR_BACK : key.getColor()); } } }
	 * 
	 * }
	 */

}
