package controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Properties;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.Tab;
import javafx.scene.control.SingleSelectionModel;

import fxapp.SpeechRecognizer;
import model.Photo;
import model.PhotoManager;

import com.fxgraph.graph.Graph;
import com.fxgraph.graph.Model;
import com.fxgraph.layout.base.Layout;
import com.fxgraph.layout.aesthetic.AestheticLayout;
import com.fxgraph.graph.CellType;

/**
 * MainScreenController is the controller for the main screen of the application
 *
 * All other screens are opened as secondary screens from this screen
 */

public class MainScreenController extends Controller {
    // The main screen's stage
    private Stage primaryStage;
    // Any popup window is set to this stage
    private Stage secondaryStage;
    // The properties of the local copy of this application
    private Properties prop;
    // The instance of SpeechRecognizer for this application
    private SpeechRecognizer speechRecognizer;
    // Controls whether voice control is running
    private boolean voiceControlToggledOn = false;
    // Controls whether multiselect is on
    private boolean multiSelectToggledOn = false;
    // The list of multiselected photos
    private ObservableList<Photo> selectedPhotos;

    // The grid of images in the main screen
    @FXML private TilePane images;
    // The "_ untagged photos" text
    @FXML private Text untaggedPhotosText;
    // Icon that changes based on the state of multiselect
    @FXML private ImageView multiSelectIndicator;
    // The button that turns multiselect on/off
    @FXML private Button multiSelectButton;
    // The "_ photos selected" text
    @FXML private Text photoSelectionText;
    // The button that turns voice control on/off
    @FXML private Button voiceControlButton;
    // Text that changes based on state of voice control
    @FXML private Text voiceControlText;
    // Turns red if voice control on, gray if off
    @FXML private Circle voiceIndicator;
    @FXML private TabPane tabPane;

