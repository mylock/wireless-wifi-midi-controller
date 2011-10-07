/*
 * Copyright (C) 2011 The Wireless WiFi MIDI Controller Open Source Project
 * 
 * Licensed under the GNU General Public License, Version 3
 */
package cz.pblazek.wwmc.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author rtep.kezalb@gmail.com
 * 
 */
public class UdpClientAdapter {

	private Context context;

	private SQLiteDatabase database;

	private UdpClientHelper dbHelper;

	public UdpClientAdapter(Context context) {
		this.context = context;
	}

	// UdpClientAdapter

	public UdpClientAdapter open() throws SQLException {
		this.dbHelper = new UdpClientHelper(context);
		this.database = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		this.dbHelper.close();
	}

	public long create(String address, int port, boolean enabled) {
		ContentValues contentValues = UdpClientAdapter.createContentValues(address, port, enabled ? 1 : 0);
		return this.database.insert(UdpClientHelper.TABLE_UDP_CLIENT, null, contentValues);
	}

	public boolean setEnabledById(long id, boolean enabled) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(UdpClientHelper.TABLE_UDP_CLIENT_ENABLED, (enabled ? 1 : 0));
		return this.database.update(UdpClientHelper.TABLE_UDP_CLIENT, contentValues, UdpClientHelper.TABLE_UDP_CLIENT_ID + "=" + id, null) > 0;
	}

	public boolean removeByEnabled(boolean enabled) {
		return this.database.delete(UdpClientHelper.TABLE_UDP_CLIENT, UdpClientHelper.TABLE_UDP_CLIENT_ENABLED + "=" + (enabled ? 1 : 0), null) > 0;
	}

	public Cursor findByEnabled(boolean enabled) {
		return this.database.query(UdpClientHelper.TABLE_UDP_CLIENT, new String[] { UdpClientHelper.TABLE_UDP_CLIENT_ID,
				UdpClientHelper.TABLE_UDP_CLIENT_ADDRESS, UdpClientHelper.TABLE_UDP_CLIENT_PORT, UdpClientHelper.TABLE_UDP_CLIENT_ENABLED },
				UdpClientHelper.TABLE_UDP_CLIENT_ENABLED + "=" + (enabled ? 1 : 0), null, null, null, null);
	}

	public Cursor finByAddressAndPort(String address, int port) {
		return this.database.query(UdpClientHelper.TABLE_UDP_CLIENT, new String[] { UdpClientHelper.TABLE_UDP_CLIENT_ID,
				UdpClientHelper.TABLE_UDP_CLIENT_ADDRESS, UdpClientHelper.TABLE_UDP_CLIENT_PORT, UdpClientHelper.TABLE_UDP_CLIENT_ENABLED },
				UdpClientHelper.TABLE_UDP_CLIENT_ADDRESS + "='" + address + "' AND " + UdpClientHelper.TABLE_UDP_CLIENT_PORT + "=" + port, null, null, null,
				null);
	}

	public Cursor findAll() {
		return this.database.query(UdpClientHelper.TABLE_UDP_CLIENT, new String[] { UdpClientHelper.TABLE_UDP_CLIENT_ID,
				UdpClientHelper.TABLE_UDP_CLIENT_ADDRESS, UdpClientHelper.TABLE_UDP_CLIENT_PORT, UdpClientHelper.TABLE_UDP_CLIENT_ENABLED }, null, null, null,
				null, null);
	}

	private static ContentValues createContentValues(String address, int port, int enabled) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(UdpClientHelper.TABLE_UDP_CLIENT_ADDRESS, address);
		contentValues.put(UdpClientHelper.TABLE_UDP_CLIENT_PORT, port);
		contentValues.put(UdpClientHelper.TABLE_UDP_CLIENT_ENABLED, enabled);
		return contentValues;
	}

}
