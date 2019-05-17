package com.scalefocus;

import com.scalefocus.k_d_tree.KDTree;
import com.scalefocus.shape.Point;
import com.scalefocus.shape.Rectangle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class App {
    public static void main(String[] args) {

        List<Point> pointsToInsert = Arrays.asList(
                new Point(3, 3),
                new Point(2, 2),
                new Point(4, 4),
                new Point(1, 1),
                new Point(5, 5));

        KDTree treeWithInsert = new KDTree();
        for (Point point : pointsToInsert) {
            treeWithInsert.insert(point);
        }


        List<Point> points = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            for (int j = 1; j <= 100; j++) {
                points.add(new Point(i, j));
            }
        }

        KDTree treeWithBuild = new KDTree();
        treeWithBuild.build(points);

        Rectangle queryRectangle = new Rectangle(0, 0, 3, 3);

        List<Point> kdTreeResult = treeWithBuild.listPoints(queryRectangle);
        System.out.println(String.format("Number of points within rectangle: %d", kdTreeResult.size()));

        Point queryPoint = new Point(5, 5);
        Point nearest = treeWithBuild.nearestNeighbor(queryPoint);
        System.out.println();
        System.out.println(String.format("Nearest neighbour to point %s, is point: %s", queryPoint, nearest));
    }
}
