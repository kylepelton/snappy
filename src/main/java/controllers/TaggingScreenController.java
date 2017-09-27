package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import java.util.ArrayList;

import edu.cmu.sphinx.api.SpeechResult;
import model.PhotoManager;
import model.Photo;
import model.SpeechRecognizer;

public class TaggingScreenController extends Controller {

    @FXML private TextArea tagsArea;
    @FXML private Button previousButton;
    @FXML private Button nextButton;
    @FXML private ImageView preview;

    private int currentIndex;

    private ObservableList<Photo> photosToTag;

    private PhotoManager photoManager;
    private SpeechRecognizer speechRecognizer;
    private Thread speechThread;

    @FXML protected void initialize() {
        System.out.println("TEST");
        photoManager = PhotoManager.getInstance();
        // try {
        //     speechRecognizer = SpeechRecognizer.getInstance();
        //     speechRecognizer.startRecognition(true);
        //     SpeechResult result;
        //     while ((result = speechRecognizer.getResult()) != null) {
        //         String tokens = result.getHypothesis().trim();
        //         System.out.println(tokens);
        //         if (tokens.split(" ").length < 3) {
        //             Platform.runLater(new Runnable() {
        //                 @Override
        //                 public void run() {
        //                     if (tokens.equalsIgnoreCase("done")) {
        //                         onDonePress();
        //                     } else if (tokens.equalsIgnoreCase("previous")) {
        //                         onPreviousPress();
        //                     } else if (tokens.equalsIgnoreCase("next")) {
        //                         onNextPress();
        //                     } else {
        //                         tagsArea.appendText(tokens + "\n");
        //                     }
        //                 }
        //             });
        //         }
        //     }
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }
        speechThread.setDaemon(true);
        speechThread.start();
        currentIndex = 0;
    }

    public void onClose() {
        speechThread.wait();
    }

    public void setPhotosToTag(ObservableList<Photo> photosToTag) {
        this.photosToTag = photosToTag;
        photoManager.setCurrentPhoto(photosToTag.get(currentIndex));
        updateView();
    }

    private void updateView() {
        boolean buttonsVisible = photosToTag.size() > 1;

        previousButton.setVisible(buttonsVisible);
        nextButton.setVisible(buttonsVisible);

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
        currentIndex = (currentIndex - 1 < 0) ?
            photosToTag.size() - 1 : (currentIndex - 1) % photosToTag.size();
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
