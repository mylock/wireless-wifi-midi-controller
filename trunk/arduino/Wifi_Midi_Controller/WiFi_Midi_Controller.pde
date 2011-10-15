/*
 * Copyright (C) 2011 The Wireless WiFi MIDI Controller Open Source Project
 * 
 * Licensed under the GNU General Public License, Version 3
 *
 * Author: Petr Blazek
 * 
 * requires:
 *   - Arduino board
 *   - SparkFun Electronics WiFly Shield
 *   - SparkFun Electronics MIDI Shield
 */

#include "WiFly.h"
#include "Config.h"

String note = "";

void setup() {
  Config config = Config();
  config.hardwareReset();
  config.setupWiFly();
}



void loop() {
  while(SpiSerial.available() > 0) {
    char frag = SpiSerial.read();
    note = note + frag;
    if (frag == ';') {
      Serial.println(note);

      note = "";
    }
  }
}
