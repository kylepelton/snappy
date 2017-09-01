package controllers;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.text.Text;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.PhotoManager;
import javafx.collections.ListChangeListener;
import javafx.scene.layout.TilePane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;
import java.util.Properties;
import model.Photo;

public class MainScreenController extends Controller {
    private Stage primaryStage;
    private Stage secondaryStage;
    private Properties prop;
    @FXML private TilePane images;

    private void openScreen(String screen, String header) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/" + screen + ".fxml"));
            secondaryStage.setScene(new Scene(loader.load()));
            secondaryStage.setTitle(header);
            secondaryStage.show();

            Controller controller = loader.getController();
            controller.setStage(secondaryStage);

        } catch (Exception e) {
            e.printStackTrace();
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
                openScreen("viewphotoscreen", photo.getFile().getName());
            });
            images.getChildren().add(view);
        }
    }

    @FXML
    private void initialize() {
        PhotoManager.getInstance().getPhotos().addListener((ListChangeListener) (change) -> {
            updatePhotos();
        });
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
        PhotoManager.getInstance().setProperties(this.prop);
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

}
