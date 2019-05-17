package com.scalefocus.k_d_tree;

import com.scalefocus.shape.Point;
import com.scalefocus.shape.Rectangle;

public class Node {

    private Point data;
    private Node leftChild;
    private Node rightChild;
    private Rectangle nodePlane;

    public Node(Point data, Rectangle nodePlane) {
        this.data = data;
        this.nodePlane = nodePlane;
    }

    public Point getData() {
        return data;
    }

    public void setData(Point data) {
        this.data = data;
    }

    public Node getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(Node leftChild) {
        this.leftChild = leftChild;
    }

    public Node getRightChild() {
        return rightChild;
    }

    public void setRightChild(Node rightChild) {
        this.rightChild = rightChild;
    }

    public Rectangle getNodePlane() {
        return nodePlane;
    }

    public void setNodePlane(Rectangle nodePlane) {
        this.nodePlane = nodePlane;
    }
}
