void setup() 
{
  Serial.begin(115200);   // open serial over USB

  setupMoistureSensor();
}

void loop() {
Serial.print("Soil Moisture = ");    
//get soil moisture value from the function below and print it
int moisture = readMoistureValue();
Serial.println(moisture);

delay(5000);//take a reading every second
}


int moistureSensorSignal = A0;
int moistureSensorPower = D7;
void setupMoistureSensor() {
  pinMode(moistureSensorPower, OUTPUT);
  // set low so no power is flowing through the sensor
  digitalWrite(moistureSensorPower, HIGH);
}
int readMoistureValue() {
    int moisture = analogRead(moistureSensorSignal); 

    return moisture;
}
