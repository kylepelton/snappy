package com.fxgraph.cells;

import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

import com.fxgraph.graph.Cell;

public class ImageCell extends Cell {

    public ImageCell(String id, Image photo) {
        super(id);

        ImageView view = new ImageView(photo);
        view.setFitWidth(100);
        view.setPreserveRatio(true);

        setView(view);

    }

}