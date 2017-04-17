package fxapp;

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
        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/mainscreen.fxml"));

            primaryStage.setTitle("Snappy");
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.show();

            MainScreenController controller = loader.getController();
            Stage secondaryStage = new Stage();
            controller.setPrimaryStage(primaryStage);
            controller.setSecondaryStage(secondaryStage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
