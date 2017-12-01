package controller;

import java.util.HashMap;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

public class HelpScreenController extends Controller {

    @FXML private ListView helpList;
    @FXML private TextArea textArea;
    private Map<String, String> map;

    @FXML
    private void initialize() {
        createMap();
        ObservableList<String> helpMenuItems = FXCollections.observableArrayList("Adding Photos",
            "Deleting Photos", "Editing Tags", "Processing Multiple Photos", "Using Graph View",
            "Using Grid View", "Using Voice Control");
        helpList.setItems(helpMenuItems);
        helpList.setOnMouseClicked(e -> {
            textArea.setText(map.get(helpList.getSelectionModel().getSelectedItem().toString()));
        });
    }

    private void createMap() {
        map = new HashMap<String, String>();
        map.put("Adding Photos", "To start using Snappy, you will have to add photos to the application. To add photos to Snappy, do the following:\n\n"
            + "1) On the main screen of the application, select the \"Add Photos\" button.\n2) This should open the Add Photos screen. There are two ways to add photos:\n"
            + "    a) If you have the photo(s) you want to add open in another screen (e.g., in your File Explorer), you can simply drag the files into the \"Drag images here\" box.\n"
            + "    b) Additionally, you can click the \"Browse\" button to open your operating system's file dialog. From there, you can navigate to the appropriate folder and select your photo(s).\n"
            + "3) All of the photos you add should appear in the \"Drag images here\" box. You can continue to add as many photos as you want.\n"
            + "4) If you want to add tag(s) to all of these photos, type them into the Tag All Photos (Optional) text box, with each additional tag separated by commas.\n"
            + "5) Once you are satisfied, select the \"Add Photos\" button on the Add Photos screen. If you do not wish to add these photos, select \"Cancel.\" Once you have done this,"
            + " the Add Photos screen should close and you should be able to see your newly-added photos in the grid view of the application.");
        map.put("Deleting Photos", "Sometimes, you might want to delete photos which you have added. Fortunately, Snappy gives you two ways to do this:\n\n"
            + "Deleting One Photo\n"
            + "1) On the main screen of the application, select the photo you wish to delete.\n"
            + "2) On this photo's viewing screen, select the \"Delete Photo\" button. On the confirmation popup, select \"Yes\" if you do want to permanently delete the photo. Otherwise, select \"No\".\n\n"
            + "Deleting Multiple Photos\n"
            + "1) On the main screen of the application, click the \"Select Multiple\" button.\n"
            + "2) Click on the photos which you wish to delete. Once you have selected all of the photos, click on \"Delete Selected Photos\". On the confirmation popup, select \"Yes\" if you want to permanently "
            + "delete these photos. Otherwise, select \"No\".");
        map.put("Editing Tags", "With two exceptions, every time you edit a photo's tag, you will do so from that photo's tag editing screen. While on this screen, you will see the photo and "
            + "a text box below it. To add tags to a photo, simply type whatever tag you want on a line separate from other tags. Please keep in mind that only typing spaces between words will "
            + "leave them in the same tag. When tagging multiple photos at a time, navigate through your photos' tag editing screens by selecting the \"Next Photo\" and \"Previous Photo\" buttons. "
            + "The list of photos loops once you reach the end of the sequence. You can finish your session at any time by pressing the \"Done Tagging\" button.\n\n"
            + "There are multiple ways to reach this tag editing screen within Snappy!\n\n"
            + "First, you can edit an individual photo's tag by clicking the photo you want to edit on the grid or graph view. Once the view photo screen is open, select the \"Edit Tags\" button "
            + "found at the bottom of the window to open this photo's tag editing screen.\n\n"
            + "Second, you can use the \"Start Tagging\" button found on the grid view to start a tagging session. This button allows for 4 options: \"All Photos\", \"Untagged Photos\", \"Selected Photos\""
            + ", and \"Selected Photos With...\". All tagging sessions start with the most recently added photo and continue in reverse chronological order with respect to the time they were uploaded to "
            + "the application.\n\n- All photos starts a tagging session which goes through the tag editing screen of every photo you currently have in the application.\n"
            + "- \"Untagged Photos\" selects every photo you have yet to add at least one tag to and starts a tagging session using just these photos. Your current number of untagged photos can be seen "
            + "directly above the \"Start Tagging\" button on the grid view.\n"
            + "- \"Selected Photos\" starts a tagging session using only the photos currently selected with the multi-select feature."
            + "- \"Selected Photos With...\" is one of the two exceptions where adding a tag to a photo is not done through the tag editing screen. This option give a single text box to add one or more "
            + "tags to every photo currently selected by the multi-select feature.\n\n"
            + "The other exception to adding tags to photos outside of the tag editing screen is seen when you first add a photo to Snappy. Please see the \"Add Photos\" help screen for more information.");
        map.put("Processing Multiple Photos", "The multi-select function makes it easy to perform batch tag editing or deletion of photos. To use multi-select, first navigate to the "
            + "grid view and click the \"Select Multiple\" button on the bottom left. Then, click on all photos that should be processed. Photos that are selected should be highlighted "
            + "with a blue border.\n\nTo delete all selected photos, click the \"Delete Selected Photos\" button near the bottom center.\n\nTo tag all selected photos with the same tag(s),"
            + " click the \"Start Tagging\" button on the top right and choose the \"Selected Photos With...\" dropdown option, then enter the comma-separated tag(s).\n\n"
            + "To launch individual tagging screens with only the selected photos, click the \"Start Tagging\" button on the top right and choose the \"Selected Photos\" dropdown option.\n\n"
            + "Finally, to exit multi-select, click the \"Stop Selecting\" button on the bottom left.");
        map.put("Using Graph View", "The graph view displays photos as a graph centering on a specific photo and shows all photos that share at least one tag with that photo. "
            + "This view provides an easy way of visualizing connections between photos.\n\nTo open up the graph view, click any photo from the grid view, then click on the "
            + "\"View Related Photos\" button. From the graph view, users can click on any photo to bring up the image details. It is possible to create many graph views simultaneously "
            + "by clicking on \"View Related Photos\" again from the image details, which can be used to visualize a chain of connected images. Use the tab bar near the top of the "
            + "application to navigate between and close graph view tabs.");
        map.put("Using Grid View", "Grid view displays all of your photos, in order of how recently they were added to the application.\n\n"
            + "From the grid view, you can click on a photo to open its viewing screen.\n\n"
            + "You can also use the search bar at the top of the grid view to filter which photos are displayed.");
        map.put("Using Voice Control", "Voice control can be used to tag your photos in a hands-free manner.\n\n"
            + "To toggle voice control, click the \"Enable Voice Control\" or \"Disable Voice Control\" button at the bottom right of the main screen. Voice control is enabled when the "
            + "indicator below this button is bright red.\n\n"
            + "When you are on the tagging screen and voice control is enabled, whatever you speak will be added as a tag to the current photo. Note that tags over three words are omitted. "
            + "Try to leave a pause after you say a tag so that the speech recognizer can process it.\n\n"
            + "If you are tagging multiple photos, you can say \"Next\" or \"Previous\" to move on to the next or previous photo in the tagging sequence, respectively.");
    }
}
