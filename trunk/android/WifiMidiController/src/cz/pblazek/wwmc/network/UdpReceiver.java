/*
 * Copyright (C) 2011 The Wireless WiFi MIDI Controller Open Source Project
 * 
 * Licensed under the GNU General Public License, Version 3
 */
package cz.pblazek.wwmc.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.MulticastSocket;
import java.net.SocketException;

import android.util.Log;

/**
 * @author rtep.kezalb@gmail.com
 * 
 */
public class UdpReceiver {

	private static final String LOG_TAG = UdpReceiver.class.getSimpleName();

	private static int UDP_RECEIVER_BROADCAST_PORT = 55555;

	private static int UDP_RECEIVER_TIMEOUT = 5000;

	// UdpReceiver

	public synchronized UdpClient receive() {
		UdpClient udpClient = null;
		DatagramSocket socket = null; // TODO maybe ... MulticastSocket
		try {
			socket = new MulticastSocket(UdpReceiver.UDP_RECEIVER_BROADCAST_PORT);
			socket.setSoTimeout(UdpReceiver.UDP_RECEIVER_TIMEOUT);
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			socket.receive(packet);
			udpClient = new UdpClient(packet.getAddress().getHostAddress(), packet.getPort());
			Log.d(LOG_TAG, "+++ " + udpClient);
		} catch (SocketException e) {
			Log.e(LOG_TAG, "An error occurred while opening the UDP socket (receiver).");
		} catch (IOException e) {
			Log.e(LOG_TAG, "An error occurred when receiving the UDP packet. (Network is unreachable.)");
		} finally {
			if (socket != null) {
				socket.close();
				socket = null;
			}
		}
		return udpClient;
	}

}
