package controller;

import java.io.File;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;

/**
 * InitConfigScreenController is the controller for the initial screen in the application
 *
 * This screen only comes up when the user first opens the applications and lets the user
 * choose where they want to set the image metadata directory (the snappy_photos folder)
 */
public class InitConfigScreenController extends Controller {

    @FXML private Button selectDirButton;
    @FXML private Button confirmButton;
    @FXML private Label directoryField;
    @FXML private Label sizeField;

    private DirectoryChooser dirChooser;
    private String directory;

    /*
     * Set the default directory sto the user's home directory
     */
    @FXML
    private void initialize() {
        String home = System.getProperty("user.home");
        directoryField.setText(home);
        directory = home;
    }

    /*
     * Change the selected directory to the location the user selects
     */
    @FXML
    public void onSelectDirButtonPress(ActionEvent event) {
        //Changes selected directory to whichever location user selects
        dirChooser = new DirectoryChooser();
        File photosdir = dirChooser.showDialog(stage);
        if (photosdir != null) {
            directory = photosdir.toString();
            directoryField.setText(directory);
            confirmButton.setDisable(false);
        }
    }

    /*
     * Check that snappy_photos doesn't already exist and close screen upon "Confirm" being pressed
     */
    @FXML
    public void onConfirmButtonPress(ActionEvent event) {
        if (new File(directory + System.getProperty("file.separator") + "snappy_photos").exists()) {
            // Alert user that this directory already has a snappy_photos folder
            Alert alert = new Alert(Alert.AlertType.WARNING,
                "The snappy_photos folder already exists. Click \"Finish\" to use the "
                + "existing folder and all of its contents. Click \"Cancel\" to select "
                + "a different folder location.",
                ButtonType.CANCEL,
                ButtonType.FINISH);
            alert.setTitle("Attention");
            alert.setHeaderText("Folder Already Exists");
            Optional<ButtonType> result = alert.showAndWait();
            System.out.println(result.get());
            if (result.get() == ButtonType.FINISH) {
                stage.close();
            }
        } else {
            stage.close();
        }
    }

    /*
     * Exit upon "Cancel" being pressed
     */
    @FXML
    public void onCancelButtonPress(ActionEvent event) {
        System.exit(0);
    }

    /*
     * Return the directory chosen for snappy_photos to Main
     */
    public String getDirectory() {
        return directory;
    }
}