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

			// TODO future
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
