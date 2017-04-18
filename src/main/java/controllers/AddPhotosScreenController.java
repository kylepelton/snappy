package controllers;

import java.awt.Desktop;
import java.io.File;
import java.util.List;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.layout.TilePane;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import javafx.scene.layout.HBox;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javax.imageio.ImageIO;
import java.io.IOException;
import javafx.scene.Node;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressBar;

import model.PhotoManager;

public class AddPhotosScreenController extends Controller {
    private Desktop desktop = Desktop.getDesktop();
    private FileChooser fileChooser;
    private List<File> fileList;
    private List<File> toAdd;
    @FXML private Pane dragarea;
    @FXML private HBox browse;
    @FXML private HBox addmore;
    @FXML private HBox images;
    @FXML private ScrollPane display;
    @FXML private Button cancelButton;
    @FXML private Text confirm;
    @FXML private ImageView check;
    @FXML private Button addPhotosButton;
    @FXML private Pane loading;
    @FXML private ProgressBar progress;

    @FXML
    private void initialize() {
        fileList = new ArrayList<>();
        toAdd = new ArrayList<>();
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
                fileList = new ArrayList<>();
                if (db.hasFiles()) {
                    success = true;
                    for (File file : db.getFiles()) {
                        fileList.add(file);
                    }
                    showToAdd();
                }
                event.setDropCompleted(success);
                event.consume();
            }
        });
    }

    private void fadeOut(Node node, int millis) {
        FadeTransition out = new FadeTransition(Duration.millis(millis), node);
        out.setFromValue(1.0);
        out.setToValue(0.0);
        out.play();
    }

    private void fadeIn(Node node, int millis) {
        FadeTransition in = new FadeTransition(Duration.millis(millis), node);
        in.setFromValue(0.0);
        in.setToValue(1.0);
        in.play();
    }

    private void fadeOut(Node node) {
        fadeOut(node, 500);
    }

    private void fadeIn(Node node) {
        fadeIn(node, 500);
    }

    private void showAddMore() {
        browse.setOpacity(1.0);
        dragarea.setOpacity(1.0);
        browse.setDisable(false);
        dragarea.setDisable(false);
        addmore.setOpacity(0.0);
        addmore.setDisable(true);
        display.setVisible(false);
        cancelButton.setVisible(true);
        fileList = new ArrayList<>();
    }

    private void showToAdd() {
        if (fileList == null || fileList.size() == 0) {
            return;
        }
        fadeIn(loading);
        loading.setDisable(false);
        loading.setVisible(true);
        browse.setOpacity(0.0);
        dragarea.setOpacity(0.0);
        browse.setDisable(true);
        dragarea.setDisable(true);
        final ArrayList<File> checkList = new ArrayList<>();
        checkList.addAll(fileList);
        Task task = new Task<Void>() {
            @Override public Void call() {
                boolean error;
                int i = 0;
                for (File file : checkList) {
                    boolean valid = !toAdd.contains(file);
                    try {
                        if (ImageIO.read(file) != null) {
                            valid = true;
                        } else {
                            error = true;
                        }
                    } catch(IOException ex) {
                        valid = false;
                        error = true;
                    }
                    if (valid) {
                        toAdd.add(0, file);
                    }
                    i++;
                    updateProgress(i, fileList.size());
                }
                return null;
            }

            @Override protected void succeeded() {
                boolean error = false;
                loading.setOpacity(0.0);
                loading.setVisible(false);
                loading.setDisable(true);
                if (toAdd.size() > 0) {
                    addPhotosButton.setDisable(false);
                    addmore.setDisable(false);
                    fadeIn(addmore);
                    display.setVisible(true);
                    images.getChildren().clear();
                    for (File file : toAdd) {
                        Pane p = new Pane();
                        ImageView view = new ImageView(new Image(file.toURI().toString(), 320, 190, true, true));
                        view.setPreserveRatio(true);
                        view.setFitHeight(190);
                        view.setFitWidth(320);
                        ImageView x = new ImageView(new Image("/icons/cancel.png"));
                        x.setPreserveRatio(true);
                        x.setFitHeight(30);
                        x.setOpacity(0.0);
                        view.setOnMouseEntered((e) -> {
                            fadeIn(x, 100);
                        });
                        view.setOnMouseExited((e) -> {
                            x.setOpacity(0.0);
                        });
                        view.setOnMouseClicked((e) -> {
                            toAdd.remove(file);
                            fileList = new ArrayList<>();
                            images.getChildren().remove(p);
                            if (toAdd.size() == 1) {
                                confirm.setText(toAdd.size() + " photo ready to add!");
                            } else {
                                confirm.setText(toAdd.size() + " photos ready to add!");
                            }
                        });
                        p.getChildren().add(view);
                        p.getChildren().add(x);
                        images.getChildren().add(p);
                    }
                    fadeIn(images);
                    if (error) {
                        check.setImage(new Image("/icons/exclamation.png"));
                        confirm.setText("Please only select image files.");
                    } else {
                        check.setImage(new Image("/icons/accept.png"));
                        if (toAdd.size() == 1) {
                            confirm.setText(toAdd.size() + " photo ready to add!");
                        } else {
                            confirm.setText(toAdd.size() + " photos ready to add!");
                        }
                    }
                } else {
                    showAddMore();
                }
            }
        };
        progress.progressProperty().bind(task.progressProperty());
        new Thread(task).start();
        boolean error = false;
    }

    @FXML protected void onBrowsePress(ActionEvent event) {
        fileList = fileChooser.showOpenMultipleDialog(stage);
        showToAdd();
    }

    @FXML protected void onAddPhotosPress(ActionEvent event) {
        //TODO
        try {
            PhotoManager.getInstance().addPhotos(toAdd);
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML protected void onCancelPress(ActionEvent event) {
        stage.close();
    }

    @FXML protected void onAddCancelPress(ActionEvent event) {
        showToAdd();
    }

    @FXML protected void onAddMorePhotosPress(ActionEvent event) {
        showAddMore();
    }

}
