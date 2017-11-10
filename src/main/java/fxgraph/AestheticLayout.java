package com.fxgraph.layout.aesthetic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.geometry.Bounds;

import com.fxgraph.graph.Cell;
import com.fxgraph.graph.Edge;
import com.fxgraph.graph.Graph;
import com.fxgraph.layout.base.Layout;

/**
 * This represents an attempt to solve the graph layout problem. That is, find
 * a way to place the nodes in a graph in a space in such a way that the graph
 * does not have too many edges crossing or nodes in "strange" places.
 *
 * This adapts the Fruchterman-Reingold algorithm from gephi. For more
 * information, please see the following links:
 *
 * https://github.com/gephi/gephi/wiki/Fruchterman-Reingold
 * http://citeseer.ist.psu.edu/viewdoc/download;jsessionid=19A8857540E8C9C26397650BBACD5311?doi=10.1.1.13.8444&rep=rep1&type=pdf
 */
public class AestheticLayout extends Layout {

    private Graph graph;
    private Random random;

    private final static int HEIGHT = 596;
    private final static int WIDTH = 880;
    private final static int AREA = HEIGHT * WIDTH;
    private final static double GRAVITY = 10.0;
    private final static int NUM_ITERS = 10;
    private final static double eps = 1.0;

    public AestheticLayout(Graph graph) {
        this.graph = graph;
        this.random = new Random();
    }

    public void execute() {
        List<Cell> nodes = graph.getModel().getAllCells();
        List<Edge> edges = graph.getModel().getAllEdges();

        // Initially position nodes in a grid
        int stride = 1 + (int) Math.ceil(Math.sqrt(nodes.size()));
        ArrayList<Integer> coords = new ArrayList<>();
        for (int i = 1; i < stride; i++) {
            for (int j = 1; j < stride; j++) {
                coords.add(i * WIDTH / stride + ((j * HEIGHT / stride) << 16));
            }
        }
        
        for (int i = 0; i < nodes.size(); i++) {
            Cell node = nodes.get(i);
            int ind;
            // source image is placed near the center
            if (i == 0) {
                ind = coords.size() / 2;
            } else {
                ind = random.nextInt(coords.size());
            }

            double x = (coords.get(ind) << 16) >> 16;
            double y = (coords.get(ind) >> 16);
            coords.remove(ind);
            node.relocate(x, y);
        }

        double k = Math.sqrt(((double)AREA) / (1.0 + nodes.size())) / 10;
        double t = HEIGHT / 10;

        for (int i = 0; i < NUM_ITERS; i++) {
            for (Cell node1 : nodes) {
                node1.dx = 0;
                node1.dy = 0;
                for (Cell node2 : nodes) {
                    if (!node1.equals(node2)) {
                        double xDist = node1.getX() - node2.getX();
                        double yDist = node1.getY() - node2.getY();
                        if (Math.abs(xDist) < 1e-5 && Math.abs(yDist) < 1e-5) {
                            xDist += eps * (random.nextDouble() - 0.5);
                            yDist += eps * (random.nextDouble() - 0.5);
                        }
                        double distance = Math.sqrt(xDist * xDist + yDist * yDist) + 1e-9;
    
                        double repulsiveForce = k * k / distance;
                        node1.dx += xDist / distance * repulsiveForce;
                        node1.dy += yDist / distance * repulsiveForce;
                        
                    }
                }
            }
    
            for (Edge e: edges) {
                Cell source = e.getSource();
                Cell target = e.getTarget();
    
                double xDist = source.getX() - target.getX();
                double yDist = target.getY() - target.getY();
                if (Math.abs(xDist) < 1e-5 && Math.abs(yDist) < 1e-5) {
                    xDist += eps * (random.nextDouble() - 0.5);
                    yDist += eps * (random.nextDouble() - 0.5);
                }
                double distance = Math.sqrt(xDist * xDist + yDist * yDist) + 1e-9;
    
                double attractiveForce =  distance / k;
                source.dx -= xDist / distance * attractiveForce;
                source.dy -= yDist / distance * attractiveForce;
                target.dx += xDist / distance * attractiveForce;
                target.dy += yDist / distance * attractiveForce;
                
            }
    
            for (Cell node: nodes) {
                double centerX = node.getX();
                double centerY = node.getY();
                node.dx = Math.max(-1 * t, Math.min(node.dx, t));
                node.dy = Math.max(-1 * t, Math.min(node.dy, t));
    
                double distance = Math.sqrt(node.dx * node.dx + node.dy * node.dy) + 1;
                node.relocate(Math.max(0, Math.min(WIDTH, centerX + node.dx / distance * node.dx)),
                              Math.max(0, Math.min(HEIGHT, centerY + node.dy / distance * node.dy)));
            }
            
            t /= 1.2;
        }
    }

}
