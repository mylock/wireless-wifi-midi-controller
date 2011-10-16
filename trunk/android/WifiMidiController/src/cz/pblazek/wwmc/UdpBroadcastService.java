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

package cz.pblazek.wwmc;

import java.util.concurrent.TimeUnit;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * @author rtep.kezalb@gmail.com
 * 
 */
public class UdpBroadcastService extends Service {

	private static final String LOG_TAG = UdpBroadcastService.class.getSimpleName();

	private static final int UDP_RECEIVER_WORKER_DELAY = 100;

	private WifiMidiControllerApplication application;

	private boolean udpReceiverWorkerStatus;

	private UdpReceiverWorker udpReceiverWorker;

	// Service

	@Override
	public void onCreate() {
		super.onCreate();

		this.application = ((WifiMidiControllerApplication) getApplication());
		this.udpReceiverWorkerStatus = false;
		this.udpReceiverWorker = new UdpReceiverWorker();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		this.udpReceiverWorkerStatus = false;
		this.udpReceiverWorker.interrupt();
		this.udpReceiverWorker = null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		this.udpReceiverWorkerStatus = true;
		this.udpReceiverWorker.start();
		return Service.START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	// UdpReceiverWorker

	private class UdpReceiverWorker extends Thread {

		public UdpReceiverWorker() {
			super(UdpBroadcastService.LOG_TAG + "---" + UdpReceiverWorker.class.getSimpleName());
		}

		// Thread

		@Override
		public void run() {
			WifiMidiControllerApplication application = UdpBroadcastService.this.application;
			boolean udpReceiverWorkerStatus = UdpBroadcastService.this.udpReceiverWorkerStatus;
			while (udpReceiverWorkerStatus) {
				try {
					application.addUdpClient(application.getUdpReceiver().receive());
					TimeUnit.MILLISECONDS.sleep(UdpBroadcastService.UDP_RECEIVER_WORKER_DELAY);
				} catch (InterruptedException e) {
					udpReceiverWorkerStatus = false;
				}
			}
		}

	}

}
