#include <ESP8266HTTPClient.h>
#include <ESP8266WiFi.h>
 
void setup() {
 
  Serial.begin(115200);                                  //Serial connection
  WiFi.begin("Wang Family Network", "hero6381");   //WiFi connection
 
  while (WiFi.status() != WL_CONNECTED) {  //Wait for the WiFI connection completion
 
    delay(500);
    Serial.println("Waiting for connection");
 
  }
 
}
 
void loop() {
 
 if(WiFi.status()== WL_CONNECTED){   //Check WiFi connection status
 
   HTTPClient http;    //Declare object of class HTTPClient
 
   http.begin("https://qrawi86kkd.execute-api.us-east-1.amazonaws.com/prod/smartpot/pot1/moisture", "65 39 E1 1C AE 10 AF A4 DB EC 8E 7E A3 D4 68 E7 DC 36 F5 70");      //Specify request destination
   http.addHeader("Content-Type", "application/json");  //Specify content-type header
 
   int httpCode = http.PUT("{\"moisture\":5}");   //Send the request
   String payload = http.getString();                  //Get the response payload
 
   Serial.println(httpCode);   //Print HTTP return code
   Serial.println(payload);    //Print request response payload
 
   http.end();  //Close connection
 
 }else{
 
    Serial.println("Error in WiFi connection");   
 
 }
 
  delay(30000);  //Send a request every 30 seconds
 
}
