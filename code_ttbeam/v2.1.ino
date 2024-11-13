#include <Arduino.h>
#define __DEBUG__
#include <SPI.h>
#include <Wire.h>
#include <Adafruit_GFX.h>
#include <Adafruit_SSD1306.h>
#include "esp_attr.h"
// Definir constantes
#define ANCHO_PANTALLA 128 // ancho pantalla OLED
#define ALTO_PANTALLA 64 // alto pantalla OLED
Adafruit_SSD1306 display(ANCHO_PANTALLA, ALTO_PANTALLA, &Wire, -1);

//GPS
#include <TinyGPS++.h>
#include <axp20x.h>
TinyGPSPlus gps;
HardwareSerial GPS(1);
AXP20X_Class axp;
//END GPS
int c=0;

//DHT22 sensor de Humedad y Temperatura
#include "DHT.h"
#define DHTPIN 25     // Digital pin connected to the DHT sensor
#define DHTTYPE DHT22   // DHT 22  (AM2302), AM2321
DHT dht(DHTPIN, DHTTYPE);
//GPS
#include <SoftwareSerial.h>
SoftwareSerial pmsSerial(13, 2);

//WiFi y Database
#include <WiFi.h>                  // Use this for WiFi instead of Ethernet.h
#include <MySQL_Connection.h>
#include <MySQL_Cursor.h>
byte mac_addr[] = { 0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED };
IPAddress server_addr(54,39,75,7); // IP of the MySQL *server* here
char user[20] = "u3kev6csezmozw4w";              // MySQL user login username
char password[30] = "S4oZ3woGlgnPwh8ATAZD";        // MySQL user login password

const char* ssid     = "JEEP21";  //nombre de red
const char* pass = "1090450742";  //clave de red
WiFiClient client;            // Use this for WiFi instead of EthernetClient
MySQL_Connection conn((Client *)&client);

//save data flash
#include "FS.h"
#include "SPIFFS.h"
#define FORMAT_SPIFFS_IF_FAILED true
// para desconcatenar
String str = "";
const char separator = ';';
const char separator3 = ',';
const int dataLength = 8;
int retorno=0, retraso=10;
int unico=1;

int timeStamp=millis();
//+100000;


void setup() {
  byte* psdRamBuffer = (byte*)ps_malloc(500000);
  
  free(psdRamBuffer);
  // put your setup code here, to run once:
  Serial.begin(115200);
  
  
  delay(5000);
  Wire.begin(21, 22);
  if (!axp.begin(Wire, AXP192_SLAVE_ADDRESS)) {
        Serial.println("AXP192 Begin PASS");
    } else { Serial.println("AXP192 Begin FAIL"); }
  axp.setPowerOutPut(AXP192_LDO2, AXP202_ON);
  axp.setPowerOutPut(AXP192_LDO3, AXP202_ON);
  axp.setPowerOutPut(AXP192_DCDC2, AXP202_ON);
  axp.setPowerOutPut(AXP192_EXTEN, AXP202_ON);
  axp.setPowerOutPut(AXP192_DCDC1, AXP202_ON);
  GPS.begin(9600, SERIAL_8N1, 34, 12);   //17-TX 18-RX
    //GPS
  #ifdef __DEBUG__
    delay(100);
    Serial.println("Iniciando pantalla OLED");
  #endif
  WiFi.begin(ssid, pass);
Serial.println(F("DHTxx test!"));
  
  dht.begin();
  
  
 
    // Iniciar pantalla OLED en la dirección 0x3C
  if (!display.begin(SSD1306_SWITCHCAPVCC, 0x3C)) {
    #ifdef __DEBUG__
        Serial.println("No se encuentra la pantalla OLED");
    #endif
      while (true);
    }
    // DISPLAY__________________________________
  display.clearDisplay();
  display.setTextSize(1);
  display.setTextColor(SSD1306_WHITE);
  pantalla("¡¡Hola mundo!!", 10, 32);
  Serial.println("inicie pantalla");   
    // DISPLAY__________________________________
  //save data
//  if(!SPIFFS.begin(FORMAT_SPIFFS_IF_FAILED)){
//    Serial.println("SPIFFS Mount Failed");
//    return;
//  }
  SPIFFS.begin();
  //delay(6000);
  display.clearDisplay();
  //writeFile(SPIFFS, "/f.txt", "");
  //deleteFile(SPIFFS, "/2021-1-2.txt");
  //deleteFile(SPIFFS, "/2021-1-5.txt");
  
  Serial.println("inicie pantalla"); 
  delay(2000);
}

