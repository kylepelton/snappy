package model;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;

public class Photo {
    private final File file;
    private final File imageFile;
    private final File tagFile;
    private final List<String> tags;
    private final List<String> tagsBackup;
    private final Image thumbnail;
    private final Image mainScreenImg;
    private final Image previewImg;

    public Photo(File file) {
        this.file = file;
        File[] files = file.listFiles();
        if (files[0].getName().equals("tags.txt")) {
            tagFile = files[0];
            imageFile = files[1];
        } else {
            tagFile = files[1];
            imageFile = files[0];
        }
        this.thumbnail = new Image(imageFile.toURI().toString(), 320, 190, true, true);
        this.mainScreenImg = new Image(imageFile.toURI().toString(), 230, 210, true, true);
        this.previewImg = new Image(imageFile.toURI().toString(), 600, 450, true, true);
        tags = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(tagFile));
            String line = br.readLine();
            while (line != null) {
                tags.add(line);
                line = br.readLine();
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        tagsBackup = new ArrayList<>();
    }

    public void commitTagChanges() {
        tagsBackup.clear();
        tagsBackup.addAll(tags);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(tagFile));
            for (String tag : tags) {
                bw.write(tag);
                bw.newLine();
            }
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
