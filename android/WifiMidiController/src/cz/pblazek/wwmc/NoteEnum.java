/*
 * Copyright (C) 2011 The Wireless WiFi MIDI Controller Open Source Project
 * 
 * Licensed under the GNU General Public License, Version 3
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

	UNKNOWN("unknown", null),

	N_C00("C00;", new Rect[] { new Rect(0, 2, 69, 240), new Rect(0, 240, 99, 478) }),

	N_CIS00("C#00;", new Rect[] { new Rect(70, 1, 129, 239) }),

	N_D00("D00;", new Rect[] { new Rect(130, 2, 169, 240), new Rect(100, 240, 199, 478) }),

	N_DIS00("D#00;", new Rect[] { new Rect(170, 1, 229, 239) }),

	N_E00("E00;", new Rect[] { new Rect(230, 2, 299, 240), new Rect(200, 240, 299, 478) }),

	N_F00("F00;", new Rect[] { new Rect(300, 2, 369, 240), new Rect(300, 240, 399, 478) }),

	N_FIS00("F#00;", new Rect[] { new Rect(370, 1, 429, 239) }),

	N_G00("G00;", new Rect[] { new Rect(430, 2, 469, 240), new Rect(400, 240, 499, 478) }),

	N_GIS00("G#00;", new Rect[] { new Rect(470, 1, 529, 239) }),

	N_A00("A00;", new Rect[] { new Rect(530, 2, 569, 240), new Rect(500, 240, 599, 478) }),

	N_AIS00("A#00;", new Rect[] { new Rect(570, 1, 629, 239) }),

	N_H00("H00;", new Rect[] { new Rect(630, 2, 699, 240), new Rect(600, 240, 699, 478) }),

	N_C01("C01;", new Rect[] { new Rect(700, 2, 799, 478) }),

	;

	private final String tone;

	private final Rect[] keyRects;

	// private final int[] rgb; // TODO future

	private NoteEnum(final String tone, final Rect[] keyRects) {
		this.tone = tone;
		this.keyRects = keyRects;
	}

	public String getTone() {
		return tone;
	}

	public Rect[] getKeyRects() {
		return keyRects;
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
