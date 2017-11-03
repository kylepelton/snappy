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
import fxapp.LoadPhotosTask;
import javafx.application.Platform;

public class PhotoManager {

    // list of all photos to display
    private ObservableList<Photo> photos;
    // current photo displayed on view/edit photo screen
    private Photo currentPhoto;
    private static PhotoManager instance = null;
    // counter to ensure all photo directory names are unique
    private static int counter = 0;
    // to load photo directory path
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
            long timeAdded = System.currentTimeMillis();
            // directory name is timestamp + unique counter
            File newPhotoDir = new File(photosdir.toString() + File.separator
                    + timeAdded + "_" + counter);
            newPhotoDir.mkdir();
            File imageFile = new File(newPhotoDir.toString() + File.separator
                    + file.getName());
            try {
                imageFile.createNewFile();
                OutputStream stream = new FileOutputStream(imageFile);
                Files.copy(file.toPath(), stream);
                stream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            PhotoManager.createMetadataFile(
                    newPhotoDir.toString() + File.separator + ".metadata",
                    imageFile.getAbsolutePath(), file.getName(), timeAdded,
                    tags);
            counter += 1;
            try {
                loadPhoto(new Photo(newPhotoDir));
            } catch (Exception e) {
                System.err.println("Error: File " + newPhotoDir + " is corrupted.");
            }
        }
    }

    public static void createMetadataFile(String fileName, String imagePath,
            String imageName, long timeAdded, List<String> tags) {
        File tagFile = new File(fileName);
        try {
            tagFile.createNewFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter(tagFile));
            // data order: image path, image name, time added, tags
            bw.write(imagePath);
            bw.newLine();
            bw.write(imageName);
            bw.newLine();
            bw.write(Long.toString(timeAdded));
            bw.newLine();
            for (String tag : tags) {
                bw.write(tag);
                bw.newLine();
            }
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // adds photo to list of photos to show
    private void loadPhoto(Photo photoToLoad) {
        if (!this.photos.contains(photoToLoad)) {
            this.photos.add(photoToLoad);
        }
    }

    // adds array of photos to list of photos to show
    private void loadPhotos(File[] photosToLoad, LoadPhotosTask task) {
        int curr = 1;
        for (File dir : photosToLoad) {
            if (dir.isDirectory()) {
                // load photo on separate thread
                Platform.runLater(() -> {
                    try {
                        loadPhoto(new Photo(dir));
                    } catch (Exception e) {
                        System.err.println("Error: File " + dir + " is corrupted.");
                    }
                });
            }
            task.updateProgress(curr, photosToLoad.length);
            curr++;
        }
    }

    public void setProperties(Properties prop) {
        this.prop = prop;
    }

    public boolean hasPhotos() {
        photosdir = new File(prop.getProperty("photosdir"));
        return photosdir.listFiles().length > 0;
    }

    // Given a task to update progress, loads the persisted photos.
    public void loadPhotosSynchronous(LoadPhotosTask task) {
        photosdir = new File(prop.getProperty("photosdir"));
        loadPhotos(photosdir.listFiles(), task);
    }

    // prepare a photo for viewing/editing
    public void setCurrentPhoto(Photo photo) {
        this.currentPhoto = photo;
    }

    // return currently viewed photo
    public Photo getCurrentPhoto() {
        return this.currentPhoto;
    }

    public ObservableList<Photo> getPhotos() {
        return this.photos;
    }

    public ObservableList<Photo> getUntaggedPhotos() {
        ObservableList<Photo> untagged = FXCollections.observableArrayList();
        for (Photo p : photos) {
            if (p.getTags() == null || p.getTags().isEmpty() || p.getTags().equals("")) {
                untagged.add(p);
            }
        }
        return untagged;
    }

    public ObservableList<Photo> getRelatedPhotos(Photo photo) {
        ObservableList<Photo> related = FXCollections.observableArrayList();
        for (Photo p : photos) {
            for (String tag : p.getTags()) {
                if (photo.getTags().contains(tag))
                {
                    related.add(p);
                    break;
                }
            }
        }
        return related;
    }

    public void deletePhoto() {
        // Remove currentPhoto from our list of photos
        photos.remove(currentPhoto);
        File directory = currentPhoto.getDirectory();

        // Delete all photos in currentPhoto's directory, then delete that directory
        String[] filesInFolder = directory.list();
        for (String s: filesInFolder) {
            File currentFile = new File(directory.getPath(), s);
            currentFile.delete();
        }
        directory.delete();
    }

    public void deletePhotos(Photo[] photosToDelete) {
        for (Photo photo : photosToDelete) {
            photos.remove(photo);
            File directory = photo.getDirectory();
            String[] filesInFolder = directory.list();
            for (String s: filesInFolder) {
                File currentFile = new File(directory.getPath(), s);
                currentFile.delete();
            }
            directory.delete();
        }
    }
}
