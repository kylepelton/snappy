package fxapp;

import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import controllers.MainScreenController;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Properties prop = new Properties();
        InputStream configInput = null;

        try {
            String delim = System.getProperty("file.separator");
            File configFile = new File(System.getProperty("user.home") + delim
                + ".snappy" + delim + "config" + delim + "config.properties");
            if (!configFile.exists()) {
                configFile.getParentFile().mkdirs();
                configFile.createNewFile();
            }
            configInput = new FileInputStream(configFile);
            prop.load(configInput);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/mainscreen.fxml"));

            primaryStage.setTitle("Snappy");
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.show();

            MainScreenController controller = loader.getController();
            Stage secondaryStage = new Stage();
            controller.setPrimaryStage(primaryStage);
            controller.setSecondaryStage(secondaryStage);
            controller.setProperties(prop);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
