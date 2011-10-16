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

#include "WiFly.h" // experimental WiFly libs: https://github.com/sparkfun/WiFly-Shield

#ifndef CONFIG_H_
#define CONFIG_H_

class Config {
public:
  Config();
  void hardwareReset();
  void setupWiFly();
private:
  void reboot(const byte pio9State = 0);
  void readResponse(const int timeOut = 0);
  char *completeCommand(const char *, const char *);
};

#endif /* CONFIG_H_ */

