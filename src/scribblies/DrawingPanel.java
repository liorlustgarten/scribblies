package scribblies;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Created by dgli on 16/02/15.
 */
public class DrawingPanel extends JPanel {

    ArrayList<ArrayList<Point2D>> rawInput = new ArrayList<ArrayList<Point2D>>();
    ArrayList<CubicBezierCurve> curves = new ArrayList<CubicBezierCurve>();
    ArrayList<Point2D> traceCache = new ArrayList<Point2D>();
    @Getter @Setter double simplifyTolerance = 2; //dictates how much detail is kept when simplifying the path
    @Getter @Setter double cornerTolerance = 135; //maximum angle (in path) considered a corner

    public void addCachePoint(Point2D p){
        this.traceCache.add(p);
    }

    public void clear (){
        rawInput = new ArrayList<ArrayList<Point2D>>();
        curves = new ArrayList<CubicBezierCurve>();
        traceCache = new ArrayList<Point2D>();
    }

    public void addPath(ArrayList<Point2D> trace){
        rawInput.add(trace);
        Point[] simplifiedPath = CurveMaker.simplifyPath(Point.convertPoint2DArrayList(trace).toArray(new Point[trace.size()]),simplifyTolerance);
        curves.add(CurveMaker.makeBezierCurve(simplifiedPath,cornerTolerance));
    }

    @Override
    public void paintComponent( Graphics g ) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        for (ArrayList<Point2D> points:rawInput) {
            drawPath(points, Color.blue, new BasicStroke(1), g2);
        }

        for (CubicBezierCurve curve:curves)
        {
            g2.setColor(Color.black);
            g2.draw(curve.getPath2D());
        }

        Path2D crazyTime = new Path2D.Double();
        crazyTime = CurveMaker.makeBezierCurve(new CurveMaker().simplifyPath(Point.convertPoint2DArrayList(traceCache).toArray(new Point[traceCache.size()]),2),135).getPath2D();
        g2.setColor(Color.blue);
        g2.setStroke(new BasicStroke(1));
        g2.draw(crazyTime);
    }

    public void drawPath (ArrayList<Point2D> path,Color col,Stroke stroke, Graphics2D g2){
        Path2D toDraw = new Path2D.Double();
        boolean isFirst =true;
        for (Point2D p : path) {
            if(isFirst) {
                toDraw.moveTo(p.getX(), p.getY());
                isFirst = false;
            }else{
                toDraw.lineTo(p.getX(), p.getY());
            }
        }
        g2.setColor(col);
        g2.setStroke(stroke);
        g2.draw(toDraw);
    }

}
