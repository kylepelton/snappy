package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;

public class Photo {
    private final String name;
    private final long timeAdded;
    private final File imageDir;
    private final File imageFile;
    private final File metadataFile;
    private final List<String> tags;
    private final List<String> tagsBackup;
    private final Image thumbnail;
    private final Image mainScreenImg;
    private final Image previewImg;

    public Photo(File imageDir) throws IOException, FileNotFoundException {
        metadataFile = new File(imageDir.getAbsolutePath() + File.separator
                + ".metadata");
        this.imageDir = imageDir;
        tags = new ArrayList<>();
        tagsBackup = new ArrayList<>();
        String name = null;
        File imageFile = null;
        long timeAdded = 0;
        BufferedReader br = new BufferedReader(new FileReader(metadataFile));
        imageFile = new File(br.readLine());
        name = br.readLine();
        timeAdded = Long.parseLong(br.readLine());
        String line = br.readLine();
        while (line != null) {
            tags.add(line);
            line = br.readLine();
        }
        br.close();
        this.name = name;
        this.imageFile = imageFile;
        this.timeAdded = timeAdded;
        
        tagsBackup.addAll(tags);
        this.thumbnail = new Image(imageFile.toURI().toString(),
                320, 190, true, true);
        this.mainScreenImg = new Image(imageFile.toURI().toString(),
                230, 210, true, true);
        this.previewImg = new Image(imageFile.toURI().toString(),
                600, 450, true, true);
    }

    public void commitTagChanges() {
        tagsBackup.clear();
        tagsBackup.addAll(tags);
        PhotoManager.createMetadataFile(metadataFile.getAbsolutePath(),
                imageFile.getAbsolutePath(), name, timeAdded, tags);
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
    
    public String getName() {
        return name;
    }

    public File getDirectory() {
        return imageDir;
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
