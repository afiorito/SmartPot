#include <ESP8266HTTPClient.h>
#include <ESP8266WiFi.h>
#include <Wire.h>
#include <Adafruit_MotorShield.h>
#include "utility/Adafruit_MS_PWMServoDriver.h"


Adafruit_MotorShield AFMS = Adafruit_MotorShield(); 
Adafruit_DCMotor *myMotor = AFMS.getMotor(1);

int watertime = 1; // how long to water in seconds
int waittime = 1; // how long to wait between watering, in minutes
int val = 0; //value for storing moisture value 
int soilPin = A0;//Declare a variable for the soil moisture sensor 
int soilPower = D7;//Variable for Soil moisture Power
 
void setup() {                             //Serial connection
  Serial.begin(115200);   // open serial over USB
  WiFi.begin("Wang Family Network", "hero6381");   //WiFi connection
 
  while (WiFi.status() != WL_CONNECTED) {  //Wait for the WiFI connection completion
 
    delay(500);
    Serial.println("Waiting for connection");
 
  }

  pinMode(soilPower, OUTPUT);//Set D7 as an OUTPUT
  digitalWrite(soilPower, LOW);    
 
}
 
void loop() {


 if(WiFi.status()== WL_CONNECTED){   //Check WiFi connection status
 Serial.print("Soil Moisture = ");    
//get soil moisture value from the function below and print it
  String moistureLevel = String(readSoil());
  Serial.println(moistureLevel);

   HTTPClient http;    //Declare object of class HTTPClient
 
   http.begin("https://qrawi86kkd.execute-api.us-east-1.amazonaws.com/prod/smartpot/pot1/moisture", "65 39 E1 1C AE 10 AF A4 DB EC 8E 7E A3 D4 68 E7 DC 36 F5 70");      //Specify request destination
   http.addHeader("Content-Type", "application/json");  //Specify content-type header
 
   int httpCode = http.PUT("{\"moisture\":" + moistureLevel + "}");   //Send the request
   String payload = http.getString();                  //Get the response payload
 
   Serial.println(httpCode);   //Print HTTP return code
   Serial.println(payload);    //Print request response payload
 
   http.end();  //Close connection
 }else{
    Serial.println("Error in WiFi connection");   
 }
  delay(30000);  //Send a request every 30 seconds
}

int readSoil()
{

    digitalWrite(soilPower, HIGH);//turn D7 "On"
    delay(10);//wait 10 milliseconds 
    val = analogRead(soilPin);//Read the SIG value form sensor 
    digitalWrite(soilPower, LOW);//turn D7 "Off"
    return val;//send current moisture value
}
