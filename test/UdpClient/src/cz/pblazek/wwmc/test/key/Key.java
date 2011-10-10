/*
 * Copyright (C) 2011 The Wireless WiFi MIDI Controller Open Source Project
 * 
 * Licensed under the GNU General Public License, Version 3
 */
package cz.pblazek.wwmc.test.key;

import java.awt.Color;

import javax.swing.JPanel;

/**
 * @author rtep.kezalb@gmail.com
 * 
 */
public class Key extends JPanel {

	private static final long serialVersionUID = 4187744341564811805L;

	public static final Color KEY_COLOR_WHITE = new Color(255, 255, 255);

	public static final Color KEY_COLOR_BLACK = new Color(0, 0, 0);

	public static final Color KEY_COLOR_BACK = new Color(255, 255, 100);

	private final Color color;

	public Key(int x, int y, int width, int height, final Color color) {
		super();
		this.color = color;
		this.setBounds(x, y, width, height);
		this.setBackground(this.color);
	}

	public Color getColor() {
		return color;
	}

}
