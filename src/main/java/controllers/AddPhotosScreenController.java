package controllers;

import java.awt.Desktop;
import java.io.File;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.text.Text;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class AddPhotosScreenController {
    private Stage stage;
    private Desktop desktop = Desktop.getDesktop();
    private FileChooser fileChooser;
    private List<File> fileList;

    public void setStage(Stage stage) {
        this.stage = stage;
        fileChooser = new FileChooser();
    }
    @FXML protected void onBrowsePress(ActionEvent event) {
        fileList = fileChooser.showOpenMultipleDialog(stage);
    }

    @FXML protected void onAddPhotosPress(ActionEvent event) {
        //TODO
        try {
            if (fileList != null && !fileList.isEmpty()) {
                for (File file : fileList) {
                    desktop.open(file);
                    System.out.println(file);
                    System.out.println(file.getName());
                }
                stage.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML protected void onCancelPress(ActionEvent event) {
        stage.close();
    }

}
