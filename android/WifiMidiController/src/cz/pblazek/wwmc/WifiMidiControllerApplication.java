/*
 * Copyright (C) 2011 The Wireless WiFi MIDI Controller Open Source Project
 * 
 * Licensed under the GNU General Public License, Version 3
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

	private UdpClientAdapter dbAdapter; // TODO close database helper

	private volatile UdpSender udpSender;

	private volatile UdpReceiver udpReceiver;

	// Application

	@Override
	public void onCreate() {
		super.onCreate();

		this.dbAdapter = new UdpClientAdapter(this);
		this.dbAdapter.open();
	}

	// WifiMidiControllerApplication

	public UdpSender getUdpSender() {
		UdpSender udpSender = this.udpSender;
		if (udpSender == null) {
			synchronized (this) {
				udpSender = this.udpSender;
				if (udpSender == null) {
					this.udpSender = udpSender = new UdpSender(getEnabledUdpClients());
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
				}
			}
		}
		return udpReceiver;
	}

	// DB

	public synchronized void addUdpClient(UdpClient udpClient) {
		if (udpClient != null) {
			Cursor cursor = this.dbAdapter.finByAddressAndPort(udpClient.getAddress(), udpClient.getPort());
			if (cursor.getCount() == 0) {
				this.dbAdapter.create(udpClient.getAddress(), udpClient.getPort(), false);
				Log.d(LOG_TAG, "+++ UdpClient was added");
			}
		}
	}

	public synchronized Cursor fetchUdpClients() {
		Log.d(LOG_TAG, "+++ fetch all UdpClients");
		return this.dbAdapter.findAll();
	}

	public synchronized void enableUdpClient(long id, boolean enabled) {
		this.dbAdapter.setEnabledById(id, enabled);
		this.udpSender = null;
		Log.d(LOG_TAG, "+++ reset UdpSender");
	}

	public synchronized void clearDisabledUdpClients() {
		this.dbAdapter.removeByEnabled(false);
		Log.d(LOG_TAG, "+++ clean disabled UdpClients");
	}

	private synchronized Set<UdpClient> getEnabledUdpClients() {
		Set<UdpClient> enabledUdpClients = new HashSet<UdpClient>();
		Cursor cursor = this.dbAdapter.findByEnabled(true);
		if (cursor.moveToFirst()) {
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
