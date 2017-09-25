package controllers;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import model.PhotoManager;
import model.Photo;
import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import java.awt.Desktop;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXMLLoader;
import controllers.Controller;

public class ViewPhotoScreenController extends Controller {

    @FXML private ImageView preview;
    @FXML private Text name;
    @FXML private Text taken;
    @FXML private Text uploaded;
    @FXML private TilePane tagsPane;

    @FXML
    private void initialize() {
        preview.setImage(PhotoManager.getInstance().getCurrentPhoto().getPreviewImg());
        updateTagView();
        name.setText("Name: " + PhotoManager.getInstance().getCurrentPhoto().getName());
    }

    @FXML protected void onDoneButtonPress(ActionEvent event) {
        stage.close();
    }

    @FXML protected void onEditTagsPress(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/taggingscreen.fxml"));

            stage.setTitle("Editing Tags");
            stage.setScene(new Scene(loader.load()));
            Controller taggingController = loader.getController();
            taggingController.setStage(stage);
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
    }

    @FXML public void onDeletePhotoPress(ActionEvent event) {
        String currentPhotoName = PhotoManager.getInstance().getCurrentPhoto().getName();

        // Trigger confirmation message that you want to delete this photo
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
            "Are you sure you want to permanently delete " + currentPhotoName + "?", ButtonType.YES, ButtonType.NO);
        alert.initOwner(stage);
        alert.setHeaderText("Permanently delete this photo?");
        alert.showAndWait();

        // If confirmed, then delete the photo and launch confirmation popup window
        if (alert.getResult() == ButtonType.YES) {
            PhotoManager.getInstance().deletePhoto();
            alert = new Alert(Alert.AlertType.NONE, "Photo Deleted", ButtonType.OK);
            alert.initOwner(stage);
            alert.setHeaderText("Successfully Deleted");
            alert.setContentText(PhotoManager.getInstance().getCurrentPhoto().getName()
                + " has been successfully deleted");
            alert.showAndWait();
            stage.close();
        }
    }

    private void updateTagView() {
        tagsPane.getChildren().clear();
        for (String tag : PhotoManager.getInstance().getCurrentPhoto().getTags()) {
            addTagToView(tag);
        }
    }

    private void addTagToView(String tag) {
        Button newTag = new Button(tag);
        tagsPane.getChildren().add(newTag);
        newTag.setMouseTransparent(true);
        newTag.setStyle("-fx-background-color: skyblue");
    }
}
