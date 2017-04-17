package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.text.Text;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AddPhotosScreenController {
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    @FXML protected void onBrowsePress(ActionEvent event) {
        //TODO
    }

    @FXML protected void onAddPhotosPress(ActionEvent event) {
        stage.close();
    }

    @FXML protected void onCancelPress(ActionEvent event) {
        //TODO
    }

}
