package sample;



import javafx.beans.property.SimpleStringProperty;


public class listaTablas {
    SimpleStringProperty Nombre;

    public listaTablas(String Nombre) {

        this.Nombre = new SimpleStringProperty(Nombre);
    }

    public String getNombre() {
        return Nombre.get();
    }
}


