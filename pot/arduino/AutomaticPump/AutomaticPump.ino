#include <Wire.h>
#include <Adafruit_MotorShield.h>
#include "utility/Adafruit_MS_PWMServoDriver.h"


int motorPin = D6;

void setup() {
  // put your setup code here, to run once:
  Serial.begin(115200);   // open serial over USB  
  Serial.println("Before");
  pinMode(motorPin, OUTPUT);
}

void loop() {
  Serial.println("Loop");
  digitalWrite(motorPin, LOW);

  delay(10000);

  digitalWrite(motorPin, HIGH);

  delay(5000);
  
}
