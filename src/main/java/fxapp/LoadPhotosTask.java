package fxapp;

import javafx.concurrent.Task;
import javafx.scene.control.ProgressBar;
import model.PhotoManager;
import javafx.stage.Stage;

public class LoadPhotosTask extends Task<Void> {
	private ProgressBar progress;

	public LoadPhotosTask(ProgressBar progress) {
		super();
		this.progress = progress;
	}

	public void updateProgress(double iterations, double total) {
		progress.setProgress(iterations / total);
	}

	@Override public Void call() {
		// Load photos
        PhotoManager.getInstance().loadPhotosSynchronous(this);
        // Wait so that the screen displays for a minimum time
        try {
    		Thread.sleep(1500);
    	} catch (InterruptedException e) {
    		// Do nothing
    	}
        return null;
    }

    @Override protected void succeeded() {
		Stage stage = (Stage) progress.getScene().getWindow();
	    stage.close();
    }
}