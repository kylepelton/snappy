package controller;

import javafx.stage.Stage;
import java.util.Observable;
import java.util.Observer;

/**
 * Controller is the parent class for all controllers in this application
 */
public abstract class Controller implements Observer {
    // Represents the controller's stage
    protected Stage stage;
    protected IMainScreenController mainScreen;
    
    /*
     * Set this controller's stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setMainScreen(IMainScreenController mainScreen) {
        this.mainScreen = mainScreen;
    }

    /*
     * Do something when the observable to which the controller subscribes
     * notifies its observers
     *
     * Each controller is an observer and can subscribe to SpeechRecognizer
     *
     * The behavior on an update depends on the controllers 
     */
    @Override
    public void update(Observable o, Object arg) {}

}