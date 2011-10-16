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

package cz.pblazek.wwmc.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * @author rtep.kezalb@gmail.com
 * 
 */
public class UdpClientHelper extends SQLiteOpenHelper {

	private static final String LOG_TAG = UdpClientHelper.class.getSimpleName();

	private static final String DB_NAME = "wwmc.db";

	private static final int DB_VERSION = 1;

	public static final String TABLE_UDP_CLIENT = "UDP_CLIENT";

	public static final String TABLE_UDP_CLIENT_ID = BaseColumns._ID;

	public static final String TABLE_UDP_CLIENT_ADDRESS = "ADDRESS";

	public static final String TABLE_UDP_CLIENT_PORT = "PORT";

	public static final String TABLE_UDP_CLIENT_ENABLED = "ENABLED";

	public UdpClientHelper(Context context) {
		super(context, UdpClientHelper.DB_NAME, null, UdpClientHelper.DB_VERSION);
	}

	// SQLiteOpenHelper

	@Override
	public void onCreate(SQLiteDatabase database) {
		StringBuilder sql = new StringBuilder(200);
		sql.append("CREATE TABLE ").append(UdpClientHelper.TABLE_UDP_CLIENT);
		sql.append(" ( ");
		sql.append(UdpClientHelper.TABLE_UDP_CLIENT_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ");
		sql.append(UdpClientHelper.TABLE_UDP_CLIENT_ADDRESS).append(" TEXT NOT NULL, ");
		sql.append(UdpClientHelper.TABLE_UDP_CLIENT_PORT).append(" INTEGER NOT NULL, ");
		sql.append(UdpClientHelper.TABLE_UDP_CLIENT_ENABLED).append(" INTEGER NOT NULL ");
		sql.append(" );");
		database.execSQL(sql.toString());
		Log.d(LOG_TAG, "+++ database was created");
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		StringBuilder sql = new StringBuilder(100);
		sql.append("drop table if exists ").append(UdpClientHelper.TABLE_UDP_CLIENT).append(" ;");
		database.execSQL(sql.toString());
		this.onCreate(database);
		Log.d(LOG_TAG, "+++ database was upgraded");
	}

}
