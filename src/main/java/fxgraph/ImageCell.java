package com.fxgraph.cells;

import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

import com.fxgraph.graph.Cell;

import model.Photo;
import model.PhotoManager;
import controller.IMainScreenController;

public class ImageCell extends Cell {
	private Photo photo;

    public ImageCell(String id, Photo photo, IMainScreenController mainscreen) {
        super(id);

        this.photo = photo;
        ImageView view = new ImageView(photo.getPreviewImg());
        view.setFitWidth(100);
        view.setPreserveRatio(true);
        view.setOnMouseClicked((e) -> {
        	PhotoManager.getInstance().setCurrentPhoto(photo);
        	mainscreen.openScreen("viewphotoscreen", photo.getName());
        });

        setView(view);
    }
}