    /*
     * Initializes this main screen
     * This is called as the FXML Loader is loading this screen
     */
    @FXML
    private void initialize() {
        // If a change occurs, update the grid of photos
        PhotoManager.getInstance().getPhotos().addListener((ListChangeListener) (change) -> {
            updatePhotos();
        });
        // Setup the thread on which speech recognition runs
        selectedPhotos = FXCollections.observableList(new ArrayList<Photo>());
        selectedPhotos.addListener((ListChangeListener) (change) -> {
            int numSelected = selectedPhotos.size();
            if (numSelected == 1) {
                photoSelectionText.setText(numSelected + " photo selected");
            } else {
                photoSelectionText.setText(numSelected + " photos selected");
            }
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

    /*
     * Open the passed in screen as a popup window from the main screen
     */
    private Controller openScreen(String screen, String header) {
        try {
            // Load this new screen
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
            if (screen.equals("viewphotoscreen")) {
                ((ViewPhotoScreenController) controller).setMainScreenController(this);
            }
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

    public void createGraphForPhoto(Photo photo) {
        //Create and add new tab for new graph view
        Graph graph = new Graph();
        Tab tab = new Tab();
        tab.setText(photo.getName());
        tabPane.getTabs().add(tab);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(graph.getScrollPane());

        tab.setContent(borderPane);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
        tab.setClosable(true);

        Model model = graph.getModel();
        graph.beginUpdate();

        model.addImageCell("0", photo.getPreviewImg());

        int num = 1;
        ObservableList<Photo> related = PhotoManager.getInstance().getRelatedPhotos(photo);
        for (Photo relatedPhoto : related) {
            if (!relatedPhoto.equals(photo)) {
                model.addImageCell(Integer.toString(num), relatedPhoto.getPreviewImg());
                model.addEdge("0", Integer.toString(num));
            }
        }

        SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
        selectionModel.select(tab);

        graph.endUpdate();
        Layout layout = new AestheticLayout(graph);
        layout.execute();
    }

    /*
     * Update the photos in the grid of images on this screen
     */
    private void updatePhotos() {
        images.getChildren().clear();

        for (Photo photo : PhotoManager.getInstance().getPhotos()) {
            ImageView view = new ImageView(photo.getMainScreenImg());
            view.setPreserveRatio(true);
            view.setFitHeight(210);
            view.setFitWidth(230);
            if (!multiSelectToggledOn) {
                view.setOnMouseClicked((e) -> {
                    PhotoManager.getInstance().setCurrentPhoto(photo);
                    openScreen("viewphotoscreen", photo.getName());
                });
            } else {
                selectedPhotos.clear();
                view.setOnMouseClicked((e) -> {
                    if(!selectedPhotos.contains(photo)) {
                        selectedPhotos.add(photo);
                        view.setStyle("-fx-effect: innershadow(one-pass-box, #039ed3, 30, 1, 0, 0);");
                    } else {
                        selectedPhotos.remove(photo);
                        view.setStyle("");
                    }
                });
            }
            images.getChildren().add(view);
        }
        // Setup number of untagged photos upon loading up application
        setUntaggedPhotosText();
    }

    /*
     * Open the tagging screen in the case where we want to tag all photos
     */
    @FXML
    private void onTagAllPhotosPress() {
        ObservableList<Photo> photos = PhotoManager.getInstance().getPhotos();
        if (photos.size() == 0) {
            // If user has no photos, don't open the screen
            noPhotosMessage("You do not have any photos.");
            return;
        }
        TaggingScreenController taggingController =
            (TaggingScreenController) openScreen("taggingscreen", "Tagging Photos");
        taggingController.setPhotosToTag(photos);
    }

    /*
     * Open the tagging screen in the case where we want to tag only untagged photos
     */
    @FXML
    private void onTagUntaggedPhotosPress() {
        ObservableList<Photo> photos = PhotoManager.getInstance().getUntaggedPhotos();
        if (photos.size() == 0) {
            noPhotosMessage("All existing photos have at least one tag.");
            return;
        }
        TaggingScreenController taggingController =
            (TaggingScreenController) openScreen("taggingscreen", "Tagging Photos");
        taggingController.setPhotosToTag(photos);
    }

    /*
     * Open the tagging screen in the case where we want to tag only selected photos
     */
    @FXML
    private void onTagSelectedPhotosPress() {
        if (selectedPhotos.size() == 0) {
            noPhotosMessage("No photos are currently selected.");
            return;
        }
        TaggingScreenController taggingController =
            (TaggingScreenController) openScreen("taggingscreen", "Tagging Photos");
        taggingController.setPhotosToTag(selectedPhotos);
    }

    /*
     * Generate error message if tagging screen wasn't opened for some reason
     */
    private void noPhotosMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING,
                message,
                ButtonType.OK);
        alert.setTitle("Attention");
        alert.setHeaderText("No Photos to Tag");
        Optional<ButtonType> result = alert.showAndWait();
    }

    /*
     * Set the "primary" stage for this controller
     * This stage is the stage for the main screen
     */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setOnCloseRequest(e -> Platform.exit());
    }

    /*
     * Set the "secondary" stage for this controller
     * This stage is for any popup window we open from main screen
     */
    public void setSecondaryStage(Stage secondaryStage) {
        this.secondaryStage = secondaryStage;
    }

    /*
     * Set the saved properties for this application
     */
    public void setProperties(Properties prop) {
        this.prop = prop;
        PhotoManager.getInstance().setProperties(prop);
        if (PhotoManager.getInstance().hasPhotos()) {
            openScreen("loadingscreen", "Loading Photos");
        }
    }

    /*
     * Turn voice control on/off
     */
    @FXML
    private void onVoiceControlToggle(ActionEvent event) {
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

    /*
     * Turn multiselect on/off
     */
    @FXML
    private void onMultiSelectToggle(ActionEvent event) {
        if (!multiSelectToggledOn) {
            multiSelectToggledOn = true;
        } else {
            selectedPhotos.clear();
            multiSelectToggledOn = false;
        }
        updatePhotos();
        setMultiSelectIndicators();
    }

    /*
     * Delete selected photos
     */
    @FXML
    private void onDeleteSelectedPress(ActionEvent event) {
        int numSelected = selectedPhotos.size();
        if (numSelected == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING,
                    "No photos are currently selected.",
                    ButtonType.OK);
            alert.setTitle("Attention");
            alert.setHeaderText("No Photos to Delete");
            Optional<ButtonType> result = alert.showAndWait();
        } else {
            Photo[] photosToDelete = selectedPhotos.toArray(new Photo[numSelected]);
            String contentString = "";
            for (Photo ph : photosToDelete) {
                contentString += ph.getName() + ", ";
            }
            contentString = contentString.substring(0, contentString.length() - 2);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to permanently delete " + contentString + "?", ButtonType.YES, ButtonType.NO);
            alert.initOwner(primaryStage);
            alert.setHeaderText("Permanently delete selected photos?");
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                PhotoManager.getInstance().deletePhotos(photosToDelete);
                alert = new Alert(Alert.AlertType.NONE, "Photos Deleted", ButtonType.OK);
                alert.initOwner(primaryStage);
                alert.setHeaderText("Successfully Deleted");
                if (photosToDelete.length == 1) {
                    alert.setContentText(contentString
                        + " has been successfully deleted");
                } else {
                    alert.setContentText(contentString
                        + " have been successfully deleted");
                }
                alert.showAndWait();
            }
        }
    }

    /*
     * Open the Add Photos screen upon clicking "Add Photos"
     */
    @FXML
    private void openAddPhotosScreen(ActionEvent event) {
        openScreen("addphotosscreen", "Add Photos");
    }

    /*
     * Update the "_ untagged photos" field in top right of main screen
     */
    private void setUntaggedPhotosText() {
        int numUntagged = PhotoManager.getInstance().getUntaggedPhotos().size();
        if (numUntagged == 1) {
            untaggedPhotosText.setText(numUntagged + " photo untagged");
        } else {
            untaggedPhotosText.setText(numUntagged + " photos untagged");
        }
    }

    /*
     * Change voice control widget appearance when toggled
     */
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

    /*
     * Change multiselect button text and icon appearance
     */
    private void setMultiSelectIndicators() {
        if (!multiSelectToggledOn) {
            multiSelectIndicator.setImage(new Image("/icons/add.png"));
            multiSelectButton.setText("Select Multiple");
        } else {
            multiSelectIndicator.setImage(new Image("/icons/delete.png"));
            multiSelectButton.setText("Stop Selecting");
        }
    }
}
