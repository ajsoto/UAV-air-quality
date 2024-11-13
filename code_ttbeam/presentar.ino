void pantalla(String texto, int ubicacion1, int ubicacion2){
        display.setCursor(ubicacion1, ubicacion2);
        display.println(texto);
        display.display();
}
void pantallavector(String texto[], int len,int ubicacion1, int ubicacion2){
  display.setCursor(ubicacion1, ubicacion2);
  for (int i; i<=len;i++){
        display.print(texto[i]);
          
  }
  display.display();
}

void mostrarUbicacion(String Fecha, String Hora){
  Serial.printf("Latitude  : %.5f ",gps.location.lat());
    Serial.printf("Longitude : %.5f \n",gps.location.lng());
    Serial.printf("Satellites: %f \n", gps.satellites.value());
    Serial.printf("Altitude  : %f M \n \n",gps.altitude.feet() / 3.2808);
    Serial.print(Fecha+"\n"+ Hora+"\n");
    Serial.printf("Speed: %.3f",gps.speed.kmph());
    Serial.println("**********************");
}
void mostrarHyT(float H, float T){
    Serial.printf("Humidity: %.1f",H);
    Serial.printf(" Temperature: %.1f",T);
    Serial.println(F("°C "));
}
void mostrarPM(){
  Serial.println();
    Serial.println("---------------------------------------");
    Serial.println("Concentration Units (standard)");
    Serial.print("PM 1.0: "); Serial.print(data.pm10_standard);
    Serial.print("\t\tPM 2.5: "); Serial.print(data.pm25_standard);
    Serial.print("\t\tPM 10: "); Serial.println(data.pm100_standard);
    Serial.println("---------------------------------------");
    Serial.println("Concentration Units (environmental)");
    Serial.print("PM 1.0: "); Serial.print(data.pm10_env);
    Serial.print("\t\tPM 2.5: "); Serial.print(data.pm25_env);
    Serial.print("\t\tPM 10: "); Serial.println(data.pm100_env);
    Serial.println("---------------------------------------");
    Serial.print("Particles > 0.3um / 0.1L air:"); Serial.println(data.particles_03um);
    Serial.print("Particles > 0.5um / 0.1L air:"); Serial.println(data.particles_05um);
    Serial.print("Particles > 1.0um / 0.1L air:"); Serial.println(data.particles_10um);
    Serial.print("Particles > 2.5um / 0.1L air:"); Serial.println(data.particles_25um);
    Serial.print("Particles > 5.0um / 0.1L air:"); Serial.println(data.particles_50um);
    Serial.print("Particles > 10.0 um / 0.1L air:"); Serial.println(data.particles_100um);
    Serial.println("---------------------------------------");
}
