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

import java.io.InterruptedIOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import javax.swing.event.EventListenerList;

import cz.pblazek.wwmc.test.key.KeyEvent;
import cz.pblazek.wwmc.test.key.KeyEventListener;

/**
 * @author rtep.kezalb@gmail.com
 * 
 */
public class ReceiveWorker extends Thread {

	private static final int WORKER_TIMEOUT = 1000;

	private boolean enabled;

	private DatagramSocket socket;

	private EventListenerList listenerList;

	public ReceiveWorker(DatagramSocket socket) {
		super();
		this.enabled = false;
		this.socket = socket;
		this.listenerList = new EventListenerList();
	}

	@Override
	public void run() {
		super.run();
		try {
			this.socket.setSoTimeout(ReceiveWorker.WORKER_TIMEOUT);
			byte[] data = new byte[8];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			while (this.enabled) {
				try {
					this.socket.receive(packet);
					fireKeyEvent(new KeyEvent(packet.getData()));
				} catch (InterruptedIOException e) {

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//

	public void open() {
		this.enabled = true;
		this.start();
	}

	public void close() {
		this.enabled = false;
		this.interrupt();
	}

	public void addKeyListener(KeyEventListener listener) {
		this.listenerList.add(KeyEventListener.class, listener);
	}

	public void removeKeyListener(KeyEventListener listener) {
		this.listenerList.remove(KeyEventListener.class, listener);
	}

	private void fireKeyEvent(KeyEvent event) {
		Object[] listeners = this.listenerList.getListenerList();
		for (int i = 0; i < listeners.length; i += 2) {
			if (listeners[i] == KeyEventListener.class) {
				((KeyEventListener) listeners[i + 1]).keyReceived(event);
			}
		}
	}

}
