/*
 * Copyright (C) 2011 The Wireless WiFi MIDI Controller Open Source Project
 * 
 * Licensed under the GNU General Public License, Version 3
 */
package cz.pblazek.wwmc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * @author rtep.kezalb@gmail.com
 * 
 */
public class UdpBroadcastReceiver extends BroadcastReceiver {

	public static final String ACTION_NAME_UDP_BROADCAST_RECEIVER = "cz.pblazek.wwmc.UDP_BROADCAST_RECEIVER";

	public static final String UDP_BROADCAST_RECEIVER_STATUS = "cz.pblazek.wwmc.UDP_BROADCAST_RECEIVER_STATUS";

	// private static MulticastLock multicastLock;

	// BroadcastReceiver

	@Override
	public void onReceive(Context context, Intent intent) {
		Toast toast;
		if (intent.getBooleanExtra(UdpBroadcastReceiver.UDP_BROADCAST_RECEIVER_STATUS, false)) {

			// TODO
			// require - android.permission.CHANGE_WIFI_MULTICAST_STATE
			// WifiManager wifiMananger = (WifiManager)
			// context.getSystemService(Context.WIFI_SERVICE);
			// UdpBroadcastReceiver.multicastLock =
			// wifiMananger.createMulticastLock(UdpBroadcastReceiver.ACTION_NAME_UDP_BROADCAST_RECEIVER);
			// UdpBroadcastReceiver.multicastLock.acquire();

			context.startService(new Intent(context, UdpBroadcastService.class));
			toast = Toast.makeText(context, "UDP broadcast receiver starts ...", Toast.LENGTH_SHORT);
		} else {
			context.stopService(new Intent(context, UdpBroadcastService.class));

			// UdpBroadcastReceiver.multicastLock.release();

			toast = Toast.makeText(context, "UDP broadcast receiver stops ...", Toast.LENGTH_SHORT);
		}
		toast.show();
	}

}
