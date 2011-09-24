/*
 * Copyright (C) 2011 The Wireless WiFi MIDI Controller Open Source Project
 * 
 * Licensed under the GNU General Public License, Version 3
 */
package cz.pblazek.wwmc;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * @author rtep.kezalb@gmail.com
 * 
 */
public class PianoRollActivity extends Activity implements OnClickListener {

	public static final String LOG_TAG = PianoRollActivity.class.getSimpleName();

	private WifiMidiControllerApplication application;

	// Activity

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.piano_roll);

		this.application = ((WifiMidiControllerApplication) getApplication());

		// TODO only for tests - remove this block
		Button button1 = (Button) findViewById(R.id.button_1);
		button1.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.piano_roll_options_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean optionsItemSelected = super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case R.id.menu_settings:
			Intent intent = new Intent(SettingsActivity.ACTION_NAME_SHOW_SETTINGS);
			startActivity(intent);
			optionsItemSelected = true;
			break;

		// case ...

		default:
			break;
		}
		return optionsItemSelected;
	}

	// OnClickListener

	@Override
	public void onClick(View view) {

		// TODO only for tests - remove this method
		sendTestNotes();
	}

	// AsyncTask

	private class ClientSender extends AsyncTask<String, Integer, Integer> {

		@Override
		protected Integer doInBackground(String... notes) {
			try {

				PianoRollActivity.this.application.getUdpSender().send(notes[0]);

			} catch (Exception e) {
				Log.e(LOG_TAG, "An error occurred when sending the UDP packet.");
			}

			// TODO

			return 0;
		}

	}

	// Test

	private void sendTestNotes() {
		for (NoteEnum noteEnum : NoteEnum.values()) {

			// TODO check the status

			new ClientSender().execute(noteEnum.getTone());
		}
	}

}