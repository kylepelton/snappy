package controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import model.PhotoManager;
import java.io.IOException;
import java.awt.Desktop;

public class ViewPhotoScreenController extends Controller {

    @FXML private ImageView preview;

    @FXML
    private void initialize() {
        preview.setImage(
            new Image(PhotoManager.getInstance().getCurrentPhoto().toURI().toString(),
                600, 450, true, true));
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML protected void onBackPress(ActionEvent event) {
        stage.close();
    }

    @FXML protected void onViewInFolderPress(ActionEvent event) {
        try {
            Desktop.getDesktop().open(PhotoManager.getInstance().getCurrentPhoto().getParentFile());
        } catch (IOException io) {
            System.out.println("File Not Found");
        };
    }
}