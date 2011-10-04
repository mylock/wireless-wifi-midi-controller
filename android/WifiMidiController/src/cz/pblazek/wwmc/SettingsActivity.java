/*
 * Copyright (C) 2011 The Wireless WiFi MIDI Controller Open Source Project
 * 
 * Licensed under the GNU General Public License, Version 3
 */
package cz.pblazek.wwmc;

import android.app.ListActivity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * @author rtep.kezalb@gmail.com
 * 
 */
public class SettingsActivity extends ListActivity implements OnSharedPreferenceChangeListener, OnItemClickListener {

	public static final String LOG_TAG = SettingsActivity.class.getSimpleName();

	public static final String ACTION_NAME_SHOW_SETTINGS = "cz.pblazek.wwmc.SHOW_SETTINGS";

	private SharedPreferences preferences;

	// ListActivity

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.preferences = PreferenceManager.getDefaultSharedPreferences(this);
		this.preferences.registerOnSharedPreferenceChangeListener(this);

		// TODO it is currently only a test
		ArrayAdapter<UdpClient> adapter = new ArrayAdapter<UdpClient>(this, android.R.layout.simple_list_item_multiple_choice, getTestUdpClients());
		this.setListAdapter(adapter);

		ListView listView = getListView();
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		listView.setOnItemClickListener(this);

		for (int i = 0; i < adapter.getCount(); i++) {
			if (this.preferences.contains(adapter.getItem(i).getIp())) {
				listView.setItemChecked(i, true);
			}
		}
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
		case R.id.menu_refresh:

			// TODO

			break;
		default:
			break;
		}
		return optionsItemSelected;
	}

	// OnSharedPreferenceChangeListener

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		Log.d(LOG_TAG, "+++ onSharedPreferenceChanged(" + sharedPreferences + ", " + key + ")");
	}

	// OnItemClickListener

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		Object item = adapterView.getItemAtPosition(position);
		if (item instanceof UdpClient) {
			UdpClient udpClient = (UdpClient) item;
			String ip = udpClient.getIp();
			int port = udpClient.getPort();

			Editor editor = this.preferences.edit();
			editor.remove(ip);
			if (getListView().isItemChecked(position)) {
				editor.putInt(ip, port);
			}
			editor.commit();
		}
	}

	// Test

	private UdpClient[] getTestUdpClients() {
		UdpClient[] udpClients = new UdpClient[10];
		for (int i = 0; i < udpClients.length; i++) {
			udpClients[i] = new UdpClient("255.255.255." + i, 8888);
		}
		return udpClients;
	}

}
