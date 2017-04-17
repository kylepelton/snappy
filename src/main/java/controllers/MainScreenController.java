package controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.text.Text;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainScreenController {
    private Stage primaryStage;
    private Stage secondaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setOnCloseRequest(e -> Platform.exit());
    }

    public void setSecondaryStage(Stage secondaryStage) {
        this.secondaryStage = secondaryStage;
    }

    @FXML protected void openTaggingScreen(ActionEvent event) {
        //TODO
    }

    @FXML protected void openAddPhotosScreen(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/addphotosscreen.fxml"));
            secondaryStage.setScene(new Scene(loader.load()));
            secondaryStage.setTitle("Add Photos");
            secondaryStage.show();

            AddPhotosScreenController controller = loader.getController();
            controller.setStage(secondaryStage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML protected void openApplicationSettingsScreen(ActionEvent event) {
        //TODO
    }

    @FXML protected void openDisplaySettingsScreen(ActionEvent event) {
        //TODO
    }

    @FXML protected void openHelpScreen(ActionEvent event) {
        //TODO
    }

}
