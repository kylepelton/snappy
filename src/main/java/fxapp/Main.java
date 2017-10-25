package fxapp;

import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import controllers.MainScreenController;
import controllers.InitConfigScreenController;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Properties prop = new Properties();
        InputStream configInput = null;
        FXMLLoader loader = null;
        Stage secondaryStage = null;
        boolean validSetup = false;

        try {
            String delim = System.getProperty("file.separator");
            File configFile = new File(System.getProperty("user.home") + delim
                + ".snappy" + delim + "config" + delim + "config.properties");
            //Show initial startup screen if first time using application
            if (!configFile.exists()) {

                secondaryStage = new Stage();
                loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/fxml/initialsetupscreen.fxml"));

                secondaryStage.setTitle("Welcome to Snappy!");
                secondaryStage.setScene(new Scene(loader.load()));
                secondaryStage.setOnCloseRequest(event -> System.exit(1));
                InitConfigScreenController initController = loader.getController();
                initController.setStage(secondaryStage);
                secondaryStage.showAndWait();

                //Create file if valid directory location given
                if (initController.getDirectory() != null) {
                    validSetup = true;
                    File newDir = new File(initController.getDirectory() + delim + "snappy_photos");
                    newDir.mkdir();

                    configFile.getParentFile().mkdirs();
                    configFile.createNewFile();
                    configInput = new FileInputStream(configFile);
                    prop.load(configInput);
                    configInput.close();

                    prop.setProperty("photosdir", initController.getDirectory() + delim + "snappy_photos");
                    OutputStream configOutput = new FileOutputStream(configFile);
                    prop.store(configOutput, null);
                    configOutput.close();
                }
            //Load config file if it exists
            } else {
                configInput = new FileInputStream(configFile);
                prop.load(configInput);
                configInput.close();
                validSetup = true;
            }

            //If config file exists, start application
            if (validSetup) {
                loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/fxml/mainscreen.fxml"));

                primaryStage.setTitle("Snappy");
                primaryStage.setScene(new Scene(loader.load()));
                primaryStage.show();

                MainScreenController controller = loader.getController();
                secondaryStage = new Stage();
                controller.setPrimaryStage(primaryStage);
                controller.setSecondaryStage(secondaryStage);
                controller.setProperties(prop);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}