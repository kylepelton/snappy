package controllers;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;

public class InitConfigScreenController extends Controller {

    @FXML private Button selectDirButton;
    @FXML private Button confirmButton;
    @FXML private Label directoryField;
    @FXML private Label sizeField;

    private DirectoryChooser dirChooser;
    private String directory;

    @FXML protected void initialize() {
        //Sets default directory to user's home directory
        String home = System.getProperty("user.home");
        directoryField.setText(home);
        directory = home;
    }

    @FXML protected void onSelectDirButtonPress(ActionEvent event) {
        //Changes selected directory to whichever location user selects
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
        if (new File(directory + System.getProperty("file.separator") + "snappy_photos").exists()) {
            Alert alert = new Alert(Alert.AlertType.WARNING,
                "The snappy_photos folder already exists. Click \"Finish\" to use the existing folder and all of its contents. Click \"Cancel\" to select a different folder location.",
                javafx.scene.control.ButtonType.CANCEL,
                javafx.scene.control.ButtonType.FINISH);
            //alert.setOnCloseRequest(EventHandler<DialogEvent> event -> stage.close());
            alert.setTitle("Attention");
            alert.setHeaderText("Folder Already Exists");
            java.util.Optional<javafx.scene.control.ButtonType> result = alert.showAndWait();
            System.out.println(result.get());
            if (result.get() == ButtonType.FINISH) {
                stage.close();
            }
        } else {
            stage.close();
        }
    }

    @FXML protected void onCancelButtonPress(ActionEvent event) {
        System.exit(0);
    }

    public String getDirectory() {
        return directory;
    }
}
