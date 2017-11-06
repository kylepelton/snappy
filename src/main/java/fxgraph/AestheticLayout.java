package com.fxgraph.layout.aesthetic;

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

    private final static int HEIGHT = 500;
    private final static int WIDTH = 500;
    private final static int AREA = 500;
    private final static double GRAVITY = 10;

    public AestheticLayout(Graph graph) {
        this.graph = graph;
        this.random = new Random();
    }

    public void execute() {
        List<Cell> nodes = graph.getModel().getAllCells();
        List<Edge> edges = graph.getModel().getAllEdges();

        // Initially give them random positions
        for (Cell node : nodes) {
            double x = random.nextDouble() * 500;
            double y = random.nextDouble() * 500;
            node.relocate(x, y);
        }

        double maxDisplace = Math.sqrt(AREA) / 10.0;
        double k = Math.sqrt(((double)AREA) / (1.0 + nodes.size()));

        for (Cell node1 : nodes) {
            for (Cell node2 : nodes) {
                if (!node1.equals(node2)) {
                    double xDist = node1.getX() - node2.getX();
                    double yDist = node1.getY() - node2.getY();
                    double distance = Math.sqrt(xDist * xDist + yDist * yDist);

                    if (distance > 0) {
                        double repulsiveForce = k * k / distance;
                        node1.dx += xDist / distance * repulsiveForce;
                        node1.dy += yDist / distance * repulsiveForce;
                    }
                }
            }
        }

        for (Edge e: edges) {
            Cell source = e.getSource();
            Cell target = e.getTarget();

            double xDist = source.getX() - target.getX();
            double yDist = target.getY() - target.getY();
            double distance = Math.sqrt(xDist * xDist + yDist * yDist);

            if (distance > 0) {
                double attractiveForce = distance * distance / k;
                source.dx -= xDist / distance * attractiveForce;
                source.dy -= yDist / distance * attractiveForce;
                target.dx += xDist / distance * attractiveForce;
                target.dy += yDist / distance * attractiveForce;
            }
        }

        for (Cell node: nodes) {
            double centerX = node.getX();
            double centerY = node.getY();
            double distance = Math.sqrt(centerX * centerX + centerY * centerY);
            double gf = 0.01 * k * GRAVITY * distance;
            node.dx -= gf * centerX / distance;
            node.dy -= gf * centerY / distance;

            node.dx /= 800;
            node.dy /= 800;

            distance = Math.sqrt(node.dx * node.dx + node.dy * node.dy);
            if (distance > 0) {
                double limitedDistance = Math.min(maxDisplace / 800, distance);
                node.relocate(centerX + node.dx / distance * limitedDistance,
                              centerY + node.dy / distance * limitedDistance);
            }
        }

    }

}
