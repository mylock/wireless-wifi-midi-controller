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
		PianoRollActivity.application.openDb();
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
	}

	@Override
	protected void onDestroy() {
		PianoRollActivity.application.cleanDisabledUdpClients();
		PianoRollActivity.application.closeDb();
		super.onDestroy();
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

		private static final int DATA_TYPE_DIFFERENCE = 500;

		private Map<NoteEnum, Region> keyRegions;

		private Map<Integer, Region> touchedKeyRegions;

		private Map<Integer, Integer> midiNumBuffer;

		private Paint keyPaint;

		// TODO other constructors should be ...

		public PianoRollView(Context context, AttributeSet attrs) {
			super(context, attrs);

			this.keyRegions = NoteEnum.getKeyRegions();

			this.touchedKeyRegions = new ConcurrentHashMap<Integer, Region>(); // ???
			this.midiNumBuffer = new ConcurrentHashMap<Integer, Integer>(); // ???

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

			// TODO refactoring - undesirable behavior keyboard appears!
			// TODO multitouched slide

			Point point = null;
			switch (action) {
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_POINTER_DOWN:
				point = new Point((int) event.getX(pointerIndex), (int) event.getY(pointerIndex));
				for (Entry<NoteEnum, Region> entry : this.keyRegions.entrySet()) {
					Region keyRegion = entry.getValue();
					if ((keyRegion.contains(point.x, point.y)) && (!this.touchedKeyRegions.containsValue(keyRegion))) {
						put(entry.getKey().getMidiNum(), pointerIndex, keyRegion);
						break;
					}
				}
				break;
			case MotionEvent.ACTION_UP:
				clear();
				break;
			case MotionEvent.ACTION_POINTER_UP:
				Region touchedKeyRegion = this.touchedKeyRegions.get(pointerIndex);
				if (touchedKeyRegion != null) {
					remove(pointerIndex);
				}
				break;
			case MotionEvent.ACTION_MOVE:
				point = new Point((int) event.getX(pointerIndex), (int) event.getY(pointerIndex));
				for (Entry<NoteEnum, Region> entry : this.keyRegions.entrySet()) {
					Region keyRegion = entry.getValue();
					if ((keyRegion.contains(point.x, point.y)) && (!this.touchedKeyRegions.containsValue(keyRegion))) {
						event.setAction(MotionEvent.ACTION_UP);
						onTouchEvent(event);
						put(entry.getKey().getMidiNum(), pointerIndex, keyRegion);
						break;
					}
				}
				break;
			default:
				event.setAction(MotionEvent.ACTION_UP);
				onTouchEvent(event);
				break;
			}

			invalidate();
			return true;
		}

		// TODO

		private synchronized void put(int midiNum, int pointerIndex, Region keyRegion) {
			new PianoRollActivity.UdpSenderWorker().execute(midiNum);
			this.touchedKeyRegions.put(pointerIndex, keyRegion);
			this.midiNumBuffer.put(pointerIndex, midiNum);
		}

		private synchronized void remove(int pointerIndex) {
			Integer midiNum = this.midiNumBuffer.get(pointerIndex);
			if (midiNum != null) {
				new PianoRollActivity.UdpSenderWorker().execute(midiNum + PianoRollView.DATA_TYPE_DIFFERENCE);
				this.touchedKeyRegions.remove(pointerIndex);
				this.midiNumBuffer.remove(pointerIndex);
			}
		}

		private synchronized void clear() {
			for (Integer key : this.touchedKeyRegions.keySet()) {
				new PianoRollActivity.UdpSenderWorker().execute(this.midiNumBuffer.get(key) + PianoRollView.DATA_TYPE_DIFFERENCE);
			}
			this.touchedKeyRegions.clear();
			this.midiNumBuffer.clear();
		}

	}

	// UdpSenderWorker

	private static class UdpSenderWorker extends AsyncTask<Integer, Integer, Integer> {

		@Override
		protected Integer doInBackground(Integer... midiNums) {
			if (midiNums != null) {
				for (Integer midiNum : midiNums) {
					byte[] data = new byte[] { (byte) (midiNum >>> 24), (byte) (midiNum >> 16 & 0xff), (byte) (midiNum >> 8 & 0xff), (byte) (midiNum & 0xff) };
					PianoRollActivity.application.getUdpSender().send(data);
				}
			}
			return 0; // TODO it can be 0 (for future use)
		}

	}

}