package activities;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainActivity extends Application {

    public static void main(String[] args) {
        try {
            launch(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/mainscreen.fxml"));
        Scene scene = new Scene(root, 1280 , 720);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Snappy Welcome");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
