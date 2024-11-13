package sample;

import com.sun.javafx.application.LauncherImpl;
import javafx.application.Application;
import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public database cn = new database();
    private static final int COUNT_LIMIT = 100;
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Monitoreo de Calidad del Aire");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
    @Override
    public void init() throws Exception {

        // Perform some heavy lifting (i.e. database start, check for application updates, etc. )
        for (int i = 1; i <= COUNT_LIMIT; i++) {
            double progress =(double) i/100;
            System.out.println("progress: " +  progress);
            LauncherImpl.notifyPreloader(this, new Preloader.ProgressNotification(progress));
            Thread.sleep(50);
        }

    }

    public static void main(String[] args) {
        LauncherImpl.launchApplication(Main.class, preloaderr.class, args);
    }
}
