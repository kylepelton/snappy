package controller;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.imageio.ImageIO;

import model.PhotoManager;

/**
 * AddPhotosScreenController is the controller for the Add Photos screen
 *
 * As its name suggests, this screen allows a user to add as many photos
 * as they desire along with group tags for these images
 */
public class AddPhotosScreenController extends Controller {
    private Desktop desktop = Desktop.getDesktop();
    private FileChooser fileChooser;
    private List<File> fileList;
    private List<File> toAdd;
    // The list of accepted image formats for Snappy
    private static final String[] IMAGE_FORMATS =
        {"*.png", "*.jpg", "*.jps", "*.gif"};
    @FXML private Pane dragArea;
    @FXML private HBox browse;
    @FXML private HBox addMore;
    @FXML private HBox images;
    @FXML private ScrollPane display;
    @FXML private Button cancelButton;
    @FXML private Text confirm;
    @FXML private ImageView check;
    @FXML private Button addPhotosButton;
    @FXML private Pane loading;
    @FXML private ProgressBar progress;
    @FXML private TextField tagBox;

    /*
     * Initialize this screen. See comments below for more details
     */
    @FXML
    private void initialize() {
        fileList = new ArrayList<>();
        toAdd = new ArrayList<>();
        fileChooser = new FileChooser();
        // Filter for files of our accepted extensions
        ExtensionFilter filter = new ExtensionFilter("Image Extensions",
                Arrays.asList(IMAGE_FORMATS));
        fileChooser.getExtensionFilters().add(filter);
        // Handle user dragging a photo to be added over the adding area
        dragArea.setOnDragOver(new EventHandler<DragEvent>() {
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
        // Handle user dropping a photo in the adding area. Add the photo
        // to the list of photos to add
        dragArea.setOnDragDropped(new EventHandler<DragEvent>() {
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
        dragArea.setOpacity(1.0);
        browse.setDisable(false);
        dragArea.setDisable(false);
        addMore.setOpacity(0.0);
        addMore.setDisable(true);
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
        dragArea.setOpacity(0.0);
        browse.setDisable(true);
        dragArea.setDisable(true);
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
                    } catch (IOException ex) {
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
                    addMore.setDisable(false);
                    fadeIn(addMore);
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
                        x.setOnMouseClicked((e) -> {
                            toAdd.remove(file);
                            fileList = new ArrayList<>();
                            images.getChildren().remove(p);
                            if (toAdd.size() == 1) {
                                confirm.setText(toAdd.size() + " photo ready to add!");
                            } else {
                                confirm.setText(toAdd.size() + " photos ready to add!");
                            }
                        });
                        view.setOnMouseEntered((e) -> {
                            fadeIn(x, 100);
                        });
                        view.setOnMouseExited((e) -> {
                            x.setOpacity(0.0);
                        });
                        view.setOnMouseClicked(x.getOnMouseClicked());
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

    /*
     * Add the photos that have been selected to the app upon clicking "Add Photos"
     */
    @FXML
    private void onAddPhotosPress(ActionEvent event) {
        try {
            List<String> tags = new ArrayList<>(Arrays.asList(tagBox.getText().split(",")));
            for (int i = 0; i < tags.size(); i++) {
                tags.set(i, tags.get(i).trim());
            }
            while (tags.contains("")) {
                tags.remove("");
            }
            PhotoManager.getInstance().savePhotos(toAdd, tags);
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * Close the screen upon clicking "Cancel"
     */
    @FXML
    private void onCancelPress(ActionEvent event) {
        stage.close();
    }

    @FXML
    private void onAddCancelPress(ActionEvent event) {
        showToAdd();
    }

    
    @FXML
    private void onAddMorePhotosPress(ActionEvent event) {
        showAddMore();
    }

}
