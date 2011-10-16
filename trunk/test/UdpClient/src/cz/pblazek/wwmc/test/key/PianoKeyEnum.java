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

	PK_C05("C05;", new Key[] { new Key(1, 1, 60, 136, Color.WHITE), new Key(1, 137, 80, 136, Color.WHITE) }, 72),

	PK_CIS05("C#05;", new Key[] { new Key(62, 1, 40, 136, Color.BLACK) }, 73),

	PK_D05("D05;", new Key[] { new Key(102, 1, 40, 136, Color.WHITE), new Key(82, 137, 80, 136, Color.WHITE) }, 74),

	PK_DIS05("D#05;", new Key[] { new Key(143, 1, 40, 136, Color.BLACK) }, 75),

	PK_E05("E05;", new Key[] { new Key(183, 1, 60, 136, Color.WHITE), new Key(163, 137, 80, 136, Color.WHITE) }, 76),

	PK_F05("F05;", new Key[] { new Key(244, 1, 60, 136, Color.WHITE), new Key(244, 137, 80, 136, Color.WHITE) }, 77),

	PK_FIS05("F#05;", new Key[] { new Key(305, 1, 40, 136, Color.BLACK) }, 78),

	PK_G05("G05;", new Key[] { new Key(345, 1, 40, 136, Color.WHITE), new Key(325, 137, 80, 136, Color.WHITE) }, 79),

	PK_GIS05("G#05;", new Key[] { new Key(386, 1, 40, 136, Color.BLACK) }, 80),

	PK_A05("A05;", new Key[] { new Key(426, 1, 40, 136, Color.WHITE), new Key(406, 137, 80, 136, Color.WHITE) }, 81),

	PK_AIS05("A#05;", new Key[] { new Key(467, 1, 40, 136, Color.BLACK) }, 82),

	PK_H05("H05;", new Key[] { new Key(507, 1, 60, 136, Color.WHITE), new Key(487, 137, 80, 136, Color.WHITE) }, 83),

	PK_C06("C06;", new Key[] { new Key(568, 1, 80, 272, Color.WHITE) }, 84),

	;

	private final String tone; // TODO future

	private final Key[] key;

	private final int midiNum;

	private PianoKeyEnum(final String tone, final Key[] key, final int midiNum) {
		this.tone = tone;
		this.key = key;
		this.midiNum = midiNum;
	}

	public String getTone() {
		return tone;
	}

	public Key[] getKey() {
		return key;
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
