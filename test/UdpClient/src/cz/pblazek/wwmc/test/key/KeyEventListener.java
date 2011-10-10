/*
 * Copyright (C) 2011 The Wireless WiFi MIDI Controller Open Source Project
 * 
 * Licensed under the GNU General Public License, Version 3
 */
package cz.pblazek.wwmc.test.key;

import java.util.EventListener;

/**
 * @author rtep.kezalb@gmail.com
 * 
 */
public interface KeyEventListener extends EventListener {

	public void keyReceived(KeyEvent event);

}
