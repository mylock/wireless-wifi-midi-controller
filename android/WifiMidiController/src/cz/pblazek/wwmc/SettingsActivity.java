/*
 * Copyright (C) 2011 The Wireless WiFi MIDI Controller Open Source Project
 * 
 * Licensed under the GNU General Public License, Version 3
 */
package cz.pblazek.wwmc;

import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import cz.pblazek.wwmc.database.UdpClientHelper;

/**
 * @author rtep.kezalb@gmail.com
 * 
 */
public class SettingsActivity extends ListActivity {

	public static final String ACTION_NAME_SHOW_SETTINGS = "cz.pblazek.wwmc.SHOW_SETTINGS";

	private WifiMidiControllerApplication application;

	// ListActivity

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.udp_client_list);

		this.application = ((WifiMidiControllerApplication) getApplication());

		resetListAdapter();
	}

	@Override
	protected void onPause() {
		super.onPause();
		this.application.cleanDisabledUdpClients();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.settings_options_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean optionsItemSelected = super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case R.id.menu_back:
			finish();
			optionsItemSelected = true;
			break;
		// TODO future
		// case R.id.menu_refresh:
		// break;
		case R.id.menu_select:
			this.application.enableUdpClients(true);
			resetListAdapter();
			optionsItemSelected = true;
			break;
		case R.id.menu_deselect:
			this.application.enableUdpClients(false);
			resetListAdapter();
			optionsItemSelected = true;
			break;
		default:
			break;
		}
		return optionsItemSelected;
	}

	// SettingsActivity

	private void resetListAdapter() {
		Cursor cursor = this.application.fetchUdpClients();
		if (cursor != null) {
			startManagingCursor(cursor);
			SimpleCursorAdapter listAdapter = new ListAdapter(this, R.layout.udp_client_row, cursor, new String[] { UdpClientHelper.TABLE_UDP_CLIENT_ADDRESS,
					UdpClientHelper.TABLE_UDP_CLIENT_PORT }, new int[] { R.id.list_row_address, R.id.list_row_port });
			setListAdapter(listAdapter);
		}
	}

	// ListAdapter

	private class ListAdapter extends SimpleCursorAdapter implements OnClickListener {

		public ListAdapter(Context context, int layout, Cursor cursor, String[] from, int[] to) {
			super(context, layout, cursor, from, to);
		}

		// SimpleCursorAdapter

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			super.bindView(view, context, cursor);
			int enabled = cursor.getInt(cursor.getColumnIndex(UdpClientHelper.TABLE_UDP_CLIENT_ENABLED));
			CheckBox enabledValue = (CheckBox) view.findViewById(R.id.list_row_enabled);
			enabledValue.setChecked(enabled == 1 ? true : false);
			enabledValue.setText(null);
			enabledValue.setTag(cursor.getLong(cursor.getColumnIndex(UdpClientHelper.TABLE_UDP_CLIENT_ID))); // for better performance
			enabledValue.setOnClickListener(this);
			TextView positionValue = (TextView) view.findViewById(R.id.list_row_position);
			positionValue.setText(String.format("%0" + String.valueOf(cursor.getCount()).length() + "d", cursor.getPosition() + 1));
		}

		// OnClickListener

		// FIXME fake :-(
		// I have a problem with onListItemClick on the original ListView because the row layout is defined outside the list layout!? Event is not invoked.

		@Override
		public void onClick(View view) {
			CheckBox enabledValue = (CheckBox) view.findViewById(R.id.list_row_enabled);
			Object tag = enabledValue.getTag();
			SettingsActivity settingsActivity = SettingsActivity.this;
			if (tag instanceof Long) {
				long id = (Long) enabledValue.getTag();

				// TODO freeze cursor position!

				settingsActivity.application.enableUdpClient(id, enabledValue.isChecked());
			}
			settingsActivity.resetListAdapter();
		}

	}

}
