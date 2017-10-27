package controller;

import fxapp.LoadPhotosTask;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ProgressBar;
import javafx.scene.Scene;
import javafx.stage.Stage;

import model.PhotoManager;

/**
 * LoadingScreenController is the controller for the screen which notifies users that
 * their photos are still loading into the application
 */
public class LoadingScreenController extends Controller {

    @FXML private ProgressBar progressBar;

    /*
     * Calls loadPhotos() upon JavaFX/FXML initializing this controller/screen
     */
    @FXML
    private void initialize() {
        loadPhotos();
    }

    /*
     * Load the saved photos and display a progress bar
     */
    private void loadPhotos() {
        progressBar.setProgress(0);
        LoadPhotosTask loadPhotosTask = new LoadPhotosTask(progressBar);
        new Thread(loadPhotosTask).start();
    }
}