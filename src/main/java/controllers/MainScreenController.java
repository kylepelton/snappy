package controllers;

import java.io.File;
import java.util.Properties;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import model.Photo;
import model.PhotoManager;

public class MainScreenController extends Controller {
    private Stage primaryStage;
    private Stage secondaryStage;
    private Properties prop;
    @FXML private TilePane images;
    @FXML private Text untaggedPhotosText;

    private Controller openScreen(String screen, String header) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/" + screen + ".fxml"));
            secondaryStage.setScene(new Scene(loader.load()));
            secondaryStage.setTitle(header);
            // Update number of untagged photos text every time a popup screen closes
            secondaryStage.setOnHidden((e) -> {
                setUntaggedPhotosText();
            });
            secondaryStage.show();

            Controller controller = loader.getController();
            controller.setStage(secondaryStage);

            return controller;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void updatePhotos() {
        images.getChildren().clear();
        for (Photo photo : PhotoManager.getInstance().getPhotos()) {
            ImageView view = new ImageView(photo.getMainScreenImg());
            view.setPreserveRatio(true);
            view.setFitHeight(210);
            view.setFitWidth(230);
            view.setOnMouseClicked((e) -> {
                PhotoManager.getInstance().setCurrentPhoto(photo);
                openScreen("viewphotoscreen", photo.getName());
            });
            images.getChildren().add(view);
        }
        // Setup number of untagged photos upon loading up application
        setUntaggedPhotosText();
    }

    @FXML
    private void initialize() {
        PhotoManager.getInstance().getPhotos().addListener((ListChangeListener) (change) -> {
            updatePhotos();
        });
    }

    @FXML
    private void startTaggingPressed() {
        // Case where we want to just tag all photos
        TaggingScreenController taggingController =
            (TaggingScreenController) openScreen("taggingscreen", "Tagging Photos");
        taggingController.setPhotosToTag(PhotoManager.getInstance().getPhotos());
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setOnCloseRequest(e -> Platform.exit());
    }

    public void setSecondaryStage(Stage secondaryStage) {
        this.secondaryStage = secondaryStage;
    }

    public void setProperties(Properties prop) {
        this.prop = prop;
        PhotoManager.getInstance().setProperties(prop);
        if (PhotoManager.getInstance().hasPhotos()) {
            openScreen("loadingscreen", "Loading Photos");
        }
    }

    @FXML protected void openTaggingScreen(ActionEvent event) {
        //TODO
    }

    @FXML protected void openAddPhotosScreen(ActionEvent event) {
        openScreen("addphotosscreen", "Add Photos");
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

    // Update the "__ untagged photos" field in top right of main screen
    private void setUntaggedPhotosText() {
        int numUntagged = 0;
        for (Photo p : PhotoManager.getInstance().getPhotos()) {
            if (p.getTags() == null || p.getTags().isEmpty() || p.getTags().equals("")) {
                numUntagged++;
            }
        }
        untaggedPhotosText.setText("" + numUntagged + " photos untagged");
    }

}
