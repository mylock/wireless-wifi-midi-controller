/*
 * Copyright (C) 2011 The Wireless WiFi MIDI Controller Open Source Project
 * 
 * Licensed under the GNU General Public License, Version 3
 */
package cz.pblazek.wwmc.test.key;

import java.awt.Color;

/**
 * @author rtep.kezalb@gmail.com
 * 
 */
public enum PianoKeyEnum {

	PK_C00("C00", new Key[] { new Key(1, 1, 60, 136, Color.WHITE), new Key(1, 137, 80, 136, Color.WHITE) }),

	PK_CIS00("C#00", new Key[] { new Key(62, 1, 40, 136, Color.BLACK) }),

	PK_D00("D00", new Key[] { new Key(102, 1, 40, 136, Color.WHITE), new Key(82, 137, 80, 136, Color.WHITE) }),

	PK_DIS00("D#00", new Key[] { new Key(143, 1, 40, 136, Color.BLACK) }),

	PK_E00("E00", new Key[] { new Key(183, 1, 60, 136, Color.WHITE), new Key(163, 137, 80, 136, Color.WHITE) }),

	PK_F00("F00", new Key[] { new Key(244, 1, 60, 136, Color.WHITE), new Key(244, 137, 80, 136, Color.WHITE) }),

	PK_FIS00("F#00", new Key[] { new Key(305, 1, 40, 136, Color.BLACK) }),

	PK_G00("G00", new Key[] { new Key(345, 1, 40, 136, Color.WHITE), new Key(325, 137, 80, 136, Color.WHITE) }),

	PK_GIS00("G#00", new Key[] { new Key(386, 1, 40, 136, Color.BLACK) }),

	PK_A00("A00", new Key[] { new Key(426, 1, 40, 136, Color.WHITE), new Key(406, 137, 80, 136, Color.WHITE) }),

	PK_AIS00("A#00", new Key[] { new Key(467, 1, 40, 136, Color.BLACK) }),

	PK_H00("H00", new Key[] { new Key(507, 1, 60, 136, Color.WHITE), new Key(487, 137, 80, 136, Color.WHITE) }),

	PK_C01("C01", new Key[] { new Key(568, 1, 80, 272, Color.WHITE) }),

	;

	private final String tone;

	private final Key[] key;

	private PianoKeyEnum(final String tone, final Key[] key) {
		this.tone = tone;
		this.key = key;
	}

	public String getTone() {
		return tone;
	}

	public Key[] getKey() {
		return key;
	}

	//

	public static PianoKeyEnum findByTone(String tone) {
		PianoKeyEnum pianoKeyEnum = null;
		if (tone != null) {
			for (PianoKeyEnum value : PianoKeyEnum.values()) {
				if (tone.startsWith(value.getTone())) {
					pianoKeyEnum = value;
					break;
				}
			}
		}
		return pianoKeyEnum;
	}
}
