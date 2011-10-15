/*
 * Copyright (C) 2011 The Wireless WiFi MIDI Controller Open Source Project
 *
 * Licensed under the GNU General Public License, Version 3
 *
 * Author: Petr Blazek
 */

#ifndef CONFIG_H_
#define CONFIG_H_

class Config {
public:
  Config();
  void hardwareReset();
  void setupWiFly();
private:
  void reboot(byte pio9State = 0);
  void readResponse(int timeOut = 0);
  char *completeCommand(const char *, const char *);
};

#endif /* CONFIG_H_ */
