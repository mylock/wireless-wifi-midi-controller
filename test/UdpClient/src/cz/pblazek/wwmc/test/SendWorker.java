/*
 * Copyright (C) 2011 The Wireless WiFi MIDI Controller Open Source Project
 * 
 * Licensed under the GNU General Public License, Version 3
 */
package cz.pblazek.wwmc.test;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import cz.pblazek.wwmc.test.key.KeyEvent;
import cz.pblazek.wwmc.test.key.KeyEventListener;

/**
 * @author rtep.kezalb@gmail.com
 * 
 */
public class SendWorker extends Thread {

	private static final int WORKER_DELAY = 5000;

	private static final String WORKER_ADDRESS = "255.255.255.255";

	private static final int WORKER_PORT = 55555;

	private static final String WORKER_MESSAGE = "SEND" + Long.toHexString(new SecureRandom().nextLong()) + Long.toHexString(new SecureRandom().nextLong());

	private boolean enabled;

	private PianoRollFrame frame;

	private DatagramSocket socket;

	private ReceiveWorker receiveWorker;

	public SendWorker(PianoRollFrame frame) {
		super();
		this.enabled = false;
		this.frame = frame;
	}

	@Override
	public void run() {
		super.run();
		try {
			byte[] data = SendWorker.WORKER_MESSAGE.getBytes();
			DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getByName(SendWorker.WORKER_ADDRESS), SendWorker.WORKER_PORT);
			while (this.enabled) {
				try {
					this.socket.send(packet);
					TimeUnit.MILLISECONDS.sleep(SendWorker.WORKER_DELAY);
				} catch (InterruptedException e) {
					this.enabled = false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//

	public void open() {
		try {
			this.socket = new DatagramSocket();
			this.receiveWorker = new ReceiveWorker(this.socket);
			this.receiveWorker.open();
			this.receiveWorker.addKeyListener(new KeyEventListener() {

				@Override
				public void keyReceived(KeyEvent event) {
					if (event.getSource() instanceof byte[]) {
						byte[] data = (byte[]) event.getSource();
						SendWorker.this.frame.sendToPiano(new String(data));
					}
				}

			});
			this.enabled = true;
			this.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void close() {
		this.enabled = false;
		this.interrupt();
		this.receiveWorker.close();
		this.receiveWorker = null;
	}

}
