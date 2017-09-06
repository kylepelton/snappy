package controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.PhotoManager;
import fxapp.LoadPhotosTask;
import javafx.scene.control.ProgressBar;

public class LoadingScreenController extends Controller {

    @FXML private ProgressBar progressBar;

    // Loads the saved photos and displays a progress bar
    private void loadPhotos() {
        progressBar.setProgress(0);
        LoadPhotosTask loadPhotosTask = new LoadPhotosTask(progressBar);
        new Thread(loadPhotosTask).start();
    }

    @FXML
    private void initialize() {
        loadPhotos();
    }
}
