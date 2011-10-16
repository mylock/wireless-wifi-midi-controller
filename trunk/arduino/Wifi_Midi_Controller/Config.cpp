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

#include "Config.h"

// the GPIO pins on the SPI UART that are connected to WiFly module pins
#define BIT_PIO9 0b00000001 // hardware factory reset + auto-adhoc
#define BIT_RESET 0b00000010 // hardware reboot

const char SSID [] = "sC";
const char AUTH_MODE [] = "4";
const char AUTH_PHRASE [] = "focus###";
const char JOIN_POLICY [] = "1";
const char IP_PROTOCOL [] = "1";
const char IP_LOCAL_PORT [] = "8888";

Config::Config() {
  Serial.begin(9600);
  Serial.println("");
  Serial.println("Wireless WiFi MIDI Controller");
  Serial.println("-----------------------------"); 

  SpiSerial.begin();
  Serial.println("... connected to SPI UART");
}

// public

void Config::hardwareReset() {
  Serial.println("... setting GPIO direction");
  SpiSerial.ioSetDirection(BIT_RESET | BIT_PIO9);

  Serial.println("... first reboot");
  reboot(BIT_PIO9);
  readResponse(1000);

  Serial.println("... toggling pin to trigger factory reset");
  byte state = 0;
  for (int i = 0; i < 5; i++) {
    SpiSerial.ioSetState(BIT_RESET | state);  
    state = !state;
    readResponse(1500);
  }

  Serial.println("... second reboot");
  reboot();
  readResponse(1000);
}

void Config::setupWiFly() {
  WiFly.begin();
  Serial.println("... entering command mode");
  if (WiFly.enterCommandMode(true)) {
    readResponse(1000);

    Serial.print("... setting wlan ssid to: ");
    Serial.println(SSID);
    WiFly.sendCommand(completeCommand("set wlan ssid", SSID), false, "AOK");

    Serial.print("... setting wlan authentication mode to: ");
    Serial.println(AUTH_MODE);
    WiFly.sendCommand(completeCommand("set wlan auth", AUTH_MODE), false, "AOK");

    Serial.print("... setting wlan passphrase to: ");
    Serial.println(AUTH_PHRASE);
    WiFly.sendCommand(completeCommand("set wlan phrase", AUTH_PHRASE), false, "AOK");

    Serial.print("... setting wlan join policy to: ");
    Serial.println(JOIN_POLICY);
    WiFly.sendCommand(completeCommand("set wlan join", JOIN_POLICY), false, "AOK");

    Serial.print("... setting ip protocol to: ");
    Serial.println(IP_PROTOCOL);
    WiFly.sendCommand(completeCommand("set ip proto", IP_PROTOCOL), false, "AOK");

    Serial.print("... setting ip local port to: ");
    Serial.println(IP_LOCAL_PORT);
    WiFly.sendCommand(completeCommand("set ip localport", IP_LOCAL_PORT), false, "AOK");

    Serial.println("... saving configuration");
    WiFly.sendCommand("save", false, "Storing in config");

    SpiSerial.println("show net");
    readResponse(1000);
    SpiSerial.println("get uart");
    readResponse(1000);

    Serial.println("... final reboot");
    reboot();
    readResponse(1000);

    Serial.print("... ip: ");
    Serial.println(WiFly.ip());
  }
}

// private

void Config::reboot(const byte pio9State) {
  SpiSerial.ioSetState( (BIT_RESET & ~BIT_RESET) | pio9State);
  delay(1);
  SpiSerial.ioSetState(BIT_RESET | pio9State);
}

void Config::readResponse(const int timeOut) {
  int target = millis() + timeOut;
  while((millis() < target) || SpiSerial.available() > 0) {
    if (SpiSerial.available()) {
      Serial.print(SpiSerial.read(), BYTE);
    }
  }   
}

char *Config::completeCommand(const char command[], const char value[]) {
  char fullCommand[100];
  fullCommand[0] = '\0';
  strcat(fullCommand, command);
  strcat(fullCommand, " ");
  strcat(fullCommand, value);
  return fullCommand;
}

