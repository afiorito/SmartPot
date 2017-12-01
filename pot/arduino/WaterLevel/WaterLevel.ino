#include "Arduino.h"

void setup() {
  Serial.begin(115200);
  setupWaterLevelSensor();
}

void loop() {
  int waterLevel = readWaterLevel();

  Serial.print("Water level: ");
  Serial.println(waterLevel);

  delay(5000);
}

int mux = D7;
int waterLevelSensorSignal = A0;
int waterLevelPower = D8;
void setupWaterLevelSensor() {
  pinMode(waterLevelPower, OUTPUT);
  pinMode(mux, OUTPUT);
  digitalWrite(mux, HIGH);
  pinMode(waterLevelSensorSignal, INPUT);
  digitalWrite(waterLevelPower, HIGH);
}
int readWaterLevel() {
  int waterLevel = analogRead(waterLevelSensorSignal);

  return waterLevel;
}

