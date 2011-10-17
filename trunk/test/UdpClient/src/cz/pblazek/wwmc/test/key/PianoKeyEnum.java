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

package cz.pblazek.wwmc.test.key;

import java.awt.Color;

/**
 * @author rtep.kezalb@gmail.com
 * 
 */
public enum PianoKeyEnum {

	PK_C04("C04;", new Key[] { new Key(1, 1, 60, 136, Color.WHITE), new Key(1, 137, 80, 136, Color.WHITE) }, 60),

	PK_CIS04("C#04;", new Key[] { new Key(62, 1, 40, 136, Color.BLACK) }, 61),

	PK_D04("D04;", new Key[] { new Key(102, 1, 40, 136, Color.WHITE), new Key(82, 137, 80, 136, Color.WHITE) }, 62),

	PK_DIS04("D#04;", new Key[] { new Key(143, 1, 40, 136, Color.BLACK) }, 63),

	PK_E04("E04;", new Key[] { new Key(183, 1, 60, 136, Color.WHITE), new Key(163, 137, 80, 136, Color.WHITE) }, 64),

	PK_F04("F04;", new Key[] { new Key(244, 1, 60, 136, Color.WHITE), new Key(244, 137, 80, 136, Color.WHITE) }, 65),

	PK_FIS04("F#04;", new Key[] { new Key(305, 1, 40, 136, Color.BLACK) }, 66),

	PK_G04("G04;", new Key[] { new Key(345, 1, 40, 136, Color.WHITE), new Key(325, 137, 80, 136, Color.WHITE) }, 67),

	PK_GIS04("G#04;", new Key[] { new Key(386, 1, 40, 136, Color.BLACK) }, 68),

	PK_A04("A04;", new Key[] { new Key(426, 1, 40, 136, Color.WHITE), new Key(406, 137, 80, 136, Color.WHITE) }, 69),

	PK_AIS04("A#04;", new Key[] { new Key(467, 1, 40, 136, Color.BLACK) }, 70),

	PK_H04("H04;", new Key[] { new Key(507, 1, 60, 136, Color.WHITE), new Key(487, 137, 80, 136, Color.WHITE) }, 71),

	PK_C05("C05;", new Key[] { new Key(568, 1, 80, 272, Color.WHITE) }, 72),

	;

	private final String tone; // TODO future

	private final Key[] keys;

	private final int midiNum;

	private PianoKeyEnum(final String tone, final Key[] keys, final int midiNum) {
		this.tone = tone;
		this.keys = keys;
		this.midiNum = midiNum;
	}

	public String getTone() {
		return tone;
	}

	public Key[] getKeys() {
		return keys;
	}

	public int getMidiNum() {
		return midiNum;
	}

	//

	public static PianoKeyEnum findByMidiNum(int midiNum) {
		PianoKeyEnum pianoKeyEnum = null;
		if (midiNum > -1) {
			for (PianoKeyEnum value : PianoKeyEnum.values()) {
				if (midiNum == value.getMidiNum()) {
					pianoKeyEnum = value;
					break;
				}
			}
		}
		return pianoKeyEnum;
	}
}
