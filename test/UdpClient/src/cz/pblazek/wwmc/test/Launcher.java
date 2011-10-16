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
