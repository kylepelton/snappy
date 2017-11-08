package com.fxgraph.graph;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class Cell extends Pane {

    private String cellId;

    private List<Cell> children = new ArrayList<>();
    private List<Cell> parents = new ArrayList<>();

    public double dx;
    public double dy;

    private Node view;

    public Cell(String cellId) {
        this.cellId = cellId;
        this.dx = 0;
        this.dy = 0;
    }

    public void addCellChild(Cell cell) {
        children.add(cell);
    }

    public List<Cell> getCellChildren() {
        return children;
    }

    public void addCellParent(Cell cell) {
        parents.add(cell);
    }

    public List<Cell> getCellParents() {
        return parents;
    }

    public void removeCellChild(Cell cell) {
        children.remove(cell);
    }

    public void setView(Node view) {
        this.view = view;
        getChildren().add(view);

    }

    public Node getView() {
        return this.view;
    }

    public String getCellId() {
        return cellId;
    }

    /*
     * The center of the cell's x value
     */
    public double getX() {
        Bounds bound = this.localToScene(this.getBoundsInLocal());
        return (bound.getMinX() + bound.getMaxX()) / 2.0;
    }

    /*
     * The center of the cell's y value
     */
    public double getY() {
        Bounds bound = this.localToScene(this.getBoundsInLocal());
        return (bound.getMinY() + bound.getMaxY()) / 2.0;
    }
}
