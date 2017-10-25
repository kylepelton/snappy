package controllers;

import java.io.File;
import java.util.Properties;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import fxapp.SpeechRecognizer;
import model.Photo;
import model.PhotoManager;

public class MainScreenController extends Controller {
    private Stage primaryStage;
    private Stage secondaryStage;
    private Properties prop;
    private SpeechRecognizer speechRecognizer;
    private boolean voiceControlToggledOn = false;
    private boolean multiSelectToggledOn = false;
    @FXML private TilePane images;
    @FXML private Text untaggedPhotosText;
    @FXML private Button multiSelectButton;
    @FXML private Button voiceControlButton;
    @FXML private Text voiceControlText;
    @FXML private Circle voiceIndicator;

    private Controller openScreen(String screen, String header) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/" + screen + ".fxml"));
            secondaryStage.setScene(new Scene(loader.load()));
            secondaryStage.setTitle(header);
            // Update number of untagged photos text every time a popup screen closes
            secondaryStage.setOnHidden((e) -> {
                setUntaggedPhotosText();
            });
            secondaryStage.show();

            Controller controller = loader.getController();
            controller.setStage(secondaryStage);

            // Allow this controller to listen to speech events
            if (speechRecognizer != null) {
                speechRecognizer.addObserver(controller);
            }

            return controller;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void updatePhotos() {
        images.getChildren().clear();
        for (Photo photo : PhotoManager.getInstance().getPhotos()) {
            ImageView view = new ImageView(photo.getMainScreenImg());
            view.setPreserveRatio(true);
            view.setFitHeight(210);
            view.setFitWidth(230);
            view.setOnMouseClicked((e) -> {
                PhotoManager.getInstance().setCurrentPhoto(photo);
                openScreen("viewphotoscreen", photo.getName());
            });
            images.getChildren().add(view);
        }
        // Setup number of untagged photos upon loading up application
        setUntaggedPhotosText();
    }

    @FXML
    private void initialize() {
        PhotoManager.getInstance().getPhotos().addListener((ListChangeListener) (change) -> {
            updatePhotos();
        });
        Thread speechThread = new Thread() {
            public void run() {
                try {
                    speechRecognizer = new SpeechRecognizer();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        speechThread.setDaemon(true);
        speechThread.start();
    }

    @FXML
    private void onAllPhotosPress() {
        // Case where we want to just tag all photos
        ObservableList<Photo> photos = PhotoManager.getInstance().getPhotos();
        if (photos.size() == 0) {
            noPhotosMessage("You do not have any photos.");
            return;
        }
        TaggingScreenController taggingController =
            (TaggingScreenController) openScreen("taggingscreen", "Tagging Photos");
        taggingController.setPhotosToTag(photos);
    }

    @FXML
    private void onUntaggedPhotosPress() {
        ObservableList<Photo> photos = PhotoManager.getInstance().getUntaggedPhotos();
        if (photos.size() == 0) {
            noPhotosMessage("All existing photos have at least one tag.");
            return;
        }
        TaggingScreenController taggingController =
            (TaggingScreenController) openScreen("taggingscreen", "Tagging Photos");
        taggingController.setPhotosToTag(photos);
    }

    private void noPhotosMessage(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING,
                message,
                javafx.scene.control.ButtonType.OK);
            alert.setTitle("Attention");
            alert.setHeaderText("No Photos to Tag");
            java.util.Optional<javafx.scene.control.ButtonType> result = alert.showAndWait();
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setOnCloseRequest(e -> Platform.exit());
    }

    public void setSecondaryStage(Stage secondaryStage) {
        this.secondaryStage = secondaryStage;
    }

    public void setProperties(Properties prop) {
        this.prop = prop;
        PhotoManager.getInstance().setProperties(prop);
        if (PhotoManager.getInstance().hasPhotos()) {
            openScreen("loadingscreen", "Loading Photos");
        }
    }

    @FXML protected void onVoiceControlToggle(ActionEvent event) {
        if (speechRecognizer == null) {
            return;
        }
        if (!voiceControlToggledOn) {
            speechRecognizer.startRecognition();
            voiceControlToggledOn = true;
        } else {
            speechRecognizer.stopRecognition();
            voiceControlToggledOn = false;
        }
        setVoiceControlIndicators();
    }

    @FXML protected void onMultiSelectToggle(ActionEvent event) {
        if (!multiSelectToggledOn) {
            multiSelectToggledOn = true;
        } else {
            multiSelectToggledOn = false;
        }
        setMultiSelectIndicators();
    }

    @FXML protected void openAddPhotosScreen(ActionEvent event) {
        openScreen("addphotosscreen", "Add Photos");
    }

    // Update the "__ untagged photos" field in top right of main screen
    private void setUntaggedPhotosText() {
        int numUntagged = PhotoManager.getInstance().getUntaggedPhotos().size();
        //for (Photo p : PhotoManager.getInstance().getPhotos()) {
        //    if (p.getTags() == null || p.getTags().isEmpty() || p.getTags().equals("")) {
        //        numUntagged++;
        //    }
        //}
        if (numUntagged == 1) {
            untaggedPhotosText.setText(numUntagged + " photo untagged");
        } else {
            untaggedPhotosText.setText(numUntagged + " photos untagged");
        }
    }

    private void setVoiceControlIndicators() {
        if (!voiceControlToggledOn) {
            voiceControlButton.setText("Enable Voice Control");
            voiceControlText.setText("Voice Control is Disabled");
            voiceIndicator.setFill(Color.valueOf("#dadada"));
        } else {
            voiceControlButton.setText("Disable Voice Control");
            voiceControlText.setText("Voice Control is Enabled");
            voiceIndicator.setFill(Color.valueOf("#ff0000"));
        }
    }

    private void setMultiSelectIndicators() {
        if (!multiSelectToggledOn) {
            multiSelectButton.setText("Select Multiple");
        } else {
            multiSelectButton.setText("Stop Selecting");
        }
    }
}
