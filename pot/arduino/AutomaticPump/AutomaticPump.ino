#include <Wire.h>
#include <Adafruit_MotorShield.h>
#include "utility/Adafruit_MS_PWMServoDriver.h"


Adafruit_MotorShield AFMS = Adafruit_MotorShield(); 
Adafruit_DCMotor *myMotor = AFMS.getMotor(1);

int watertime = 1; // how long to water in seconds
int waittime = 1; // how long to wait between watering, in minutes
int val = 0; //value for storing moisture value 
int soilPin = A0;//Declare a variable for the soil moisture sensor 
int soilPower = 7;//Variable for Soil moisture Power


void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);   // open serial over USB

  pinMode(soilPower, OUTPUT);//Set D7 as an OUTPUT
  digitalWrite(soilPower, LOW);
  AFMS.begin();
  myMotor->setSpeed(255);
}

void loop() {
  // put your main code here, to run repeatedly:
  Serial.print("Soil Moisture = ");    
//get soil moisture value from the function below and print it
  Serial.println(readSoil());
  if(readSoil()>500){
  myMotor->run(FORWARD);
  delay(watertime*1000);        // multiply by 1000 to translate seconds to milliseconds

  myMotor->run(RELEASE);
  delay(waittime*5000);
  }
  delay(1000);//take a reading every second
  
}

int readSoil()
{

    digitalWrite(soilPower, HIGH);//turn D7 "On"
    delay(10);//wait 10 milliseconds 
    val = analogRead(soilPin);//Read the SIG value form sensor 
    digitalWrite(soilPower, LOW);//turn D7 "Off"
    return val;//send current moisture value
}
