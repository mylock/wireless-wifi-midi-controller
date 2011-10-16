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

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Set;

import android.util.Log;

/**
 * @author rtep.kezalb@gmail.com
 * 
 */
public class UdpSender {

	private static final String LOG_TAG = UdpSender.class.getSimpleName();

	private Set<UdpClient> udpClients;

	private DatagramSocket socket;

	public UdpSender(Set<UdpClient> udpClients) {
		super();
		this.udpClients = udpClients;
	}

	// UdpSender

	// TODO synchronize different order

	public synchronized void send(byte[] data) {
		try {
			this.socket = new DatagramSocket();
			for (UdpClient udpClient : this.udpClients) {
				send(udpClient, data);
			}
		} catch (SocketException e) {
			Log.e(LOG_TAG, "An error occurred while opening the UDP socket (sender).");
		} finally {
			if (this.socket != null) {
				this.socket.close();
			}
		}
	}

	private void send(UdpClient udpClient, byte[] data) {
		try {
			InetAddress address = InetAddress.getByName(udpClient.getAddress());
			int port = udpClient.getPort();
			DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
			this.socket.send(packet);
			// Log.d(LOG_TAG, "+++ Packet [data=" + new String(data) + ", data.length=" + data.length + ", address=" + address.getHostAddress() + ", port=" +
			// port + "]");
		} catch (Exception e) {
			Log.e(LOG_TAG, "An error occurred when sending the UDP packet. (Network is unreachable.)");
		}
	}

}
