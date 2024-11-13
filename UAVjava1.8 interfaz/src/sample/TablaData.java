package sample;


import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class TablaData {

    SimpleStringProperty Muestra;
    SimpleDoubleProperty Media, Mediana,Moda,Desv,Vmin,Vmax;
    public TablaData(String Muestra, double Media, double Mediana, double Moda, double Desv, double Vmin, double Vmax) {
        this.Muestra = new SimpleStringProperty(Muestra);
        this.Media = new SimpleDoubleProperty(Media);
        this.Mediana = new SimpleDoubleProperty(Mediana);
        this.Moda = new SimpleDoubleProperty(Moda);
        this.Desv = new SimpleDoubleProperty(Desv);
        this.Vmin = new SimpleDoubleProperty(Vmin);
        this.Vmax = new SimpleDoubleProperty(Vmax);

    }
    public String getMuestra() { return Muestra.get(); }
    public double getMedia(){ return Media.get();   }
    public double getMediana(){  return Mediana.get(); }
    public double getModa(){  return Moda.get();}
    public double getDesv(){  return Desv.get();    }
    public double getVmin(){  return Vmin.get();    }
    public double getVmax(){  return Vmax.get();    }





}


