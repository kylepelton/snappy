package model;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

public class PhotoManager {

    private ArrayList<File> photos;
    private ObservableList<File> _photos;
    private File currentPhoto;
    private static PhotoManager instance = null;

    protected PhotoManager() {
        photos = new ArrayList<File>();
        _photos = FXCollections.observableList(photos);
    }

    public static PhotoManager getInstance() {
        if(instance == null) {
            instance = new PhotoManager();
        }
        return instance;
    }

    public void addPhotos(List<File> photos) {
        for (File file : photos) {
            if (!this._photos.contains(file)) {
                this._photos.add(file);
            }
        }
    }

    public void setCurrentPhoto(File photo) {
        this.currentPhoto = photo;
    }

    public File getCurrentPhoto() {
        return currentPhoto;
    }

    public ObservableList<File> getPhotos() {
        return _photos;
    }
}