/*
 * Copyright (C) 2011 The Wireless WiFi MIDI Controller Open Source Project
 * 
 * Licensed under the GNU General Public License, Version 3
 */
package cz.pblazek.wwmc;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * @author rtep.kezalb@gmail.com
 * 
 */
public class UdpBroadcastService extends Service {

	public static final String LOG_TAG = UdpBroadcastService.class.getSimpleName();

	public static final int CLIENT_RECEIVER_DELAY = 500;

	private ClientReceiver clientReceiver;

	// Service

	@Override
	public void onCreate() {
		super.onCreate();
		this.clientReceiver = new ClientReceiver();
		Log.d(LOG_TAG, "+++ onCreate()");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		this.clientReceiver.start();
		Log.d(LOG_TAG, "+++ onStartCommand(" + intent + ", " + flags + ", " + startId + ")");
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {

		// TODO

		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		this.clientReceiver.interrupt();
		this.clientReceiver = null;
		Log.d(LOG_TAG, "+++ onDestroy()");
	}

	// Thread

	private class ClientReceiver extends Thread {

		public ClientReceiver() {
			super(UdpBroadcastService.class.getSimpleName() + "---" + ClientReceiver.class.getSimpleName());
		}

		@Override
		public void run() {
			while (clientReceiver != null) {
				try {

					// TODO

					Log.d(LOG_TAG, "+++ clientReceiver.run()");
					Thread.sleep(CLIENT_RECEIVER_DELAY);
				} catch (InterruptedException e) {

				}
			}
		}

	}

}
