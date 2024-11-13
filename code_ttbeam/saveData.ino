
void  listDir(fs::FS &fs, const char * dirname, uint8_t levels, char nombre[20], boolean nucleo){
    
    delay(100);
    File root = fs.open(dirname);
    Serial.printf("1.Listdir Listing directory: %s\r\n", dirname);
    q=0;
    if(!root){
        Serial.println("- failed to open directory");
        return;
    }
    if(!root.isDirectory()){
        Serial.println(" - not a directory");
        return;
    }
    
    File file = root.openNextFile();
   
    while(file){
        if(file.isDirectory()){
            Serial.print("  DIR : ");
            Serial.println(file.name());
            
        } else {
           tablasLocales[q]=file.name();
           q++;
            
            //Serial.println(String(nombre)  +"==="+ String(file.name()));
            Serial.print("  FILE1:: ");
            Serial.print(file.name());
            Serial.print("\tSIZE: ");
            Serial.println(file.size());
            //readFile(SPIFFS,file.name());     
        }
        file = root.openNextFile();
    }
    file.close(); 
}

String solo="";
int numRow(String path){
   int i=0;
    File f = SPIFFS.open(path, "r");
    if (!f) {
      Serial.println("Fallo apertura de archivo"); 
      return (0);
      }
      else{  
        while(f.available() and f.readStringUntil('\n') !="ds"){
          //solo=f.readStringUntil('\n');
          //solo="";
          i++;
            }  
        Serial.println("numRows "+ String(i));
      } 
  
   f.close();
   return(i);
}

void writefile(String path, String texto){
  File  file = SPIFFS.open(path, "a+");
  if (!file) {
    Serial.println("Fallo apertura de archivo"); 
    return;
  }
  //Serial.println("====== Escribiendo en el archivo SPIFFS =========");
  file.println(texto);
  file.close();
}


//void renameFile(fs::FS &fs, const char * path1, const char * path2){
//    Serial.printf("Renaming file %s to %s\r\n", path1, path2);
//    if (fs.rename(path1, path2)) {
//        Serial.println("- file renamed");
//    } else {
//        Serial.println("- rename failed");
//    }
//}

void deleteFile(fs::FS &fs, const char * path){
    Serial.printf("Deleting file: %s\r\n", path);
    if(fs.remove(path)){
        Serial.println("- file deleted");
    } else {
        Serial.println("- delete failed");
    }
}


String s;
String str2[8];
String s1;
void readFile(fs::FS &fs, String path, int num){
  String path1=path;
  path="/"+ path +".txt";
  Serial.println("newPatch:"+path);
  File file = SPIFFS.open(path);
   if (!file) {
  Serial.println("Fallo apertura de archivo");
  return; 
  }
  else{
    Serial.println("====== Leyendo del archivo SPIFFS ======="); 
    // write 10 strings to file
    int i=0;
    while(file.available()){
      i++;
      if(i>num){
        s=file.readStringUntil('\n');
        Serial.print(i);
        Serial.print(":");
        Serial.println(s); 
        int z=0;
        while (z<= 8){
          int index1 = s.indexOf(separator3);
          str2[z]=s.substring(0, index1);
          s = s.substring(index1 + 1);
          z++;     
          }
        transform(path1,str2);
      }else{ s1=file.readStringUntil('\n');}   
    }
  }
}

int contador=0;

void transform(String patch,String vector[8]){
  
  vector[0].replace("/","-");
  String fechaft = vector[0];
    float Ht=vector[2].toFloat();
    float Tt=vector[3].toFloat();
    int pmt2=vector[4].toInt();
    int  pmt10=vector[5].toInt();
    float latt=vector[6].toFloat();
    float longt=vector[7].toFloat();
    
  char* formato = "INSERT INTO `bg0ajs7w7p0xqozm0hek`.`%s` (`fecha`, `hora`, `H`, `T`, `PM2.5`, `PM10.0`, `LAT`, `LONG`) VALUES ('%s', '%s', '%f', '%f', '%i', '%i', '%.6f', '%.6f');";
  char* formato2="%s,%s, %s,%f,%f, %i, %i, %.6f, %.6f,;";
  char INSERT_SQL[800]=" ";
  //Serial.println("voy a transformar");
  sprintf(INSERT_SQL, formato, patch,fechaft,vector[1], Ht, Tt, pmt2, pmt10,latt, longt);
  //Serial.println("transformado.........................");
  //Serial.println(INSERT_SQL);
  Serial.println("Connecting..."+ String(WiFi.status()));
  MySQL_Cursor *cur_mem = new MySQL_Cursor(&conn);
  //if (conn.connect(server_addr, 3306, user, password)) {
    //Serial.print("CONEXION EXITOSA");
    cur_mem->execute(INSERT_SQL);
    //delete cur_mem;
    Serial.println(INSERT_SQL);
    //delay(1000);
    
//  } else{
//    Serial.println("Connection failed.");
//    conn.close(); 
//  }
}
