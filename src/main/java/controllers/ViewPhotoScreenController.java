package controllers;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import model.PhotoManager;
import model.Photo;

import java.awt.Desktop;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ViewPhotoScreenController extends Controller {

    @FXML private ImageView preview;
    @FXML private FlowPane tags;
    @FXML private Button addTagButton;
    @FXML private Button editTagsButton;
    @FXML private Button undoButton;
    @FXML private Button confirmChangesButton;
    @FXML private HBox buttonBox;
    private boolean editingTags;

    private void flipButtons() {
        if (editingTags) {
            editingTags = false;
            buttonBox.getChildren().remove(0);
            buttonBox.getChildren().remove(0);
            buttonBox.getChildren().remove(0);
            buttonBox.getChildren().add(0, editTagsButton);
        } else {
            editingTags = true;
            buttonBox.getChildren().remove(0);
            buttonBox.getChildren().add(0, undoButton);
            buttonBox.getChildren().add(0, addTagButton);
            buttonBox.getChildren().add(0, confirmChangesButton);
        }
    }

    @FXML
    private void initialize() {
        preview.setImage(PhotoManager.getInstance().getCurrentPhoto().getPreviewImg());
        updateTagView();
        tags.setAlignment(Pos.TOP_LEFT);
        buttonBox.getChildren().remove(0);
        buttonBox.getChildren().remove(0);
        buttonBox.getChildren().remove(0);
    }

    @FXML protected void onBackPress(ActionEvent event) {
        stage.close();
    }

    @FXML protected void onViewInFolderPress(ActionEvent event) {
        try {
            Desktop.getDesktop().open(PhotoManager.getInstance().getCurrentPhoto().getDirectory());
        } catch (IOException io) {
            System.out.println("File Not Found");
        };
    }

    @FXML protected void onEditTagsPress(ActionEvent event) {
        flipButtons();
        updateTagView();
    }

    @FXML protected void onAddTagPress(ActionEvent event) {
        addTagToView("        ");
    }

    @FXML protected void onUndoPress(ActionEvent event) {
        flipButtons();
        updateTagView();
    }

    @FXML protected void onConfirmChangesPress(ActionEvent event) {
        updateTagModel();
        flipButtons();
        updateTagView();
    }

    private void updateTagModel() {
        List<String> newTags = new ArrayList<>();
        for (Node n : tags.getChildren()) {
            if (n instanceof HBox) {
                HBox h = (HBox) n;
                Node text = h.getChildren().get(0);
                if (text instanceof Text) {
                    newTags.add(((Text)text).getText().trim());
                }
                if (text instanceof TextField) {
                    newTags.add(((TextField)text).getText().trim());
                }
            }
        }
        while (newTags.contains("")) {
            newTags.remove("");
        }
        PhotoManager.getInstance().getCurrentPhoto().setTags(newTags);
        PhotoManager.getInstance().getCurrentPhoto().commitTagChanges();
    }

    private void updateTagView() {
        tags.getChildren().clear();
        for (String tag : PhotoManager.getInstance().getCurrentPhoto().getTags()) {
            addTagToView(tag);
        }
    }

    private void addTagToView(String tag) {
        HBox tagView = new HBox();
        tagView.setStyle("-fx-background-color: #E0FFFF; "
                + "-fx-border-radius: 3;"
                + "-fx-border-color: #87CEFA;");
        ObservableList<Node> children = tagView.getChildren();
        children.add(new Text(tag));
        if (editingTags) {
            ImageView x = new ImageView(new Image("/icons/cancel.png", 10, 10, true, true));
            x.setOnMouseClicked((e) -> {
                tags.getChildren().remove(tagView);
            });
            tagView.setOnMouseClicked((e) -> {
                if (tagView.getChildren().get(0) instanceof Text) {
                    TextField tf = new TextField(((Text)tagView.getChildren().get(0)).getText().trim());
                    tagView.getChildren().remove(0);
                    tf.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                        if (newValue == false) {
                            tagView.getChildren().remove(0);
                            tagView.getChildren().add(0, new Text(tf.getText()));
                        }
                    });
                    tagView.getChildren().add(0, tf);
                }
            });
            children.add(x);
        }
        tags.getChildren().add(tagView);
    }
}
