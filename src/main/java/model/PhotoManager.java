package model;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

public class PhotoManager {

    private ArrayList<Photo> photos;
    private ObservableList<Photo> _photos;
    private Photo currentPhoto;
    private static PhotoManager instance = null;

    protected PhotoManager() {
        photos = new ArrayList<Photo>();
        _photos = FXCollections.observableList(photos);
    }

    public static PhotoManager getInstance() {
        if(instance == null) {
            instance = new PhotoManager();
        }
        return instance;
    }

    public void addPhotos(List<File> photos, List<String> tags) {
        for (File file : photos) {
            if (!this._photos.contains(file)) {
                this._photos.add(new Photo(file, tags));
            }
        }
    }

    public void setCurrentPhoto(Photo photo) {
        this.currentPhoto = photo;
    }

    public Photo getCurrentPhoto() {
        return currentPhoto;
    }

    public ObservableList<Photo> getPhotos() {
        return _photos;
    }
}