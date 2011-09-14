/*
 * Copyright (C) 2011 The Wireless WiFi MIDI Controller Open Source Project
 * 
 * Licensed under the GNU General Public License, Version 3
 */
package cz.pblazek.wwmc;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * @author rtep.kezalb@gmail.com
 * 
 */
public class WifiMidiControllerActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.options_menu, menu);

		//

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		boolean optionsItemSelected = super.onOptionsItemSelected(menuItem);

		Log.d("options_menu", String.format(">>> itemId=%s", menuItem.getItemId()));

		switch (menuItem.getItemId()) {
		case R.id.menu_piano_roll:

			//

			optionsItemSelected = true;
		case R.id.menu_settings:

			//

			optionsItemSelected = true;
		}

		Log.d("options_menu", String.format("<<< itemSelected=%s", optionsItemSelected));

		return optionsItemSelected;
	}

}