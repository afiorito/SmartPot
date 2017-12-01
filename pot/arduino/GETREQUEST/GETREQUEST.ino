#include <Wire.h>
#include <UnoWiFiDevEd.h>

#define CONNECTOR     "rest" 
#define SERVER_ADDR   "jsonplaceholder.typicode.com"


void setup() {

  Serial.begin(9600);
  Ciao.begin(); // CIAO INIT
}

void loop() {
      String request = "/albums/1";

      Ciao.println("Read json data"); 
      CiaoData data = Ciao.read(CONNECTOR, SERVER_ADDR, request, "GET");

      String state = data.get(1);
      String response = data.get(2);

      if (!data.isEmpty()){
          Ciao.println( "State: " + String (data.get(1)) );
          Ciao.println( "Response: " + String (data.get(2)) );
          Serial.println( "State: " + String (data.get(1)) );
          Serial.println( "Response: " + String (data.get(2)) );
        }
        else{ 
          Ciao.println ("Read Error");
          Serial.println ("Write Error");
        }

  delay(5000);
} 
