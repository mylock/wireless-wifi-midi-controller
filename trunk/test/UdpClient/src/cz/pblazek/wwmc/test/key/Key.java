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

import javax.swing.JPanel;

/**
 * @author rtep.kezalb@gmail.com
 * 
 */
public class Key extends JPanel {

	private static final long serialVersionUID = 4187744341564811805L;

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
