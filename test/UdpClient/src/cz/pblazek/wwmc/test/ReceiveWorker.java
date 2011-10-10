/*
 * Copyright (C) 2011 The Wireless WiFi MIDI Controller Open Source Project
 * 
 * Licensed under the GNU General Public License, Version 3
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
