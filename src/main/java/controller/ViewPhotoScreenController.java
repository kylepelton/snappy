package controller;

import java.awt.Desktop;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import model.PhotoManager;
import model.Photo;

/**
 * ViewPhotoScreenController is the controller for the View Photo screen
 *
 * This screen is opened upon clicking a photo in the main screen. On this
 * screen you can find important information about a photo, and you can
 * also delete a photo or edit its tags. This final option will take you
 * to the tagging screen
 */
public class ViewPhotoScreenController extends Controller {

    @FXML private ImageView preview;
    @FXML private Text name;
    @FXML private Text uploaded;
    @FXML private FlowPane tagsPane;

    /*
     * Initialize the controller. Just set the preview of the image along with the
     * image's tags, name, and date uploaded
     */
    @FXML
    private void initialize() {
        preview.setImage(PhotoManager.getInstance().getCurrentPhoto().getPreviewImg());
        updateTagView();
        name.setText("Name: " + PhotoManager.getInstance().getCurrentPhoto().getName());
        Date photoDate = new Date(PhotoManager.getInstance().getCurrentPhoto().getTimeAdded());
        uploaded.setText("Date Uploaded: " + photoDate.toString());
    }

    /*
     * Close this screen when "Done" is pressed
     */
    @FXML
    private void onDoneButtonPress(ActionEvent event) {
        stage.close();
    }

    /*
     * Open a new Graph Screen based on the photo currently being viewed
     */
    @FXML
    private void onGraphButtonPress(ActionEvent event) {
        mainScreen.createGraphForPhoto(PhotoManager.getInstance().getCurrentPhoto());
        stage.close();
    }

    /*
     * Switch to the tagging screen for this photo when "Edit Tags" is pressed
     */
    @FXML
    private void onEditTagsPress(ActionEvent event) {
        Controller controller = mainScreen.openScreen("taggingscreen", "Editing Tags");

        ObservableList<Photo> photos = FXCollections.observableArrayList();
        photos.add(PhotoManager.getInstance().getCurrentPhoto());

        ((TaggingScreenController) controller).setPhotosToTag(photos);
    }

    /*
     * Handle deleting this photo when "Delete Photo" is pressed
     */
    @FXML
    private void onDeletePhotoPress(ActionEvent event) {
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

    /*
     * Fill the tags area with the appropriate tags
     */
    private void updateTagView() {
        tagsPane.getChildren().clear();
        for (String tag : PhotoManager.getInstance().getCurrentPhoto().getTags()) {
            addTagToView(tag);
        }
    }

    /*
     * Create a blue box for each tag to go in
     */
    private void addTagToView(String tag) {
        Button newTag = new Button(tag);
        tagsPane.getChildren().add(newTag);
        newTag.setMouseTransparent(true);
        newTag.setStyle("-fx-background-color: skyblue");
    }
}
