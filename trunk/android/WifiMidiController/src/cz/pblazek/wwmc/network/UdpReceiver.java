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

package cz.pblazek.wwmc.network;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import android.util.Log;

/**
 * @author rtep.kezalb@gmail.com
 * 
 */
public class UdpReceiver {

	private static final String LOG_TAG = UdpReceiver.class.getSimpleName();

	private static int UDP_RECEIVER_BROADCAST_PORT = 55555;

	private static int UDP_RECEIVER_TIMEOUT = 2000;

	// UdpReceiver

	public synchronized UdpClient receive() {
		UdpClient udpClient = null;
		DatagramSocket socket = null; // TODO maybe ... MulticastSocket
		try {
			socket = new DatagramSocket(UdpReceiver.UDP_RECEIVER_BROADCAST_PORT);
			socket.setSoTimeout(UdpReceiver.UDP_RECEIVER_TIMEOUT);
			byte[] data = new byte[512];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			socket.receive(packet);
			udpClient = new UdpClient(packet.getAddress().getHostAddress(), packet.getPort());
			// Log.d(LOG_TAG, "+++ " + udpClient);
		} catch (InterruptedIOException e) {
			// socket received timeout
		} catch (SocketException e) {
			Log.e(LOG_TAG, "An error occurred while opening the UDP socket (receiver).");
		} catch (IOException e) {
			Log.e(LOG_TAG, "An error occurred when receiving the UDP packet. (Network is unreachable.)");
		} finally {
			if (socket != null) {
				socket.close();
			}
		}
		return udpClient;
	}

}
