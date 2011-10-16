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

import java.util.HashSet;
import java.util.Set;

import android.app.Application;
import android.database.Cursor;
import android.util.Log;
import cz.pblazek.wwmc.database.UdpClientAdapter;
import cz.pblazek.wwmc.database.UdpClientHelper;
import cz.pblazek.wwmc.network.UdpClient;
import cz.pblazek.wwmc.network.UdpReceiver;
import cz.pblazek.wwmc.network.UdpSender;

/**
 * @author rtep.kezalb@gmail.com
 * 
 */
public class WifiMidiControllerApplication extends Application {

	// TODO refactor synchronization

	private static final String LOG_TAG = WifiMidiControllerApplication.class.getSimpleName();

	private volatile UdpClientAdapter dbAdapter;

	private volatile UdpSender udpSender;

	private volatile UdpReceiver udpReceiver;

	// Application

	@Override
	public void onCreate() {
		super.onCreate();

		this.dbAdapter = new UdpClientAdapter(this);
	}

	// WifiMidiControllerApplication

	public UdpSender getUdpSender() {
		UdpSender udpSender = this.udpSender;
		if (udpSender == null) {
			synchronized (this) {
				udpSender = this.udpSender;
				if (udpSender == null) {
					this.udpSender = udpSender = new UdpSender(getEnabledUdpClients());
					Log.d(LOG_TAG, "+++ reset UdpSender");
				}
			}
		}
		return udpSender;
	}

	public UdpReceiver getUdpReceiver() {
		UdpReceiver udpReceiver = this.udpReceiver;
		if (udpReceiver == null) {
			synchronized (this) {
				udpReceiver = this.udpReceiver;
				if (udpReceiver == null) {
					this.udpReceiver = udpReceiver = new UdpReceiver();
					Log.d(LOG_TAG, "+++ reset UdpReceiver");
				}
			}
		}
		return udpReceiver;
	}

	// DB

	public synchronized void openDb() {
		this.dbAdapter.open();
		Log.d(LOG_TAG, "+++ open DB");
	}

	public synchronized void closeDb() {
		this.dbAdapter.close();
		Log.d(LOG_TAG, "+++ close DB");
	}

	public synchronized void addUdpClient(UdpClient udpClient) {
		if (udpClient != null) {
			if (this.dbAdapter.create(udpClient.getAddress(), udpClient.getPort(), false) > -1) {
				Log.d(LOG_TAG, "+++ UdpClient was added");
			}
		}
	}

	public synchronized Cursor fetchUdpClients() {
		Log.d(LOG_TAG, "+++ fetch all UdpClients");
		return this.dbAdapter.findAll();
	}

	public synchronized void enableUdpClient(long id, boolean enabled) {
		if (this.dbAdapter.setEnabledById(id, enabled)) {
			this.udpSender = null;
			Log.d(LOG_TAG, "+++ change one selection");
		}
	}

	public synchronized void enableUdpClients(boolean enabled) {
		if (this.dbAdapter.setEnabled(enabled)) {
			this.udpSender = null;
			Log.d(LOG_TAG, "+++ change selection");
		}
	}

	public synchronized void cleanDisabledUdpClients() {
		if (this.dbAdapter.removeByEnabled(false)) {
			Log.d(LOG_TAG, "+++ clean disabled UdpClients");
		}
	}

	private synchronized Set<UdpClient> getEnabledUdpClients() {
		Set<UdpClient> enabledUdpClients = new HashSet<UdpClient>();
		Cursor cursor = this.dbAdapter.findByEnabled(true);
		if ((cursor != null) && (cursor.moveToFirst())) {
			do {
				UdpClient udpClient = new UdpClient(cursor.getString(cursor.getColumnIndex(UdpClientHelper.TABLE_UDP_CLIENT_ADDRESS)), cursor.getInt(cursor
						.getColumnIndex(UdpClientHelper.TABLE_UDP_CLIENT_PORT)));
				enabledUdpClients.add(udpClient);
				Log.d(LOG_TAG, "+++ changed: " + udpClient);
			} while (cursor.moveToNext());
		}
		return enabledUdpClients;
	}

}
