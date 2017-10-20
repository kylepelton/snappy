package controllers;

import javafx.stage.Stage;
import java.util.Observable;
import java.util.Observer;

public class Controller implements Observer {
    protected Stage stage;
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void update(Observable o, Object arg) {}

}