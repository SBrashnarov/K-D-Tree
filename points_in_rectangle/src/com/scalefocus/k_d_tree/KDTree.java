package com.scalefocus.k_d_tree;

import com.scalefocus.shape.Point;
import com.scalefocus.shape.Rectangle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class KDTree {

    private static final Rectangle ROOT_PLANE =
            new Rectangle(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);

    private Node root;

    public void build(List<Point> points) {
        this.root = this.build(points, ROOT_PLANE, 0);
    }

    private Node build(List<Point> points, Rectangle nodePlane, int depth) {
        if (points.isEmpty()) {
            return null;
        }

        points.sort((x, y) -> (depth % 2 == 0) ? (x.getX() - y.getX()) : (x.getY() - y.getY()));
        int medianIndex = points.size() / 2;
        List<Point> leftSublist = new ArrayList<>(points.subList(0, medianIndex));
        List<Point> rightSublist = new ArrayList<>(points.subList(medianIndex + 1, points.size()));

        Point nodeData = points.get(medianIndex);
        Node newNode = new Node(nodeData, nodePlane);

        Map<String, Rectangle> childrenPlanes = this.splitPlane(nodePlane, depth, nodeData);

        newNode.setLeftChild(this.build(leftSublist, childrenPlanes.get("left"), depth + 1));
        newNode.setRightChild(this.build(rightSublist, childrenPlanes.get("right"), depth + 1));

        return newNode;
    }

    public void insert(Point point) {
        this.root = this.insert(point, this.root, 0, ROOT_PLANE);
    }

    private Node insert(Point point, Node node, int depth, Rectangle nodePlane) {
        if (node == null) {
            return new Node(point, nodePlane);
        }

        Map<String, Rectangle> childrenPlanes = this.splitPlane(nodePlane, depth, node.getData());

        int compare = this.compare(point, node, depth);

        if (compare > 0) {
            node.setRightChild(this.insert(point, node.getRightChild(), ++depth, childrenPlanes.get("right")));
        } else {
            node.setLeftChild(this.insert(point, node.getLeftChild(), ++depth, childrenPlanes.get("left")));
        }

        return node;
    }

    public List<Point> listPoints(Rectangle area) {
        List<Point> pointsInRange = new ArrayList<>();

        this.listPoints(area, this.root, pointsInRange::add, 0);

        return pointsInRange;
    }

    private void listPoints(Rectangle area, Node node, Consumer<Point> consumer, int depth) {
        if (area.containsPoint(node.getData())) {
            consumer.accept(node.getData());
        }

        if (node.getLeftChild() != null && node.getLeftChild().getNodePlane().intersect(area)) {
            this.listPoints(area, node.getLeftChild(), consumer, ++depth);
        }

        if (node.getRightChild() != null && node.getRightChild().getNodePlane().intersect(area)) {
            this.listPoints(area, node.getRightChild(), consumer, ++depth);
        }
    }

    public Point nearestNeighbor(Point point) {
        if (this.root == null) {
            throw new IllegalStateException("Tree is empty");
        }

        Node nearestNode = this.nearestNeighbor(point, this.root, 0, this.root);

        return nearestNode.getData();
    }

    private Node nearestNeighbor(Point point, Node node, int depth, Node bestNode) {
        if (node == null) {
            return bestNode;
        }

        double bestDistance = this.distance(point, bestNode.getData());
        if (bestDistance < this.shortestDistanceToPlane(point, node.getNodePlane())) {
            return bestNode;
        }

        double pointToNodeDistance = this.distance(point, node.getData());
        if (pointToNodeDistance < bestDistance) {
            bestNode = node;
        }

        if (this.compare(point, node, depth) > 0) {
            bestNode = this.nearestNeighbor(point, node.getRightChild(), ++depth, bestNode);
            bestNode = this.nearestNeighbor(point, node.getLeftChild(), ++depth, bestNode);
        } else {
            bestNode = this.nearestNeighbor(point, node.getLeftChild(), ++depth, bestNode);
            bestNode = this.nearestNeighbor(point, node.getRightChild(), ++depth, bestNode);
        }

        return bestNode;
    }

    //return negative or zero for left and positive for right
    private int compare(Point point, Node node, int depth) {
        if (depth % 2 == 0) {
            //even compare by x
            return Integer.compare(point.getX(), node.getData().getX());
        } else {
            //odd compare by y
            return Integer.compare(point.getY(), node.getData().getY());
        }
    }

    private Map<String, Rectangle> splitPlane(Rectangle plane, int depth, Point point) {
        Map<String, Rectangle> planes = new HashMap<>();

        if (depth % 2 == 0) {
            planes.putIfAbsent("left", new Rectangle(plane.getX1(), plane.getY1(), point.getX(), plane.getY2()));
            planes.putIfAbsent("right", new Rectangle(point.getX(), plane.getY1(), plane.getX2(), plane.getY2()));
        } else {
            planes.putIfAbsent("left", new Rectangle(plane.getX1(), plane.getY1(), plane.getX2(), point.getY()));
            planes.putIfAbsent("right", new Rectangle(plane.getX1(), point.getY(), plane.getX2(), plane.getY2()));
        }

        return planes;
    }

    private double shortestDistanceToPlane(Point point, Rectangle plane) {
        if (plane.containsPoint(point)) {
            return 0;
        } else {
            int dx = Math.max(Math.max(plane.getX1() - point.getX(), 0), point.getX() - plane.getX2());
            int dy = Math.max(Math.max(plane.getY1() - point.getY(), 0), point.getY() - plane.getY2());

            return Math.sqrt((dx * dx) + (dy * dy));
        }
    }

    private double distance(Point first, Point second) {
        int dx = first.getX() - second.getX();
        int dy = first.getY() - second.getY();

        return Math.sqrt((dx * dx) + (dy * dy));
    }
}
