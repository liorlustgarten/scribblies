package scribblies;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.ListIterator;

/**
 * Created by Lior on 19/02/2015.
 */
public class Point implements com.goebl.simplify.Point {
    private double x,y;
    public Point(double x, double y){
        this.x=x;
        this.y=y;
    }

    public Point(){
    }

    public Point(Point2D p2d){
        setX(p2d.getX());
        setY(p2d.getY());
    }
    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    public void setX(double x){
        this.x = x;
    }

    public void setY(double y){
        this.y = y;
    }

    public static ArrayList<Point> convertPoint2DArrayList(ArrayList<Point2D> p2d){
        ListIterator<Point2D> i = p2d.listIterator();
        ArrayList<Point> ret = new ArrayList<Point>();
        while (i.hasNext()){
            ret.add(new Point(i.next()));
        }
        return ret;
    }
}
