package model;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

public class PhotoManager {

    private ObservableList<Photo> photos;
    private Photo currentPhoto;
    private static PhotoManager instance = null;

    protected PhotoManager() {
        this.photos = FXCollections.observableList(new ArrayList<Photo>());
    }

    public static PhotoManager getInstance() {
        if(instance == null) {
            instance = new PhotoManager();
        }
        return instance;
    }

    public void addPhotos(List<File> photosToAdd, List<String> tags) {
        for (File file : photosToAdd) {
            if (!this.photos.contains(file)) {
                this.photos.add(new Photo(file, tags));
            }
        }
    }

    public void setCurrentPhoto(Photo photo) {
        this.currentPhoto = photo;
    }

    public Photo getCurrentPhoto() {
        return this.currentPhoto;
    }

    public ObservableList<Photo> getPhotos() {
        return this.photos;
    }
}
