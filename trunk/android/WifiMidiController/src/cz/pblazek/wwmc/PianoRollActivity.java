/*
 * Copyright (C) 2011 The Wireless WiFi MIDI Controller Open Source Project
 * 
 * Licensed under the GNU General Public License, Version 3
 */
package cz.pblazek.wwmc;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Region;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

/**
 * @author rtep.kezalb@gmail.com
 * 
 */
public class PianoRollActivity extends Activity {

	private static WifiMidiControllerApplication application;

	// Activity

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().addFlags(LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

		setContentView(R.layout.piano_roll);
		((PianoRollActivity.PianoRollView) findViewById(R.id.view_piano_roll)).invalidate();

		PianoRollActivity.application = ((WifiMidiControllerApplication) getApplication());
	}

	@Override
	protected void onResume() {
		super.onResume();
		Intent intent = new Intent(UdpBroadcastReceiver.ACTION_NAME_UDP_BROADCAST_RECEIVER);
		intent.putExtra(UdpBroadcastReceiver.UDP_BROADCAST_RECEIVER_STATUS, true);
		sendBroadcast(intent);
	}

	@Override
	protected void onPause() {
		super.onPause();
		Intent intent = new Intent(UdpBroadcastReceiver.ACTION_NAME_UDP_BROADCAST_RECEIVER);
		intent.putExtra(UdpBroadcastReceiver.UDP_BROADCAST_RECEIVER_STATUS, false);
		sendBroadcast(intent);
		PianoRollActivity.application.cleanDisabledUdpClients();
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
		default:
			break;
		}
		return optionsItemSelected;
	}

	// PianoRollView

	private static class PianoRollView extends View {

		private Map<NoteEnum, Region> keyRegions;

		private Map<Integer, Region> touchedKeyRegions;

		private Paint keyPaint;

		// TODO other constructors should be ...

		public PianoRollView(Context context, AttributeSet attrs) {
			super(context, attrs);

			this.keyRegions = NoteEnum.getKeyRegions();

			this.touchedKeyRegions = new ConcurrentHashMap<Integer, Region>(); // ???

			this.keyPaint = new Paint();
			this.keyPaint.setStyle(Style.FILL);
			this.keyPaint.setARGB(255, 255, 180, 0);
		}

		// View

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			for (Region touchedKeyRegion : this.touchedKeyRegions.values()) {
				canvas.drawPath(touchedKeyRegion.getBoundaryPath(), this.keyPaint);
			}
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			int action = event.getActionMasked();
			int pointerIndex = event.getActionIndex();
			// int pointerId = event.getPointerId(pointerIndex);

			switch (action) {
			case MotionEvent.ACTION_MOVE:
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_POINTER_DOWN:

				// TODO refactoring - undesirable behavior keyboard appears!
				// TODO multitouched slide

				Point point = new Point((int) event.getX(pointerIndex), (int) event.getY(pointerIndex));

				for (Entry<NoteEnum, Region> entry : this.keyRegions.entrySet()) {
					Region keyRegion = entry.getValue();
					if ((keyRegion.contains(point.x, point.y)) && (!this.touchedKeyRegions.containsValue(keyRegion))) {
						new PianoRollActivity.UdpSenderWorker().execute(entry.getKey());
						this.touchedKeyRegions.put(Integer.valueOf(pointerIndex), keyRegion);
						break;
					}
				}

				break;
			case MotionEvent.ACTION_UP:
				this.touchedKeyRegions.clear();
				break;
			case MotionEvent.ACTION_POINTER_UP:
				this.touchedKeyRegions.remove(pointerIndex);
				break;
			default:
				break;
			}

			invalidate();
			return true;
		}

	}

	// UdpSenderWorker

	private static class UdpSenderWorker extends AsyncTask<NoteEnum, Integer, Integer> {

		@Override
		protected Integer doInBackground(NoteEnum... noteEnums) {
			for (NoteEnum noteEnum : noteEnums) {
				PianoRollActivity.application.getUdpSender().send(noteEnum.getTone());
			}
			return 0; // TODO it can be 0 (for future use)
		}

	}

}