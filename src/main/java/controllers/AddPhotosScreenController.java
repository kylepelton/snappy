package controllers;

import java.awt.Desktop;
import java.io.File;
import java.util.List;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.text.Text;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

public class AddPhotosScreenController {
    private Stage stage;
    private Desktop desktop = Desktop.getDesktop();
    private FileChooser fileChooser;
    private List<File> fileList;
    @FXML
    private Text dragarea;

    public void setStage(Stage stage) {
        this.stage = stage;
        fileChooser = new FileChooser();
        dragarea.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                if (db.hasFiles()) {
                    event.acceptTransferModes(TransferMode.COPY);
                } else {
                    event.consume();
                }
            }
        });

        dragarea.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasFiles()) {
                    success = true;
                    String filePath = null;
                    fileList = new ArrayList<>();
                    for (File file:db.getFiles()) {
                        fileList.add(file);
                    }
                }
                event.setDropCompleted(success);
                event.consume();
            }
        });
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
