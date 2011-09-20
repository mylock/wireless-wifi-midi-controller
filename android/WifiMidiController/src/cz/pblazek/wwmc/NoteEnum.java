/*
 * Copyright (C) 2011 The Wireless WiFi MIDI Controller Open Source Project
 * 
 * Licensed under the GNU General Public License, Version 3
 */
package cz.pblazek.wwmc;

/**
 * @author rtep.kezalb@gmail.com
 * 
 */
public enum NoteEnum {

	N_C("C"),

	N_CIS("C#"),

	N_D("D"),

	N_DIS("D#"),

	N_E("E"),

	N_EIS("E#"),

	N_F("F"),

	N_FIS("F#"),

	N_G("G"),

	N_GIS("G#"),

	N_A("A"),

	N_AIS("A#"),

	N_H("H"),

	N_HIS("H#"),

	;

	private final String tone;

	private NoteEnum(final String tone) {
		this.tone = tone;
	}

	public String getTone() {
		return tone;
	}

}
