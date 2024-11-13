package sample;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ContenidoTablaBD {
    SimpleIntegerProperty id, PM10, PM25;
    SimpleStringProperty fecha, hora;
    SimpleDoubleProperty lat, longi, H, T;

    public ContenidoTablaBD(Integer id, String fecha, String hora, double H, double T, int PM25, int PM10, double lat, double longi) {
        this.id = new SimpleIntegerProperty(id);
        this.fecha = new SimpleStringProperty(fecha);
        this.hora = new SimpleStringProperty(hora);
        this.H = new SimpleDoubleProperty(H);
        this.T = new SimpleDoubleProperty(T);
        this.PM10 = new SimpleIntegerProperty(PM10);
        this.PM25 = new SimpleIntegerProperty(PM25);
        this.lat = new SimpleDoubleProperty(lat);
        this.longi = new SimpleDoubleProperty(longi);
    }

    public Integer getid() {
        return id.get();
    }

    public String gefecha() {
        return fecha.get();
    }

    public String gehora() {
        return hora.get();
    }

    public double getH() {
        return H.get();
    }

    public double getT() {
        return T.get();
    }

    public Integer getPM10() {
        return PM10.get();
    }

    public Integer getPM25() {
        return PM25.get();
    }

    public double getlat() {
        return lat.get();
    }

    public double getlongi() {
        return longi.get();
    }

    public  String[] getArray() {
        String[] datos ={String.valueOf(id), String.valueOf(fecha), String.valueOf(hora),String.valueOf(H),String.valueOf(T),
                String.valueOf(PM25),String.valueOf(PM10), String.valueOf(lat),String.valueOf(longi)};
        return datos;
    }

}
