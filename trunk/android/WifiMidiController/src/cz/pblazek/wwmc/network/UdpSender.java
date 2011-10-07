/*
 * Copyright (C) 2011 The Wireless WiFi MIDI Controller Open Source Project
 * 
 * Licensed under the GNU General Public License, Version 3
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
		try {
			this.socket = new DatagramSocket();
		} catch (SocketException e) {
			Log.e(LOG_TAG, "An error occurred while opening the UDP socket (sender).");
		}
	}

	// UdpSender

	// TODO synchronize different order

	public void send(String rawData) {
		byte[] data = rawData.getBytes();
		for (UdpClient udpClient : this.udpClients) {
			send(udpClient, data);
		}
	}

	private void send(UdpClient udpClient, byte[] data) {
		try {
			InetAddress address = InetAddress.getByName(udpClient.getAddress());
			int port = udpClient.getPort();
			DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
			this.socket.send(packet);
			// Log.d(LOG_TAG, "+++ Packet [data=" + new String(data) +
			// ", data.length=" + data.length + ", address=" +
			// address.getHostAddress() + ", port=" + port
			// + "]");
		} catch (Exception e) {
			Log.e(LOG_TAG, "An error occurred when sending the UDP packet. (Network is unreachable.)");
		}
	}

	// TODO close ...

}
