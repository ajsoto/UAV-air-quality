package sample;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


import com.csvreader.CsvWriter;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

//guardar
//import java.awt.event.ActionEvent;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Controller implements Initializable {
    @FXML
    ScrollPane UAVShow,SistemaShow;
    //cursores
    @FXML private ImageView arrow_inicio, arrow_medidas, arrow_estandares, arrow_soporte;
    @FXML private ImageView arrowUAVShow, arrowSistemaShow;
    //paneles Principales
    @FXML public AnchorPane panel_inicio, panel_medidas, panel_estandares, panel_soporte;

    // Paneles Secundarios
    @FXML public AnchorPane panel_historico, panel_estadistica, panel_descargas;
    @FXML private JFXButton GraficarHis, MostrarEstd;
    @FXML private WebView webView;
    @FXML private JFXComboBox ComboVariables,ComboFechaMapa, ComboVariableEstadistica, ComboVariableHis;
    @FXML private JFXComboBox ComboDtFinHis, ComboDtInHis,ComboDtEstd, ComboDTFinEstd;
    @FXML private JFXComboBox fechaDowI, fechaDowF;
    @FXML public TableView tabla;
    @FXML public TableColumn  Muestra, Media, Mediana,Moda,Desv,Vmin,Vmax;
    @FXML public LineChart<Number, Number> graficaH;
    @FXML   private CategoryAxis xAxisBar= new CategoryAxis();
    @FXML private NumberAxis yAxisBar= new NumberAxis();
    public String fecha11="";
    @FXML private  BarChart<String,Number> EstadisticHistogram ;
    @FXML private HBox PM;
    @FXML private VBox Temperatura, Hum;

    //barra de galeria
    @FXML ImageView mainImageView,mainImageView1;
    @FXML
    AnchorPane primaryStage, primaryStage1;
    @FXML ScrollPane scrollPane,scrollPane1;
    @FXML HBox imagesStore,imagesStore1;
    private List<Image> images = new ArrayList<>();
    private List<Image> images1 = new ArrayList<>();

    ObservableList listaAlumnos = FXCollections.observableArrayList();
    ObservableList listaContenidoTablaBD = FXCollections.observableArrayList();
    private ArrayList vectorBD = new ArrayList();//por silas moscas
    private ArrayList vector = new ArrayList(), variables=new ArrayList(), valores=new ArrayList();
    ObservableList<String> comboIDContent2;

    private ObservableList<Double> valorDeDesviacion = FXCollections.observableArrayList();
    private ObservableList<String> medidaDeDesviacion = FXCollections.observableArrayList();


    private XYChart.Series seriesMedia = new XYChart.Series();
    private XYChart.Series seriesMediana = new XYChart.Series();
    private XYChart.Series seriesModa = new XYChart.Series();
    private XYChart.Series seriesMin = new XYChart.Series();
    private XYChart.Series seriesMax = new XYChart.Series();


    private Statement st, sta;
    private ResultSet rs,  rsa;
    public database cn = new database();

    private ArrayList vector2 = new ArrayList();


    @FXML public AnchorPane EstandarPM, EstandarH;
    @FXML public AnchorPane EstandarT;
    @FXML public Label NameWH, NameMail, NameUbicacion;
    @FXML public ImageView arrowPM, arrowH, arrowT;
    private void loadImages(String path, List images) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose Directory");
        File directory = new File(path);
        if (directory != null) {
            File[] files = directory.listFiles();
            String fileName;
            for (int i = 0; i < files.length; ++i) {
                fileName = files[i].getName().toLowerCase();
                if (files[i] != null && (fileName.endsWith(".jpg") || fileName.endsWith(".png") ||
                        fileName.endsWith(".bmp"))) {
                    try {
                        images.add(SwingFXUtils.toFXImage(ImageIO.read(files[i]), null));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            System.exit(1);
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        EstadisticHistogram.setAnimated(false);
        ComboFechaMapa.setDisable(true);
        ComboDtFinHis.setDisable(true);
        GraficarHis.setDisable(true);
        MostrarEstd.setDisable(true);
        ComboDTFinEstd.setDisable(true);
        //galeria
        loadImages("src/sample/image/UAV/",images);
        mainImageView.setImage(images.get(0));
        mainImageView.setPreserveRatio(true);
        for (int i = 0; i < images.size(); ++i) {
            ImageView imageView = new ImageView();
            imageView.setCursor(javafx.scene.Cursor.CLOSED_HAND);
            imageView.setOnMouseClicked(event ->
                    mainImageView.setImage(imageView.getImage()) );
            imageView.setImage(images.get(i));
            imageView.setFitWidth(primaryStage.getWidth() / 4);
            imageView.setFitHeight(primaryStage.getHeight() / 4 - 50);
            imageView.setPreserveRatio(true);
            //imageView.setFitWidth(70);
            imageView.setFitHeight(70);
            imagesStore.getChildren().add(imageView);
        }

        scrollPane1.setContent(imagesStore1);
        scrollPane1.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);


        loadImages("src/sample/image/sistema/", images1);
        mainImageView1.setImage(images1.get(0));
        mainImageView1.setPreserveRatio(true);
        for (int i = 0; i < images1.size(); ++i) {
            ImageView imageView = new ImageView();
            imageView.setCursor(javafx.scene.Cursor.CLOSED_HAND);
            imageView.setOnMouseClicked(event ->
                    mainImageView1.setImage(imageView.getImage()) );
            imageView.setImage(images1.get(i));
            imageView.setFitWidth(primaryStage1.getWidth() / 4);
            imageView.setFitHeight(primaryStage1.getHeight() / 4 - 50);
            imageView.setPreserveRatio(true);
            imageView.setFitHeight(70);
            imagesStore1.getChildren().add(imageView);
        }

        scrollPane1.setContent(imagesStore1);
        scrollPane1.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);



        // se asocia la lista de datos a la tabla


        try {


            DatabaseMetaData meta = (DatabaseMetaData) cn.con.getMetaData();
            rs = meta.getTables(null, null, null, new String[] {"TABLE" });
            int count = 0;
            while (rs.next()) {
                vector.add(rs.getString("TABLE_NAME"));
                String tblName = rs.getString("TABLE_NAME");

                count++;
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        // añade el boton para selección de columnas
        tabla.setTableMenuButtonVisible(true);

        // muestra un elemento si la tabla está vacía
        tabla.setPlaceholder(new Label("La tabla no contiene datos"));
        //vectores de fechas


        variables.add("PM2.5");
        variables.add("PM10.0");
        variables.add("Humedad");
        variables.add("Temperatura");
        ObservableList<String> comboIDVariables =
                FXCollections.observableArrayList(variables);
        ObservableList<String> comboIDContent =
                FXCollections.observableArrayList(vector);

        ComboFechaMapa.setItems(comboIDContent);
        fechaDowI.setItems(comboIDContent);

        ComboDtEstd.setItems(comboIDContent);
        ComboDtInHis.setItems(comboIDContent);

        ComboVariables.setItems(comboIDVariables);
        ComboVariableEstadistica.setItems(comboIDVariables);
        ComboVariableHis.setItems(comboIDVariables);

        fechaDowF.setDisable(true);
        WebEngine engine=webView.getEngine();

        engine.loadContent("\t<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>Simple Leaflet Map with Heatmap </title>\n" +
                "    <meta charset=\"utf-8\" />\n" +
                "    <link \n" +
                "        rel=\"stylesheet\" \n" +
                "        href=\"http://cdn.leafletjs.com/leaflet-0.7/leaflet.css\"\n" +
                "    />\n" +
                "<style type=\"text/css\">\n" +
                "      /* Always set the map height explicitly to define the size of the div\n" +
                "       * element that contains the map. */\n" +
                "      #map {\n" +
                "        height: 100%;\n" +
                "      }\n" +
                "\n" +
                "      /* Optional: Makes the sample page fill the window. */\n" +
                "      html,\n" +
                "      body {\n" +
                "        height: 100%;\n" +
                "        margin: 0;\n" +
                "        padding: 0;\n" +
                "      }\n" +
                "\n" +
                "     \n" +
                "    </style>"+
                "</head>\n" +
                "<body>\n" +
                "    <title>Simple Leaflet Map with Heatmap </title>\n" +
                "    <div id=\"map\" style=\"width: 608px; height: 409px\"></div>\n" +
                "\n" +
                "    <script\n" +
                "       src=\"http://cdn.leafletjs.com/leaflet-0.7/leaflet.js\">\n" +
                "    </script>\n" +
                "    \n" +
                "    <script\n" +
                "       src=\"http://leaflet.github.io/Leaflet.heat/dist/leaflet-heat.js\">\n" +
                "    </script>\n" +
                "  <script ></script>\n" +
                "    <script>\n" +
                "        \n" +
                "        var map = L.map('map').setView([7.9475,-72.5065], 18);\n" +
                "        mapLink = \n" +
                "            '<a href=\"http://openstreetmap.org\">OpenStreetMap</a>';\n" +
                "        L.tileLayer(\n" +
                "            'http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {\n" +
                "            attribution: '&copy; ' + mapLink + ' Contributors',\n" +
                "            maxZoom: 22,\n" +
                "\n" +
                "        }).addTo(map);\n" +
                "        \n" +
                "        \n" +
                "    \n" +
                "    \n" +
                "    </script>\n" +
                "\n" +
                "</body>\n" +
                "</html>");

    }

    public void Mapa(String fechafinal, String Variable){

        String ubc = "`, `LAT`, `LONG";
        int rango[]={};
        String[] color={};
        switch(Variable) {
            case "H":
                rango= new int[]{17,38,50,66,83};
                color= new String[]{"cyan", "blue", "yellow", "orange", "red","purple"};
                PM.setVisible(false); Hum.setVisible(true);
                Temperatura.setVisible(false);

                break;
            case "T":
                rango= new int[]{25,30,35, 40, 45};
                color= new String[]{"cyan", "blue", "yellow", "orange", "red","purple"};
                Temperatura.setVisible(true);Hum.setVisible(false);
                PM.setVisible(false);
                break;
            case "PM2.5":
                rango= new int[]{12, 37, 55, 150,250};
                color= new String[]{"green", "yellow", "orange", "red", "purple","brown"};
                PM.setVisible(true);Hum.setVisible(false);
                Temperatura.setVisible(false);
                break;
            case "PM10.0":
                rango= new int[]{54, 154, 254, 354,424};
                color= new String[]{"green", "yellow", "orange", "red", "purple","brown"};
                PM.setVisible(true);
                Temperatura.setVisible(false);
                break;

            default:
                rango= new int[]{20, 25, 30, 35,60,80};
                color= new String[]{"green", "yellow", "orange", "red", "purple","brown"};
                // code block
        }
        double medida=0;
        try {


            st = cn.con.createStatement();
            rs = st.executeQuery("Select `" +Variable +ubc+ "` from `" + fechafinal + "`;");
            ArrayList ubica = new ArrayList();
            String ubicacion = "[0,0],", ubicacion1 = "[0,0],", ubicacion2="[0,0],",
                    ubicacion3 ="[0,0],", ubicacion4="[0,0],", ubicacion5="[0,0]";

            String lat="[0,0],";
            while (rs.next()) {
                medida=rs.getDouble(Variable);
                lat=rs.getDouble("LAT")+ "," + rs.getDouble("LONG");
                if(medida<rango[0]){
                    ubicacion  = ubicacion  + "[" + rs.getDouble("LAT") + "," + rs.getDouble("LONG") + "],";
                }if(medida>=rango[0] && medida<rango[1]){
                    ubicacion1 = ubicacion1 + "[" + rs.getDouble("LAT") + "," + rs.getDouble("LONG") + "],";

                }if (medida>=rango[1]  && medida<rango[2]){
                    ubicacion2 = ubicacion2 + "[" + rs.getDouble("LAT") + "," + rs.getDouble("LONG") + "],";

                }if (medida>=rango[2] && medida<rango[3]) {
                    ubicacion3 = ubicacion3 + "[" + rs.getDouble("LAT") + "," + rs.getDouble("LONG") + "],";

                }if (medida>=rango[3] && medida<rango[4]) {
                    ubicacion4 = ubicacion4 + "[" + rs.getDouble("LAT") + "," + rs.getDouble("LONG") + "],";

                }if (medida>=rango[4]){
                    ubicacion5 = ubicacion5 + "[" + rs.getDouble("LAT") + "," + rs.getDouble("LONG") + "],";
                }
            }



            WebEngine engine = webView.getEngine();
            //cargarlo desde una ruta externa

            //String urll = getClass().getResource("mapa1.html").toExternalForm();
            //engine.setJavaScriptEnabled(true);
            //engine.load(urll);

            engine.loadContent("<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "    <title>Simple Leaflet Map with Heatmap </title>\n" +
                    "    <meta charset=\"utf-8\" />\n" +
                    "    <link \n" +
                    "        rel=\"stylesheet\" \n" +
                    "        href=\"http://cdn.leafletjs.com/leaflet-0.7/leaflet.css\"\n" +
                    "    />\n" +
                    "<style type=\"text/css\">\n" +
                    "      /* Always set the map height explicitly to define the size of the div\n" +
                    "       * element that contains the map. */\n" +
                    "      #map {\n" +
                    "        height: 100%;\n" +
                    "      }\n" +
                    "\n" +
                    "      /* Optional: Makes the sample page fill the window. */\n" +
                    "      html,\n" +
                    "      body {\n" +
                    "        height: 100%;\n" +
                    "        margin: 0;\n" +
                    "        padding: 0;\n" +
                    "      }\n" +
                    "\n" +
                    "     \n" +
                    "    </style>"+
                    "</head>\n" +
                    "<body>\n" +
                    "    <title>Simple Leaflet Map with Heatmap </title>\n" +
                    "    <div id=\"map\" style=\"border:1px; width: 608px; height: 409px\"></div>\n" +
                    "\n" +
                    "    <script\n" +
                    "       src=\"http://cdn.leafletjs.com/leaflet-0.7/leaflet.js\">\n" +
                    "    </script>\n" +
                    "    \n" +
                    "    <script\n" +
                    "       src=\"http://leaflet.github.io/Leaflet.heat/dist/leaflet-heat.js\">\n" +
                    "    </script>\n" +
                    "  <script ></script>\n" +
                    "    <script>\n" +
                    "        var quake= ["+ ubicacion +"];\n" +
                    "        var quake1= ["+ ubicacion1 +"];\n" +
                    "        var quake2 =["+ ubicacion2 +"];\n" +
                    "        var quake3 = ["+ ubicacion3 +"];\n" +
                    "        var quake4 = ["+ ubicacion4 +"];\n" +
                    "        var quake5 = ["+ ubicacion5 +"];\n" +
                    "        var map = L.map('map').setView(["+lat+"], 18);\n" +
                    "        mapLink = \n" +
                    "            '<a href=\"http://openstreetmap.org\">OpenStreetMap</a>';\n" +
                    "        L.tileLayer(\n" +
                    "            'http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {\n" +
                    "            attribution: '&copy; ' + mapLink + ' Contributors',\n" +
                    "            maxZoom: 19,\n" +
                    "\n" +
                    "        }).addTo(map);\n" +
                    "        \n" +
                    "        \n" +
                    "        var heat = L.heatLayer(quake,{\n" +
                    "            radius: 15,\n" +
                    "            blur: 28, \n" +
                    "            maxZoom: 1,\n" +
                    "            gradient:{1: '"+color[0]+"'},\n" +
                    "        }).addTo(map);\n" +
                    "        var heat = L.heatLayer(quake1,{\n" +
                    "            radius: 15,\n" +
                    "            blur: 28, \n" +
                    "            maxZoom: 1,\n" +
                    "            gradient:{1: '"+color[1]+"'},\n" +
                    "        }).addTo(map);\n" +
                    "        var heat = L.heatLayer(quake2,{\n" +
                    "            radius: 15,\n" +
                    "            blur: 28, \n" +
                    "            maxZoom: 1,\n" +
                    "            gradient:{1: '"+color[2]+"'},\n" +
                    "        }).addTo(map);\n" +
                    "        var heat = L.heatLayer(quake3,{\n" +
                    "            radius: 15,\n" +
                    "            blur: 28, \n" +
                    "            maxZoom: 1,\n" +
                    "            gradient:{1: '"+color[3]+"'},\n" +
                    "        }).addTo(map);\n" +
                    "         var heat = L.heatLayer(quake4,{\n" +
                    "            radius: 15,\n" +
                    "            blur: 28, \n" +
                    "            maxZoom: 1,\n" +
                    "            gradient:{0.9: '"+color[4]+"'},\n" +
                    "        }).addTo(map);\n" +
                    "         var heat = L.heatLayer(quake5,{\n" +
                    "            radius: 15,\n" +
                    "            blur: 28, \n" +
                    "            maxZoom: 10,\n" +
                    "            gradient:{0.1: '"+color[5]+"'},\n" +
                    "        }).addTo(map);\n" +
                    "         var heat = L.heatLayer(quakePurple,{\n" +
                    "            radius: 12,\n" +
                    "            blur: 28, \n" +
                    "            maxZoom: 1,\n" +
                    "            gradient:{0.1: 'purple'},\n" +
                    "        }).addTo(map);\n" +
                    "    \n" +
                    "    </script>\n" +
                    "\n" +
                    "</body>\n" +
                    "</html>");



        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public void FechaMapa(ActionEvent actionEvent) {
        fecha11=ComboFechaMapa.getValue().toString();
        String Variablee=CambioDeNombreVariable(ComboVariables.getValue().toString());
        Mapa(fecha11, Variablee);


    }

    public void VariableMapa(ActionEvent actionEvent) {
        ComboFechaMapa.setDisable(false);

        if(fecha11!=""){
            fecha11=ComboFechaMapa.getValue().toString();
            String Variablee=CambioDeNombreVariable(ComboVariables.getValue().toString());
            Mapa(fecha11, Variablee);

        }

    }
    public void onDateEstd(javafx.event.ActionEvent event){
        ComboDTFinEstd.setDisable(false);
        ComboDos(ComboDtEstd);
    }
    //------------FIN  CALCULA LA ESTADISTICA DE LA INTERFAZ---------
    //-------Muestra las fechas siguente al combo 1-------
    public  void ComboDos(JFXComboBox comboDts){
        vector2.clear();

        boolean agregar = false;
        for (int j = 0; j < vector.size(); j++) {
            if (vector.get(j).equals(comboDts.getValue())) {
                agregar = true;
            }
            if (agregar){
                vector2.add(vector.get(j));
            }
        }
        comboIDContent2 = FXCollections.observableArrayList(vector2);

        ComboDtFinHis.setItems(comboIDContent2);
        ComboDTFinEstd.setItems(comboIDContent2);
        fechaDowF.setItems(comboIDContent2);
    }
    //-------FIN Muestra las fechas siguente al combo 1-------


    public String CambioDeNombreVariable(String Variable){
        if(Variable=="Humedad"){
            Variable="H";
        } if(Variable=="Temperatura"){
            Variable="T";
        }
        return Variable;
    }
    private Integer count=0;

    //--------se encargan del historial-------///
    public void historialCambio(String fechafinal){

        XYChart.Series<Number, Number> series1 = new XYChart.Series<>();
        try{
            Statement st;
            ResultSet plotHum;
            st=cn.con.createStatement();
            String Variable=CambioDeNombreVariable(ComboVariableHis.getValue().toString());
            String g= fechafinal;
            plotHum=st.executeQuery("select  `"+ Variable+"` from `"+ g+ "`");
            while(plotHum.next()){

                count++;
                series1.getData().add(new XYChart.Data<>(count, plotHum.getDouble(Variable)));
            }
            series1.setName(g);
            graficaH.getData().add(series1);
            graficaH.setCreateSymbols(false);
            graficaH.setTitle(Variable);

        }catch (Exception e) {
            System.out.println("Error " + e);

        }
    }
    public void GraficarHist(MouseEvent event) {

        graficaH.getData().clear();
        count=0;

        ArrayList vector3 = vector2;

        Iterator it = vector3.iterator();
        while(it.hasNext()){
            String fechafinal= (String) it.next();

            if((ComboDtFinHis.getValue().toString() == fechafinal)) {

                historialCambio(fechafinal);

                break;
            }
            else{
                historialCambio(fechafinal);}
        }




    }
    public void onHistDate(ActionEvent actionEvent) {
        ComboDos(ComboDtInHis);
        ComboDtFinHis.setDisable(false);
    }
    public void onDateHiFin(ActionEvent actionEvent) {

        GraficarHis.setDisable(false);
    }
//----------fin Historial-------------//

    //----------mostrar estadistica----------//
    public static double[] convertDoubles(List<Double> doubles)
    {
        double[] ret = new double[doubles.size()];
        Iterator<Double> iterator = doubles.iterator();
        int i = 0;
        while(iterator.hasNext())
        {
            ret[i] = iterator.next();
            i++;
        }
        return ret;
    }
    public void EstadisticaCambio(String fechafinal){

        String Variable=CambioDeNombreVariable(ComboVariableEstadistica.getValue().toString());
        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        int cc=1;
        try {
            sta = cn.con.createStatement();
            rsa = sta.executeQuery("Select `"+Variable+"` from `"+ fechafinal+"`;");
            //DescriptiveStatistics statsH = new DescriptiveStatistics(), statsT=new DescriptiveStatistics(),
            //       statsPM10= new DescriptiveStatistics(), statsPM25= new DescriptiveStatistics();
            DescriptiveStatistics statsVariable = new DescriptiveStatistics();
            ArrayList valoresss = new ArrayList();

            while (rsa.next()) {
                try {
                    statsVariable.addValue(rsa.getDouble(Variable));
                    cc=cc+1;
                    valoresss.add(rsa.getDouble(Variable));

                }catch (Exception e){ }
            }
            double[] arr = valoresss.stream().mapToDouble(d -> (double) d).toArray();
            double modaa=getModa(arr);
            seriesMedia.setName("Media");
            seriesMedia.getData().add(new XYChart.Data<String, Number>(fechafinal,  statsVariable.getMean()));
            seriesMediana.setName("Mediana");
            seriesMediana.getData().add(new XYChart.Data<String, Number>(fechafinal,  statsVariable.getPercentile(50)));
            seriesModa.setName("Moda");
            seriesModa.getData().add(new XYChart.Data<String, Number>(fechafinal,  modaa));
            seriesMin.setName("Min");
            seriesMin.getData().add(new XYChart.Data<String, Number>(fechafinal,  statsVariable.getMin()));
            seriesMax.setName("Max");
            seriesMax.getData().add(new XYChart.Data(fechafinal, statsVariable.getMax()));





            //estaditica aui
            listaAlumnos.addAll(new TablaData(fechafinal,statsVariable.getMean(),
                    statsVariable.getPercentile(50),modaa,statsVariable.getStandardDeviation(),
                    statsVariable.getMin(),statsVariable.getMax()));
            EstadisticHistogram.autosize();
            //FIN CSV  ---------------------------------------------------------------------------------------//
        }catch (Exception e){
            database cn = new database();
            System.out.println("error danger");
        }
        tabla.setItems(listaAlumnos);



    }
    public void MostrarEstadistica(ActionEvent actionEvent) {
        seriesMedia.getData().clear();
        seriesMediana.getData().clear();
        seriesModa.getData().clear();
        seriesMin.getData().clear();
        seriesMax.getData().clear();
        listaAlumnos.clear();

        EstadisticHistogram.autosize();

        ArrayList vector3 = vector2;

        Iterator it = vector3.iterator();
        while(it.hasNext()){
            String fechafinal= (String) it.next();

            if((ComboDTFinEstd.getValue().toString() == fechafinal)) {
                EstadisticaCambio(fechafinal);
                break;
            }
            else{
                EstadisticaCambio(fechafinal);}

        }
        EstadisticHistogram.getData().addAll(seriesMedia, seriesMediana, seriesModa,seriesMin, seriesMax);
        EstadisticHistogram.autosize();
        Muestra.setCellValueFactory(new PropertyValueFactory<TablaData, String>("Muestra"));
        Media.setCellValueFactory(new PropertyValueFactory<TablaData, Double>("Media"));
        Mediana.setCellValueFactory(new PropertyValueFactory<TablaData, Double>("Mediana"));
        Moda.setCellValueFactory(new PropertyValueFactory<TablaData, Double>("Moda"));
        Desv.setCellValueFactory(new PropertyValueFactory<TablaData, Double>("Desv"));
        Vmin.setCellValueFactory(new PropertyValueFactory<TablaData, Double>("Vmin"));
        Vmax.setCellValueFactory(new PropertyValueFactory<TablaData, Double>("Vmax"));

    }

    public static Date ParseFecha(String fecha) {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaDate = null;
        try {
            fechaDate = formato.parse(fecha);
        }
        catch (ParseException ex)
        {

        }
        return fechaDate;
    }

    public void onInicionButtonClicked(MouseEvent event){

        panel_inicio.setVisible(true);
        arrow_inicio.setVisible(true);

        panel_estandares.setVisible(false);
        arrow_estandares.setVisible(false);
        panel_medidas.setVisible(false);
        arrow_medidas.setVisible(false);
        panel_soporte.setVisible(false);
        arrow_soporte.setVisible(false);
        panel_historico.setVisible(false);
        panel_estadistica.setVisible(false);
    }
    public void onMedidasButtonClicked(MouseEvent event){
        panel_medidas.setVisible(true);
        arrow_medidas.setVisible(true);

        panel_inicio.setVisible(false);
        arrow_inicio.setVisible(false);
        panel_estandares.setVisible(false);
        arrow_estandares.setVisible(false);
        panel_soporte.setVisible(false);
        arrow_soporte.setVisible(false);
        panel_historico.setVisible(false);
        panel_estadistica.setVisible(false);
        panel_descargas.setVisible(false);

        //base de datos

    }
    public void onEstandaresButtonClicked(MouseEvent event){
        panel_estandares.setVisible(true);
        arrow_estandares.setVisible(true);

        panel_inicio.setVisible(false);
        arrow_inicio.setVisible(false);
        panel_medidas.setVisible(false);
        arrow_medidas.setVisible(false);
        panel_soporte.setVisible(false);
        arrow_soporte.setVisible(false);
        panel_historico.setVisible(false);
        panel_estadistica.setVisible(false);
        panel_descargas.setVisible(false);


    }
    public void onSoporteButtonClicked(MouseEvent event){
        panel_soporte.setVisible(true);
        arrow_soporte.setVisible(true);

        panel_inicio.setVisible(false);
        arrow_inicio.setVisible(false);
        panel_medidas.setVisible(false);
        arrow_medidas.setVisible(false);
        panel_estandares.setVisible(false);
        arrow_estandares.setVisible(false);
        panel_historico.setVisible(false);
        panel_estadistica.setVisible(false);
        panel_descargas.setVisible(false);
    }
    public void onHistoricoButtonClicked(MouseEvent event){
        panel_historico.setVisible(true);
        arrow_medidas.setVisible(true);

        panel_inicio.setVisible(false);
        arrow_inicio.setVisible(false);
        panel_medidas.setVisible(false);
        panel_estandares.setVisible(false);
        arrow_estandares.setVisible(false);
        panel_soporte.setVisible(false);
        arrow_soporte.setVisible(false);
        panel_estadistica.setVisible(false);
        panel_descargas.setVisible(false);

    }
    public void onEstadisticaButtonClicked(MouseEvent event) {
        panel_estadistica.setVisible(true);
        arrow_medidas.setVisible(true);
        MostrarEstd.setDisable(false);

        panel_estandares.setVisible(false);
        arrow_estandares.setVisible(false);
        panel_medidas.setVisible(false);

        panel_soporte.setVisible(false);
        arrow_soporte.setVisible(false);
        panel_historico.setVisible(false);
        panel_inicio.setVisible(false);
        arrow_inicio.setVisible(false);

        ComboDtFinHis.setDisable(true);
        GraficarHis.setDisable(true);
        seriesMedia.getData().clear();
        seriesMediana.getData().clear();
        seriesModa.getData().clear();
        seriesMin.getData().clear();
        seriesMax.getData().clear();
    }
    public void onDescargasButtonClicked(MouseEvent event) {
        panel_descargas.setVisible(true);


        panel_estandares.setVisible(false);
        arrow_estandares.setVisible(false);
        panel_medidas.setVisible(false);
        panel_soporte.setVisible(false);
        arrow_soporte.setVisible(false);
        panel_historico.setVisible(false);
        panel_inicio.setVisible(false);
        arrow_inicio.setVisible(false);
    }


    public static double getModa(double[] muestra) {

        int maximoNumRepeticiones= 0;
        double moda= 0;

        for(int i=0; i<muestra.length; i++)
        {
            int numRepeticiones= 0;
            for(int j=0; j<muestra.length; j++)
            {
                if(muestra[i]==muestra[j])
                {
                    numRepeticiones++;
                }   //fin if
                if(numRepeticiones>maximoNumRepeticiones)
                {
                    moda= muestra[i];
                    maximoNumRepeticiones= numRepeticiones;
                }   //fin if
            }
        }   //fin for

        return moda;

    }   //fin getModa


    public void HabilitarMostrar(ActionEvent actionEvent) {
        MostrarEstd.setDisable(false);
    }
    public void guardarCSV(){
        String fechafinal="nombre del cssv";

        try {
            sta = cn.con.createStatement();
            rsa = sta.executeQuery("Select * from `"+ fechafinal+"`;");
            String fileName = "src/items.csv";
            CsvWriter csvWriter = new CsvWriter(fileName);
            ArrayList<ContenidoTablaBD> Productos= new ArrayList();
            //Crear CSV con la data de la nuve-----------------------------------------------------------------//

            DescriptiveStatistics statsH = new DescriptiveStatistics(), statsT=new DescriptiveStatistics(),
                    statsPM10= new DescriptiveStatistics(), statsPM25= new DescriptiveStatistics();

            while (rsa.next()) {
                try {


                    String[] entries = { String.valueOf(rsa.getInt("id")),  String.valueOf(rsa.getString("fecha")),
                            rsa.getString("hora"),  String.valueOf(rsa.getDouble("H")),  String.valueOf(rsa.getDouble("T")),
                            String.valueOf(rsa.getInt("PM2.5")),   String.valueOf(rsa.getInt("PM10.0")),
                            String.valueOf(rsa.getDouble("LAT")),  String.valueOf(rsa.getDouble("LONG")) };

                    csvWriter.writeRecord(entries);



                }catch (Exception e){ }
            }
            csvWriter.close();
            //FIN CSV  ---------------------------------------------------------------------------------------//
        }catch (Exception e){

            System.out.println("error danger");
        }


    }


    public void VerEstandarPM(ActionEvent actionEvent) {
        EstandarPM.setVisible(true); EstandarH.setVisible(false);
        EstandarT.setVisible(false);
        arrowPM.setVisible(true);
        arrowH.setVisible(false);
        arrowT.setVisible(false);

    }

    public void VerEstandarT(ActionEvent actionEvent) {
        EstandarPM.setVisible(false); EstandarH.setVisible(false);
        EstandarT.setVisible(true);
        arrowPM.setVisible(false); arrowH.setVisible(false);
        arrowT.setVisible(true);
    }
    public void VerEstandarH(ActionEvent actionEvent) {
        EstandarPM.setVisible(false); EstandarH.setVisible(true);
        EstandarT.setVisible(false);
        arrowPM.setVisible(false); arrowH.setVisible(true);
        arrowT.setVisible(false);
    }

    public void CallLLamada(MouseEvent mouseEvent) {
        String web11="https://chat.whatsapp.com/JJdZijk6vhxFna5DYx15rn";
        weblink(web11);

    }

    public void CallCorreo(MouseEvent mouseEvent) {
        String web11="mailto:jeisoneduardoep@ufps.edu.co";
        weblink(web11);
    }

    public void CallUbicacion(MouseEvent mouseEvent) {
        String web11="https://ww2.ufps.edu.co/";
        weblink(web11);


    }
    public void weblink(String web11){
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    desktop.browse(new URI(web11));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void VerSistema(MouseEvent mouseEvent) {
        UAVShow.setVisible(false);
        SistemaShow.setVisible(true);
        arrowUAVShow.setVisible(false);
        arrowSistemaShow.setVisible(true);

    }

    public void VerUAV(MouseEvent mouseEvent) {
        UAVShow.setVisible(true);
        SistemaShow.setVisible(false);
        arrowUAVShow.setVisible(true);
        arrowSistemaShow.setVisible(false);
    }
    private Stage stage;
    public int ccc=0;
    public String buscarDescarga(String fechafinal, String ubica){
        try {


            st = cn.con.createStatement();
            rs = st.executeQuery("Select * from `" + fechafinal + "`;");



            while (rs.next()) {
                ubica  = ubica  +ccc+ "," +rs.getString("Fecha")+ "," +rs.getString("hora")+ "," +
                        rs.getDouble("H")+ "," +rs.getDouble("T")+ "," +
                        rs.getDouble("PM10.0")+ "," + rs.getDouble("PM2.5")+ "," +
                        rs.getDouble("LAT") + "," + rs.getDouble("LONG") + "\n";
                ccc++;

            }
            System.out.println(ubica);

        } catch (SQLException e) {
            System.out.println("error try");
            e.printStackTrace();
        }
        return (ubica);
    }

    public void Descargar(ActionEvent actionEvent) {
        ccc=0;
        FileChooser fileChooser = new FileChooser();
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("*.TXT", "txt");
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter(
                "Portable Network Graphics (CSV)", "csv"));
        File file = fileChooser.showSaveDialog(stage);
        file= new File(file + ".csv");


        String ubica = "ID,Fecha,Hora,Humedad,Temperatura,PM10.0,PM2.5,Latitud,Longitud;\n";
        if(file!=null){
            FileWriter fw = null;
            BufferedWriter bw = null;
            try {
                fw = new FileWriter(file, false);
                bw = new BufferedWriter(fw);
                ArrayList vector3 = vector2;

                Iterator it = vector3.iterator();
                while(it.hasNext()){
                    String fechafinal= (String) it.next();
                    System.out.println(fechafinal);

                    if((fechaDowF.getValue().toString() == fechafinal)) {
                        ubica=buscarDescarga(fechafinal, ubica);
                        break;
                    }
                    else{
                        ubica=buscarDescarga(fechafinal, ubica);}
                }

                System.out.println(ubica+"ddd");
                bw.write(ubica, 0, ubica.length());

            } catch (Exception e) {
                System.out.println(ubica+"errror");
            } finally {
                try {
                    bw.close();
                } catch (Exception e2) {

                }
            }

        }

        System.out.println("guardado");
        //ubica = "ID,Fecha,Hora,Humedad,Temperatura,PM10.0,PM2.5,Latitud,Longitud;\n";
        }

    public void OnDescargaDate(ActionEvent actionEvent) {
        ComboDos(fechaDowI);
        fechaDowF.setDisable(false);
    }
}

