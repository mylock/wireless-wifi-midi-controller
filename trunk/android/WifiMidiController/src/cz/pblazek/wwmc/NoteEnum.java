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
import java.util.concurrent.ConcurrentHashMap;

import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.Region.Op;

/**
 * @author rtep.kezalb@gmail.com
 * 
 */
public enum NoteEnum {

	UNKNOWN("unknown", null, -1),

	N_C05("C05;", new Rect[] { new Rect(0, 2, 69, 240), new Rect(0, 240, 99, 478) }, 72),

	N_CIS05("C#05;", new Rect[] { new Rect(70, 1, 129, 239) }, 73),

	N_D05("D05;", new Rect[] { new Rect(130, 2, 169, 240), new Rect(100, 240, 199, 478) }, 74),

	N_DIS05("D#05;", new Rect[] { new Rect(170, 1, 229, 239) }, 75),

	N_E05("E05;", new Rect[] { new Rect(230, 2, 299, 240), new Rect(200, 240, 299, 478) }, 76),

	N_F05("F05;", new Rect[] { new Rect(300, 2, 369, 240), new Rect(300, 240, 399, 478) }, 77),

	N_FIS05("F#05;", new Rect[] { new Rect(370, 1, 429, 239) }, 78),

	N_G05("G05;", new Rect[] { new Rect(430, 2, 469, 240), new Rect(400, 240, 499, 478) }, 79),

	N_GIS05("G#05;", new Rect[] { new Rect(470, 1, 529, 239) }, 80),

	N_A05("A05;", new Rect[] { new Rect(530, 2, 569, 240), new Rect(500, 240, 599, 478) }, 81),

	N_AIS05("A#05;", new Rect[] { new Rect(570, 1, 629, 239) }, 82),

	N_H05("H05;", new Rect[] { new Rect(630, 2, 699, 240), new Rect(600, 240, 699, 478) }, 83),

	N_C06("C06;", new Rect[] { new Rect(700, 2, 799, 478) }, 84),

	;

	private final String tone;

	private final Rect[] keyRects;

	// TODO MIDI message

	private final int midiNum;

	// private final int[] rgb; // TODO future

	private NoteEnum(final String tone, final Rect[] keyRects, final int midiNum) {
		this.tone = tone; // TODO future
		this.keyRects = keyRects;
		this.midiNum = midiNum;
	}

	public String getTone() {
		return tone;
	}

	public Rect[] getKeyRects() {
		return keyRects;
	}

	public int getMidiNum() {
		return midiNum;
	}

	public static Map<NoteEnum, Region> getKeyRegions() {
		Map<NoteEnum, Region> keyRegions = new ConcurrentHashMap<NoteEnum, Region>(); // ???
		for (NoteEnum noteEnum : NoteEnum.values()) {
			if ((noteEnum != NoteEnum.UNKNOWN) && (noteEnum.getKeyRects() != null)) {
				Region keyRegion = new Region();
				for (Rect keyRect : noteEnum.getKeyRects()) {
					keyRegion.op(keyRect, Op.UNION);
				}
				keyRegions.put(noteEnum, keyRegion);
			}
		}
		return keyRegions;
	}

}
