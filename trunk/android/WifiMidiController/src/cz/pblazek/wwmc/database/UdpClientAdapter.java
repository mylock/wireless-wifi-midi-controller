/*
 * Copyright (C) 2011 The Wireless WiFi MIDI Controller Open Source Project
 * 
 * Licensed under the GNU General Public License, Version 3
 */
package cz.pblazek.wwmc.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author rtep.kezalb@gmail.com
 * 
 */
public class UdpClientAdapter {

	private UdpClientHelper dbHelper;

	private SQLiteDatabase database;

	public UdpClientAdapter(Context context) {
		this.dbHelper = new UdpClientHelper(context);
	}

	// UdpClientAdapter

	public UdpClientAdapter open() {
		this.database = this.dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		this.database.close();
		this.dbHelper.close();
	}

	public long create(String address, int port, boolean enabled) {
		long id = -1;
		if (this.database.isOpen()) {
			this.database.beginTransaction();
			try {
				Cursor cursor = this.database.query(UdpClientHelper.TABLE_UDP_CLIENT, new String[] { UdpClientHelper.TABLE_UDP_CLIENT_ID,
						UdpClientHelper.TABLE_UDP_CLIENT_ADDRESS, UdpClientHelper.TABLE_UDP_CLIENT_PORT, UdpClientHelper.TABLE_UDP_CLIENT_ENABLED },
						UdpClientHelper.TABLE_UDP_CLIENT_ADDRESS + "='" + address + "' AND " + UdpClientHelper.TABLE_UDP_CLIENT_PORT + "=" + port, null, null,
						null, null);
				if (cursor.getCount() == 0) {
					ContentValues contentValues = UdpClientAdapter.createContentValues(address, port, enabled ? 1 : 0);
					id = this.database.insert(UdpClientHelper.TABLE_UDP_CLIENT, null, contentValues);
				}
				this.database.setTransactionSuccessful();
			} finally {
				this.database.endTransaction();
			}
		}
		return id;
	}

	public boolean setEnabledById(long id, boolean enabled) {
		boolean status = false;
		if (this.database.isOpen()) {
			this.database.beginTransaction();
			try {
				ContentValues contentValues = new ContentValues();
				contentValues.put(UdpClientHelper.TABLE_UDP_CLIENT_ENABLED, (enabled ? 1 : 0));
				status = this.database.update(UdpClientHelper.TABLE_UDP_CLIENT, contentValues, UdpClientHelper.TABLE_UDP_CLIENT_ID + "=" + id, null) > 0;
				this.database.setTransactionSuccessful();
			} finally {
				this.database.endTransaction();
			}
		}
		return status;
	}

	public boolean setEnabled(boolean enabled) {
		boolean status = false;
		if (this.database.isOpen()) {
			this.database.beginTransaction();
			try {
				ContentValues contentValues = new ContentValues();
				contentValues.put(UdpClientHelper.TABLE_UDP_CLIENT_ENABLED, (enabled ? 1 : 0));
				status = this.database.update(UdpClientHelper.TABLE_UDP_CLIENT, contentValues, null, null) > 0;
				this.database.setTransactionSuccessful();
			} finally {
				this.database.endTransaction();
			}
		}
		return status;

	}

	public boolean removeByEnabled(boolean enabled) {
		boolean status = false;
		if (this.database.isOpen()) {
			this.database.beginTransaction();
			try {
				status = this.database.delete(UdpClientHelper.TABLE_UDP_CLIENT, UdpClientHelper.TABLE_UDP_CLIENT_ENABLED + "=" + (enabled ? 1 : 0), null) > 0;
				this.database.setTransactionSuccessful();
			} finally {
				this.database.endTransaction();
			}
		}
		return status;
	}

	public Cursor findByEnabled(boolean enabled) {
		Cursor cursor = null;
		if (this.database.isOpen()) {
			cursor = this.database.query(UdpClientHelper.TABLE_UDP_CLIENT, new String[] { UdpClientHelper.TABLE_UDP_CLIENT_ID,
					UdpClientHelper.TABLE_UDP_CLIENT_ADDRESS, UdpClientHelper.TABLE_UDP_CLIENT_PORT, UdpClientHelper.TABLE_UDP_CLIENT_ENABLED },
					UdpClientHelper.TABLE_UDP_CLIENT_ENABLED + "=" + (enabled ? 1 : 0), null, null, null, null);
		}
		return cursor;
	}

	public Cursor findAll() {
		Cursor cursor = null;
		if (this.database.isOpen()) {
			cursor = this.database.query(UdpClientHelper.TABLE_UDP_CLIENT, new String[] { UdpClientHelper.TABLE_UDP_CLIENT_ID,
					UdpClientHelper.TABLE_UDP_CLIENT_ADDRESS, UdpClientHelper.TABLE_UDP_CLIENT_PORT, UdpClientHelper.TABLE_UDP_CLIENT_ENABLED }, null, null,
					null, null, null);
		}
		return cursor;
	}

	// it has only one use (future)
	private static ContentValues createContentValues(String address, int port, int enabled) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(UdpClientHelper.TABLE_UDP_CLIENT_ADDRESS, address);
		contentValues.put(UdpClientHelper.TABLE_UDP_CLIENT_PORT, port);
		contentValues.put(UdpClientHelper.TABLE_UDP_CLIENT_ENABLED, enabled);
		return contentValues;
	}

}