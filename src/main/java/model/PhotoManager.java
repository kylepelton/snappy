package model;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.io.File;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;

public class PhotoManager {

    private ObservableList<Photo> photos;
    private Photo currentPhoto;
    private static PhotoManager instance = null;
    private static int counter = 0;
    private Properties prop;
    private File photosdir;

    protected PhotoManager() {
        this.photos = FXCollections.observableList(new ArrayList<Photo>());
    }

    public static PhotoManager getInstance() {
        if(instance == null) {
            instance = new PhotoManager();
        }
        return instance;
    }

    public void savePhotos(List<File> photosToSave, List<String> tags) {
        for (File file : photosToSave) {
            File newPhotoFile = new File(photosdir.toString() + File.separator + System.currentTimeMillis() + "_" + counter);
            newPhotoFile.mkdir();
            File imageFile = new File(newPhotoFile.toString() + File.separator + file.getName());
            try {
                imageFile.createNewFile();
                OutputStream stream = new FileOutputStream(imageFile);
                Files.copy(file.toPath(), stream);
                stream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            File tagFile = new File(newPhotoFile.toString() + File.separator + "tags.txt");
            try {
                tagFile.createNewFile();
                BufferedWriter bw = new BufferedWriter(new FileWriter(tagFile));
                for (String tag : tags) {
                    bw.write(tag);
                    bw.newLine();
                }
                bw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            counter += 1;
            loadPhoto(new Photo(newPhotoFile));
        }
    }

    private void loadPhoto(Photo photoToLoad) {
        if (!this.photos.contains(photoToLoad)) {
            this.photos.add(photoToLoad);
        }
    }

    private void loadPhotos(File[] photosToLoad) {
        loadPhotos(Arrays.asList(photosToLoad));
    }

    private void loadPhotos(List<File> photosToLoad) {
        for (File file : photosToLoad) {
            loadPhoto(new Photo(file));
        }
    }

    public void setProperties(Properties prop) {
        this.prop = prop;
        photosdir = new File(prop.getProperty("photosdir"));
        loadPhotos(photosdir.listFiles());
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
