package controllers;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;

public class InitConfigScreenController extends Controller {

    @FXML private Button selectDirButton;
    @FXML private Button confirmButton;
    @FXML private Label directoryField;
    @FXML private Label sizeField;

    private DirectoryChooser dirChooser;
    private String directory;
    private boolean confirmed = false;

    @FXML protected void onSelectDirButtonPress(ActionEvent event) {
        dirChooser = new DirectoryChooser();
        File photosdir = dirChooser.showDialog(stage);
        if (photosdir != null) {
            directory = photosdir.toString();
            directoryField.setText(directory);
            sizeField.setText("There are about " + Long.toString(photosdir.getFreeSpace() / 1073741824)
                + " gigabytes available for Snappy to use.");
            confirmButton.setDisable(false);
        }
    }

    @FXML protected void onConfirmButtonPress(ActionEvent event) {
        confirmed = true;
        stage.close();
    }

    @FXML protected void onCancelButtonPress(ActionEvent event) {
        stage.close();
    }

    public String getDirectory() {
        if (confirmed) {
            return directory;
        }
        return null;
    }
}
