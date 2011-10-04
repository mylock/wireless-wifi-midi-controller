/*
 * Copyright (C) 2011 The Wireless WiFi MIDI Controller Open Source Project
 * 
 * Licensed under the GNU General Public License, Version 3
 */
package cz.pblazek.wwmc;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.List;

import android.util.Log;

/**
 * @author rtep.kezalb@gmail.com
 * 
 */
public class UdpSender {

	public static final String LOG_TAG = UdpSender.class.getSimpleName();

	private List<UdpClient> udpClients;

	private DatagramSocket socket;

	public UdpSender(List<UdpClient> udpClients) {
		super();
		this.udpClients = udpClients;
		try {
			this.socket = new DatagramSocket();
		} catch (SocketException e) {
			Log.e(LOG_TAG, "An error occurred while opening the UDP socket.");
		}
	}

	public void send(String rawData) throws Exception {
		for (UdpClient udpClient : this.udpClients) {
			byte[] data = rawData.getBytes();
			InetAddress address = InetAddress.getByName(udpClient.getIp());
			int port = udpClient.getPort();
			DatagramPacket packet = new DatagramPacket(data, data.length, address, port);

			this.socket.send(packet);
			Log.d(LOG_TAG, "+++ Packet [data=" + data.toString() + ", data.length=" + data.length + ", client=" + address + ":" + port + "]");
		}
	}

}
