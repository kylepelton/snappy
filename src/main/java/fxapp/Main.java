package fxapp;

import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import controller.MainScreenController;
import controller.InitConfigScreenController;
import fxapp.Logger;

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
        Stage helpStage = null;
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

            //Set up log
            File logDir = new File(System.getProperty("user.home") + delim
                + ".snappy" + delim + "logs");
            if (!logDir.exists()) {
                logDir.mkdirs();
            } else if (!logDir.isDirectory()) {
                logDir.delete();
            }
            File logFile = new File(System.getProperty("user.home") + delim
                + ".snappy" + delim + "logs" + delim + "LOG.txt");
            if (logFile.exists()) {
                // Don't know how this is possible, but leave in for sanity check
                logFile.delete();
            }
            logFile.createNewFile();
            Logger.log("LOG FILE SUCCESSFULLY CREATED\n\n");

            File photosdir = new File(prop.getProperty("photosdir")); 
            if (!photosdir.exists() || !photosdir.isDirectory()) {
                System.err.println("Error: Photo Directory " + photosdir + " is missing.");
                try {
                    photosdir.mkdirs();
                } catch (Exception e) {  // if directory creation fails
                    e.printStackTrace();
                    validSetup = false;
                }
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
                helpStage = new Stage();
                controller.setPrimaryStage(primaryStage);
                controller.setSecondaryStage(secondaryStage);
                controller.setHelpStage(helpStage);
                controller.setProperties(prop);
            }
        } catch (Exception e) {
            // We can't guarantee at this point that the log has been created
            e.printStackTrace();
        }
    }
}
