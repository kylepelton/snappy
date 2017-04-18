package model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;

public class Photo {
    private final File file;
    private final List<String> tags;
    private final List<String> tagsBackup;
    private final Image thumbnail;
    private final Image mainScreenImg;
    private final Image previewImg;

    public Photo(File file) {
        this.file = file;
        this.thumbnail = new Image(file.toURI().toString(), 320, 190, true, true);
        this.mainScreenImg = new Image(file.toURI().toString(), 230, 210, true, true);
        this.previewImg = new Image(file.toURI().toString(), 600, 450, true, true);
        tags = new ArrayList<>();
        tagsBackup = new ArrayList<>();
    }

    public Photo(File file, List<String> tags) {
        this(file);
        if (tags != null) {
            this.tags.addAll(tags);
            tagsBackup.addAll(tags);
        }
    }
    
    public void commitTagChanges() {
        tagsBackup.clear();
        tagsBackup.addAll(tags);
    }
    
    public void revertTagChanges() {
        tags.clear();
        tags.addAll(tagsBackup);
    }

    public void addTag(String tag) {
        this.tags.add(tag);
    }

    public void removeTag(String tag) {
        this.tags.remove(tag);
    }

    public void setTag(String oldTag, String newTag) {
        int ind = this.tags.indexOf(oldTag);
        this.tags.set(ind, newTag);
    }
    
    public void setTags(List<String> tags) {
        this.tags.clear();
        this.tags.addAll(tags);
    }

    public File getFile() {
        return file;
    }

    public List<String> getTags() {
        return tags;
    }

    public Image getThumbnail() {
        return thumbnail;
    }

    public Image getMainScreenImg() {
        return mainScreenImg;
    }

    public Image getPreviewImg() {
        return previewImg;
    }
}
