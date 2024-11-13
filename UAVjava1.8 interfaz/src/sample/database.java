package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class database {
    ObservableList listaTablas1 = FXCollections.observableArrayList();
    private String nombreBd="bg0ajs7w7p0xqozm0hek";
    private String usuario="u3kev6csezmozw4w";
    private String password="S4oZ3woGlgnPwh8ATAZD";
    private String timezone = "?autoReconnect=true&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private String url="jdbc:mysql://bg0ajs7w7p0xqozm0hek-mysql.services.clever-cloud.com/"+nombreBd+timezone+"";
    Connection con;
    public database() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, usuario, password);
            ResultSet rs = null;
            DatabaseMetaData meta = (DatabaseMetaData) con.getMetaData();

            if (con != null) {
                System.out.println("conexion Exitosa");
            }
        } catch (Exception e) {
            System.out.println("Error 1" + e);

        }

    }
    public void alldata(){
        try {
            ResultSet rs = null;
            DatabaseMetaData meta = con.getMetaData();
            rs = meta.getTables(null, null, null, new String[] {
                    "TABLE"
            });
            int count = 0;
            System.out.println("All table names are in test database:");
            while (rs.next()) {
                listaTablas1.addAll(new listaTablas(rs.getString("TABLE_NAME")));
                String tblName = rs.getString("TABLE_NAME");
                //System.out.println(tblName);
                count++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