String pm2="";
String pm10="";   

float H,T,f;
String hum, tem;
String Fecha;
String Hora;


String datee;
char nombress[25];
char* formato2="%i/%i/%i, %s,%f,%f, %i, %i, %.6f, %.6f,;";




struct pms5003data {
  uint16_t framelen;
  uint16_t pm10_standard, pm25_standard, pm100_standard;
  uint16_t pm10_env, pm25_env, pm100_env;
  uint16_t particles_03um, particles_05um, particles_10um, particles_25um, particles_50um, particles_100um;
  uint16_t unused;
  uint16_t checksum;
};
struct pms5003data data;
 String tablasLocales[60]={};
 boolean comienzo= true;
int q=0;
boolean ll=false;
int wifi;
void loop(){
  wifi=WiFi.status();
while (WiFi.status() == WL_CONNECTED and timeStamp<=millis() and ll==false) {
  display.clearDisplay();
  pantalla("WIFI\n upload to dataBase",10,0);
  conn.close();
  if (conn.connect(server_addr, 3306, user, password) ) {
     listDir(SPIFFS, "/", 0,"hola", true); //compara las tablas y crea las necesarias  
     for(int w=0;w<q;w++){
      display.clearDisplay();
      //Serial.print("\for: " +tablasLocales[w]);
        //Serial.println("");
        int numRows=numRow(tablasLocales[w]);
        //Serial.print("moriirrrrr" +tablasLocales[w]);
        String name3= tablasLocales[w]; 
        name3.replace(".txt","");
        name3.replace("/","");
        pantalla("archivo:"+name3,0,20);
        veTablas(name3, numRows);
    }     
 }
 conn.close();
 delay(10000);
 Serial.println("reiniciar");
  WiFi.begin(ssid, pass);
timeStamp=millis()+100000;    
} 
//listDir(SPIFFS, "/", 0,"hola", true);


 almacenamiento_de_medidas(); 
}

void almacenamiento_de_medidas(){
  Serial.println("reiniciar");
  pmsSerial.begin(9600);
  pmsSerial.listen();
  while(!readPMSdata(&pmsSerial)){
    pm2=String(data.particles_25um);
    pm10=String(data.particles_100um);
    //Serial.print(".");
    }
  pmsSerial.end();

  H=dht.readHumidity();T=dht.readTemperature();f = dht.readTemperature(true);  
  if (isnan(H) || isnan(T) || isnan(f)) {
      Serial.println(F("Failed to read from DHT sensor!"));
      return;
    }
  smartDelay(500);
  hum= String(H); tem= String(T);
  
  if(gps.location.lat() !=0 and H !=0){
     Fecha =String(gps.date.year())+"-"+String(gps.date.month())+"-"+String(gps.date.day()+1);
     String Fecha2 =String(gps.date.year())+"/"+String(gps.date.month())+"/"+String(gps.date.day());
     Hora= String(gps.time.hour()-5)+":"+String(gps.time.minute())+":"+String(gps.time.second());
     String latitud=String(gps.location.lat(),5);
     String longitud=String(gps.location.lng(),5);
     String wifiStatus="Wifi";
     if (wifi == WL_CONNECTED){
       wifiStatus="Wifi";
     }else { wifiStatus="....";
     WiFi.begin(ssid, pass);}
     String vector[]={Fecha, Hora+"\n", "H:"+hum+" %" , "T:"+tem+" C \n","PM2.5: "+ pm2+" ug/m3 \n","PM10.0: "+pm10+" ug/m3 \n", "loc:"+latitud, ","+longitud+"\n", "conteo: "+String(c)+"\n ",wifiStatus};
     c++;
     Serial.println("----"+String(WL_CONNECTED)+"---");
     display.clearDisplay();
     pantallavector(vector,9,0,0);
     //display.clearDisplay();
     datee = "/"+Fecha+".txt";
     datee.toCharArray(nombress, 25);
     Serial.println("transformado.........................");
     String informacion= Fecha2+","+Hora+","+hum+","+tem +","+ pm2+","+pm10+","+latitud+","+longitud+",";
     Serial.println(informacion);
     //delay(4000);
     writefile(nombress,informacion);
     //appendFile(SPIFFS, nombress, informacion);
     Serial.println(retorno);
     retorno++;
     
  }else{
     Serial.println("esperando conexion del gps .........."); 
  }
  
}

static void smartDelay(unsigned long ms){ 
  unsigned long start = millis();
  do {
    while (GPS.available())
      gps.encode(GPS.read());
  } while (millis() - start < ms);
}
