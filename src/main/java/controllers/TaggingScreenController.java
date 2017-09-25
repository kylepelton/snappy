package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.collections.ObservableList;
import java.util.ArrayList;

import model.PhotoManager;
import model.Photo;

public class TaggingScreenController extends Controller {

    @FXML private TextArea tagsArea;
    @FXML private Button previousButton;
    @FXML private Button nextButton;
    @FXML private ImageView preview;

    private int currentIndex;

    private ObservableList<Photo> photosToTag;

    private PhotoManager photoManager;

    @FXML protected void initialize() {
        photoManager = PhotoManager.getInstance();
        photosToTag = PhotoManager.getInstance().getPhotos();
        currentIndex = 0;
        photoManager.setCurrentPhoto(photosToTag.get(currentIndex));
        updateView();
    }

    private void updateView() {
        preview.setImage(photoManager.getCurrentPhoto().getPreviewImg());
        tagsArea.clear();
        for (String tag : photoManager.getCurrentPhoto().getTags()) {
            tagsArea.setText(tagsArea.getText() + tag + "\n");
        }
    }

    private void saveTags() {
        ArrayList<String> newTags = getTags();
        photoManager.getCurrentPhoto().setTags(newTags);
        photoManager.getCurrentPhoto().commitTagChanges();
    }

    private ArrayList<String> getTags() {
        ArrayList<String> tags = new ArrayList<String>();
        String[] text = tagsArea.getText().split("\n");
        for (String tag : text) {
            if (!tag.trim().equals("")) {
                tags.add(tag.trim().toLowerCase());
            }
        }
        return tags;
    }

    @FXML protected void onPreviousPress() {
        saveTags();
        currentIndex = (currentIndex - 1) % photosToTag.size();
        photoManager.setCurrentPhoto(photosToTag.get(currentIndex));
        updateView();
    }

    @FXML protected void onNextPress() {
        saveTags();
        currentIndex = (currentIndex + 1) % photosToTag.size();
        photoManager.setCurrentPhoto(photosToTag.get(currentIndex));
        updateView();
    }

    @FXML protected void onDonePress() {
        saveTags();
        stage.close();
    }
}