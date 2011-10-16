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
 * 
 * requires:
 *   - Arduino board (ADK, Mega, ...)
 *   - SparkFun Electronics WiFly Shield
 *   - SparkFun Electronics MIDI Shield
 */

#include "WiFly.h" // experimental WiFly library: https://github.com/sparkfun/WiFly-Shield
#include "MIDI.h" // Arduino MIDI Library: http://sourceforge.net/projects/arduinomidilib/
#include "Config.h"

char data[4];
int index = 0;

void setup() {
  Config config = Config();
  //  config.hardwareReset(); // TODO uncomment
  //  config.setupWiFly(); // TODO uncomment

  MIDI.begin(4);
}

void loop() {
  while(SpiSerial.available() > 0) {
    data[index] = SpiSerial.read();
    if (index == 3) {
      int midiNum = (data[0] << 24) + ((data[1] & 0xff) << 16) + ((data[2] & 0xff) << 8) + (data[3] & 0xff);

      Serial.println(midiNum);

      MIDI.sendNoteOn(midiNum, 127, 1);
      delay(500);
      MIDI.sendNoteOff(midiNum, 0, 1);

      index = 0;
      continue;
    }
    index++;
  }
}

