#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <ArduinoJson.h>

#define ssid "Powerhouse"
#define pwd "a7b6e7c9d9"
#define secret "65 39 E1 1C AE 10 AF A4 DB EC 8E 7E A3 D4 68 E7 DC 36 F5 70"
#define cycle_time 5000
#define water_time 20000

/*
 * Moisture Range: 200 - 1000
 * Water Level Range: 65-90
 */

/**
 * GLOBAL VARIABLES
 */
int motorPin = D5;
int mux = D4;
int analogIN = A0;

/**
 * SETUP
 */
void setup() {
  Serial.begin(115200);

  pinMode(mux, OUTPUT);
  pinMode(motorPin, OUTPUT);
  digitalWrite(motorPin, LOW);  // make sure motor is off
  pinMode(analogIN, INPUT);

  setupNetwork();

  setupMoistureSensor();
  setupWaterLevelSensor();
}

/**
 * LOOP
 */
void loop() {
  if(WiFi.status() != WL_CONNECTED) {
    setupNetwork();
  }
  
  int potStatus = checkSmartPot();
  switch(potStatus) {
    case -1: 
      delay(cycle_time);
      return;
    case 1:
      water();
      setWatered();
      delay(cycle_time);
      return;
    default:
      break;
  }

  int waterLevel = readWaterLevel();
  Serial.println("Water Level: ");
  Serial.println(waterLevel);
  
  int moisture = readMoistureValue();
  Serial.println("Moisture: ");
  Serial.println(moisture);

  // If there's no water, no point turning the motor on
  //if(moisture < 100 && waterLevel > 5) {
    if(moisture < 300) {
    Serial.println("Watering...");
    water();
  }

  delay(cycle_time);
}

/**
 * Connect to WiFi network
 */
void setupNetwork() {
  WiFi.begin(ssid, pwd);

  Serial.print("Wating for connection...");

  while(WiFi.status() != WL_CONNECTED) {
    delay(1000);
    Serial.print(".");
  }
  Serial.println();
  Serial.println("Connected");
}

/**
 * Gets smartpot and checks the running & watering status 
 * return -1 if smartpot should stop running
 * return  0 if smartpot should run but not start watering
 * return  1 if smartpot should start watering
 */
int checkSmartPot() {
  HTTPClient http;
  http.begin("https://qrawi86kkd.execute-api.us-east-1.amazonaws.com/prod/smartpot/pot1", secret);

  int httpCode = http.GET();
  String payload = http.getString();

  StaticJsonBuffer<200> jsonBuffer;
  JsonObject& root = jsonBuffer.parseObject(payload);

  int shouldRun;
  int shouldWater;

  if (root.success()) {
    shouldRun = root["running"];
    shouldWater = root["watering"];
  } else {
    return -1;
  }

  Serial.println(httpCode);
  Serial.println(payload);

  http.end();

  if(shouldWater == 1) {
    return 1;
  }

  if(shouldRun == 0) {
    return -1;
  }

  return 0;
}

/**
 * Water the plant inside the pot
 */
void water() {
  // turn the motor on by turning on transistor
  digitalWrite(motorPin, HIGH);
  delay(water_time);
  digitalWrite(motorPin, LOW);
  updateLastWatered();
}

void setWatered() {
  HTTPClient http;
  http.begin("https://qrawi86kkd.execute-api.us-east-1.amazonaws.com/prod/smartpot/pot1/watering", secret);
  http.addHeader("Content-Type", "application/json");
  
  int httpCode = http.PUT("{\"watering\": 0}");
  String payload = http.getString();

  Serial.println(httpCode);
  Serial.println(payload);

  http.end();
}

// update the last watering time on AWS
void updateLastWatered() { 
  HTTPClient http;
  http.begin("https://qrawi86kkd.execute-api.us-east-1.amazonaws.com/prod/smartpot/pot1/watered", secret);
  http.addHeader("Content-Type", "application/json");
  
  int httpCode = http.PUT("");
  String payload = http.getString();

  Serial.println(httpCode);
  Serial.println(payload);

  http.end();
}

/**
 * START - MOISTURE SENSOR
 */
int moistureSensorPower = D6;
void setupMoistureSensor() {
  pinMode(moistureSensorPower, OUTPUT);
  // set low so no power is flowing through the sensor
  digitalWrite(moistureSensorPower, LOW);
}

// read the moisture from the moisture sensor
int readMoistureValue() {
  // select channel 0
  digitalWrite(moistureSensorPower, HIGH);
  digitalWrite(mux, LOW);
  delay(500);
  
  int moisture = analogRead(analogIN);
  sendMoisture(moisture);
  digitalWrite(moistureSensorPower, LOW);
  delay(500); 
    
  return moisture;
}

// send moisture to AWS
void sendMoisture(int moisture) {
  HTTPClient http;
  http.begin("https://qrawi86kkd.execute-api.us-east-1.amazonaws.com/prod/smartpot/pot1/moisture", secret);
  http.addHeader("Content-Type", "application/json");

  String request = "{\"moisture\":" + String(moisture) + "}";
  
  int httpCode = http.PUT(request);
  String payload = http.getString();

  Serial.println(httpCode);
  Serial.println(payload);

  http.end();
}
/**
 * END - MOISTURE SENSOR
 */

/**
 * START - WATER LEVEL SENSOR
 */
int waterLevelPower = D7;
void setupWaterLevelSensor() {
  pinMode(waterLevelPower, OUTPUT);
  // set low so no power is flowing through the sensor
  digitalWrite(waterLevelPower, LOW);
}

// read water level from the water level sensor
int readWaterLevel() {
  // select channel 1
  digitalWrite(waterLevelPower, HIGH);
  digitalWrite(mux, HIGH);
  delay(500);
  
  int waterLevel = analogRead(analogIN);
  sendWaterLevel(waterLevel);
  digitalWrite(waterLevelPower, LOW);
  delay(500);

  return waterLevel;
}

// send water level to AWS
void sendWaterLevel(int waterLevel) {
  HTTPClient http;
  http.begin("https://qrawi86kkd.execute-api.us-east-1.amazonaws.com/prod/smartpot/pot1/waterLevel", secret);
  http.addHeader("Content-Type", "application/json");

  String request = "{\"waterLevel\":" + String(waterLevel) + "}";
  
  int httpCode = http.PUT(request);
  String payload = http.getString();

  Serial.println(httpCode);
  Serial.println(payload);

  http.end();
}
/**
 * END - WATER LEVEL SENSOR
 */
