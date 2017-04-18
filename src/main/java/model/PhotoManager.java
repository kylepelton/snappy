package model;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

public class PhotoManager {

    private ArrayList<File> photos;
    private ObservableList<File> _photos;
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
        this._photos.addAll(photos);
    }

    public ObservableList<File> getPhotos() {
        return _photos;
    }
}