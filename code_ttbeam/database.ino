boolean estado1=false;
void  veTablas(String nameTable, int numRows){
  if (conn.connect(server_addr, 3306, user, password) and comienzo==true) {
  char query[55] = "show tables FROM bg0ajs7w7p0xqozm0hek;";
  long head_count = 0;
  delay(2000);
  Serial.println("\n 2. Vetablas. \n Running SELECT and printing results");
  MySQL_Cursor *cur_mem = new MySQL_Cursor(&conn);
  cur_mem->execute(query);
  column_names *cols = cur_mem->get_columns();
  for (int f = 0; f < cols->num_fields; f++) {
    Serial.print(cols->fields[f]->name);
    if (f < cols->num_fields-1) {
      Serial.print(", ");
    }
  }
  Serial.println();
  String tablas[60]={};
  int numT=0;
  // Read the rows and print them
  row_values *row = NULL;
  do {
    row = cur_mem->get_next_row();
    if (row != NULL) { 
      String tabla= row->values[0];
      tablas[numT]=tabla;
      numT+=1; 
    }
  } while (row != NULL);
  // Deleting the cursor also frees up memory used
  delete cur_mem;
  estado1=false;
  for (int f = 0; f < numT; f++) {
    
    if (tablas[f]==nameTable){
      estado1=true;
      int numRowDB=numRowsDB(tablas[f]);
      pantalla("local:"+String(numRows)+" DB:"+String(numRowDB),0,30);
      Serial.println("igual::"+tablas[f]+"=="+nameTable);
      Serial.println("DB:"+String(numRowDB)+ " local:"+String(numRows));
      delay(2000);
      if (numRows>numRowDB){
          Serial.println("voy a cargar la info a la BD");
          readFile(SPIFFS,nameTable, numRowDB);
          //llamo cargar inofrmacion a la base de datos
        }
      }else{
        //Serial.println("  "+tablas[f]+"::DIFERENTE DE::"+nameTable);
      }  
    } 

if(estado1!=true){
     Serial.println("es hora de crear==============");
        createTable(nameTable);
        readFile(SPIFFS,nameTable, 0);
        Serial.println("no  existe");
        estado1=true;
      } 
      else{
        Serial.println("la tabla ya existe==============");
        
      }
}else{conn.close();}

}
void createTable(String nameTable){
  String insertar = "CREATE TABLE  IF NOT EXISTS `bg0ajs7w7p0xqozm0hek`.`"+nameTable+"` (`id` int(11)  NOT NULL AUTO_INCREMENT ,`fecha` varchar(255) COLLATE utf8_bin NOT NULL,`hora` varchar(255) COLLATE utf8_bin NOT NULL,`H` FLOAT NOT NULL,`T` FLOAT NOT NULL,`PM2.5` int(11) NOT NULL,`PM10.0` int(11) NOT NULL,`LAT` FLOAT NOT NULL,`LONG` FLOAT NOT NULL,PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;";  
  char INSERT_SQL[800];
  insertar.toCharArray(INSERT_SQL, 800);
  Serial.println("Recording data."+ String(INSERT_SQL));
  // Initiate the query class instance
  MySQL_Cursor *cur_mem = new MySQL_Cursor(&conn);
  // Execute the query
  cur_mem->execute(INSERT_SQL);
  // Note: since there are no results, we do not need to read any data
  // Deleting the cursor also frees up memory used
  delete cur_mem;
}
int verAllDatos(String nametable){
  
  const char QUERY_POP[] = "count * FROM bg0ajs7w7p0xqozm0hek.`2020-08-15`;";
  char query[128];
  MySQL_Cursor *cur_mem = new MySQL_Cursor(&conn);
  sprintf(query, QUERY_POP, 9000000);
  // Execute the query
  cur_mem->execute(query);
  column_names *cols = cur_mem->get_columns();
  for (int f = 0; f < cols->num_fields; f++) {
    Serial.print(cols->fields[f]->name);
    if (f < cols->num_fields-1) {Serial.print(',');  }
  }
  Serial.println();
  row_values *row = NULL;
  do {  row = cur_mem->get_next_row();
    if (row != NULL) {
      for (int f = 0; f < cols->num_fields; f++) {
        Serial.print(row->values[f]);
        int valor= int(row->values[f]);
        return valor;
        if (f < cols->num_fields-1) {
          Serial.print(',');
        }
      }
      Serial.println();
    }
  } while (row != NULL);
  delete cur_mem;
}

void enviarDatos() {
   Serial.print("ingrese a qq EXITOSA");
    delay(1000);
    MySQL_Cursor *cur_mem = new MySQL_Cursor(&conn);
    delay(9000);
    Serial.println("Enviado.........................");
    float fecha = 10.3;
    float temperatura= 22.33;
    int chipid= 100;
    String casa= "casa";
    char buffer[10]=" ";
    //char INSERT_SQL[100]=" ";
    //char* formato = "INSERT INTO `bg0ajs7w7p0xqozm0hek`.`tutorial` (`chipid`, `fecha`, `temeperatura`) VALUES ('%i', '%.2f', '%.2f');";
  
    //sprintf(INSERT_SQL, formato, chipid, fecha, temperatura);
    
    String Fecha= "23/ss";
    String Hora ="13:12:11";
    float H= 52.2;
    float T=15.1;
    int pm20= 20;
    int pm100= 4;
    float latitud= 7.2;
    float longitud= -72.3;
    char* formato = "INSERT INTO `bg0ajs7w7p0xqozm0hek`.`2020-12-08` (`fecha`, `hora`, `H`, `T`, `PM2.5`, `PM10.0`, `LAT`, `LONG`) VALUES ('%s', '%s', '%f', '%f', '%i', '%i', '%.6f', '%.6f');";
    char* formato2="%s, %s,%f,%f, %i, %i, %.6f, %.6f,;";
     char INSERT_SQL[500]=" ";
    sprintf(INSERT_SQL, formato2, Fecha, Hora, H, T, pm20, pm100,latitud, longitud);
    Serial.println("transformado.........................");
    Serial.println(INSERT_SQL);

}
String query1;
int numRowsDB(String nameTable){
  
  query1 = "select count(*)  FROM bg0ajs7w7p0xqozm0hek.`"+nameTable+"`;";
  char query[800];
  query1.toCharArray(query, 800);
  long head_count = 0;
  MySQL_Cursor *cur_mem = new MySQL_Cursor(&conn);
  cur_mem->execute(query);
  column_names *cols = cur_mem->get_columns();
  row_values *row = NULL;
  String tabla;
  do { row = cur_mem->get_next_row();
    if (row != NULL) {
        tabla= row->values[0];
         }
  } while (row != NULL);
  Serial.print("conteo"); 
   Serial.println(tabla.toInt()); 
  delete cur_mem;
  return(tabla.toInt());
}
