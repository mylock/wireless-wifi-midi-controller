/*
 * Copyright (C) 2011 The Wireless WiFi MIDI Controller Open Source Project
 * 
 * Licensed under the GNU General Public License, Version 3
 */
package cz.pblazek.wwmc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * @author rtep.kezalb@gmail.com
 * 
 */
public class WifiMidiControllerApplication extends Application implements OnSharedPreferenceChangeListener {

	public static final String LOG_TAG = WifiMidiControllerApplication.class.getSimpleName();

	private SharedPreferences preferences;

	private volatile UdpSender udpSender;

	private volatile UdpReceiver udpReceiver;

	// Application

	@Override
	public void onCreate() {
		super.onCreate();

		this.preferences = PreferenceManager.getDefaultSharedPreferences(this);
		this.preferences.registerOnSharedPreferenceChangeListener(this);

		// TODO startService(new Intent(this, UdpBroadcastService.class));
	}

	// OnSharedPreferenceChangeListener

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub

		super.onTerminate();
	}

	// TODO destroy ... stopService(new Intent(this,
	// UdpBroadcastService.class));

	@Override
	public synchronized void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

		// reset

		this.udpSender = null;
		this.udpReceiver = null;
	}

	// WifiMidiControllerApplication

	public UdpSender getUdpSender() {
		UdpSender udpSender = this.udpSender;
		if (udpSender == null) {
			synchronized (this) {
				udpSender = this.udpSender;
				if (udpSender == null) {
					this.udpSender = udpSender = new UdpSender(getUdpClients());
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

	private List<UdpClient> getUdpClients() {
		List<UdpClient> udpClients = new ArrayList<UdpClient>();
		for (Entry<String, ?> entry : this.preferences.getAll().entrySet()) {
			UdpClient udpClient = new UdpClient(entry.getKey(), (Integer) entry.getValue());
			udpClients.add(udpClient);
			Log.d(LOG_TAG, "+++ " + udpClient);
		}
		return udpClients;
	}

}