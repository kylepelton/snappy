package controller;

import java.util.ArrayList;
import java.util.Observable;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import edu.cmu.sphinx.api.SpeechResult;

import fxapp.SpeechRecognizer;
import model.PhotoManager;
import model.Photo;

/**
 * TaggingScreenController is the controller for the tagging screen
 *
 * This screen allows the user to go through the selected photos, view tags,
 * and edit tags as necessary
 */
public class TaggingScreenController extends Controller {

    @FXML private TextArea tagsArea;
    @FXML private Button previousButton;
    @FXML private Button nextButton;
    @FXML private ImageView preview;

    // Current index in the list of photos
    private int currentIndex;

    private ObservableList<Photo> photosToTag;

    private PhotoManager photoManager;

    @FXML
    private void initialize() {
        photoManager = PhotoManager.getInstance();
        currentIndex = 0;
    }

    /*
     * Handle SpeechRecognizer "updating"
     *
     * When SpeechRecognizer notifies the tagging screen of a change,
     * we should check if the text is a command for the screen or a
     * tag to be added to the given photo
     */
    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof SpeechRecognizer) {
            String tokens = (String)arg;
            // For now, only consider tags that are max 2 words long
            if (tokens.split(" ").length < 3) {
                if (tokens.equalsIgnoreCase("done")) {
                    // Exit the screen
                    onDonePress();
                } else if (tokens.equalsIgnoreCase("previous")) {
                    // Go to the previous photo
                    onPreviousPress();
                } else if (tokens.equalsIgnoreCase("continue")) {
                    // Go to the next photo
                    onNextPress();
                } else if (!tokens.equals("")) {
                    // Add this input as a tag to this photo
                    tagsArea.appendText(tokens + "\n");
                }
            }
        }
    }

    /*
     * Set the photosToTag instance variable
     */
    public void setPhotosToTag(ObservableList<Photo> photosToTag) {
        this.photosToTag = photosToTag;
        photoManager.setCurrentPhoto(photosToTag.get(currentIndex));
        updateView();
    }

    /*
     * Update the screen to hold pertinent info for the current photo
     */
    private void updateView() {
        // Only have Previous/Next buttons if more than one photo
        boolean buttonsVisible = photosToTag.size() > 1;

        stage.setTitle("Editing Tags (" + (currentIndex + 1) + "/" + photosToTag.size() + ")");

        previousButton.setVisible(buttonsVisible);
        nextButton.setVisible(buttonsVisible);

        preview.setImage(photoManager.getCurrentPhoto().getPreviewImg());
        tagsArea.clear();
        for (String tag : photoManager.getCurrentPhoto().getTags()) {
            tagsArea.setText(tagsArea.getText() + tag + "\n");
        }
        tagsArea.deleteText(tagsArea.getText().length() - 1, tagsArea.getText().length());
    }

    /*
     * Save the new set of tags for this photo
     */
    private void saveTags() {
        ArrayList<String> newTags = getTags();
        photoManager.getCurrentPhoto().setTags(newTags);
        photoManager.getCurrentPhoto().commitTagChanges();
    }

    /*
     * Get the new list of tags for this photo out of the tagging area
     */
    private ArrayList<String> getTags() {
        ArrayList<String> tags = new ArrayList<String>();
        String[] text = tagsArea.getText().split("\n");
        for (String tag : text) {
            String trimmedTag = tag.trim();
            if (!trimmedTag.isEmpty() && !tags.contains(trimmedTag.toLowerCase())) {
                tags.add(trimmedTag.toLowerCase());
            }
        }
        return tags;
    }

    /*
     * Go to the previous photo in the list of photos
     */
    @FXML
    private void onPreviousPress() {
        saveTags();
        currentIndex = (currentIndex - 1 < 0) ?
            photosToTag.size() - 1 : (currentIndex - 1) % photosToTag.size();
        photoManager.setCurrentPhoto(photosToTag.get(currentIndex));
        updateView();
    }

    /*
     * Go to the next photo in the list of photos
     */
    @FXML
    private void onNextPress() {
        saveTags();
        currentIndex = (currentIndex + 1) % photosToTag.size();
        photoManager.setCurrentPhoto(photosToTag.get(currentIndex));
        updateView();
    }

    /*
     * Save the changes and close the tagging screen
     */
    @FXML
    private void onDonePress() {
        saveTags();
        stage.close();
    }
}
