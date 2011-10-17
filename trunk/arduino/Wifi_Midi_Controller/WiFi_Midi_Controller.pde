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
#include "pt.h" // Protothreads library: http://www.sics.se/~adam/pt/
#include "Config.h"

#define DATA_LEN 4
#define DATA_TYPE_DIFFERENCE 500
#define MIDI_CHANNEL_INIT 4
#define MIDI_CHANNEL_NOTE 1
#define MIDI_VELOCITY_NOTE_ON 127
#define MIDI_VELOCITY_NOTE_OFF 0

static int protothread1_flag, protothread2_flag;
static struct pt pt1, pt2;

char data[DATA_LEN];
int index = 0;

void setup() {
  Config config = Config();
  //  config.hardwareReset(); // TODO uncomment
  //  config.setupWiFly(); // TODO uncomment

  PT_INIT(&pt1);
  PT_INIT(&pt2);

  MIDI.begin(MIDI_CHANNEL_INIT);
}

void loop() {
  protothread1(&pt1);
  protothread2(&pt2);
}

void sendSimpleNote() {
  while(SpiSerial.available() > 0) {
    data[index] = SpiSerial.read();
    if (index == (DATA_LEN - 1)) {
      int midiNum = (data[0] << 24) + ((data[1] & 0xff) << 16) + ((data[2] & 0xff) << 8) + (data[3] & 0xff);

      if ((midiNum - DATA_TYPE_DIFFERENCE) < 0) {
        MIDI.sendNoteOn(midiNum, MIDI_VELOCITY_NOTE_ON, MIDI_CHANNEL_NOTE);
      } 
      else {
        MIDI.sendNoteOff((midiNum - DATA_TYPE_DIFFERENCE), MIDI_VELOCITY_NOTE_OFF, MIDI_CHANNEL_NOTE);
      }

      index = 0;
      continue;
    }
    index++;
  }
}

// protothreads

static int protothread1(struct pt *pt) {
  PT_BEGIN(pt);
  while(1) {
    PT_WAIT_UNTIL(pt, protothread2_flag != 0);

    sendSimpleNote();

    protothread2_flag = 0;
    protothread1_flag = 1;
  }
  PT_END(pt);
}

static int protothread2(struct pt *pt) {
  PT_BEGIN(pt);
  while(1) {
    protothread2_flag = 1;
    PT_WAIT_UNTIL(pt, protothread1_flag != 0);

    sendSimpleNote();

    protothread1_flag = 0;
  }
  PT_END(pt);
}

