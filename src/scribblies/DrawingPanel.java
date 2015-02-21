package scribblies;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Created by dgli on 16/02/15.
 */
public class DrawingPanel extends JPanel {

    ArrayList<Point2D> lineTrace;
    ArrayList<Point2D> traceCache = new ArrayList<Point2D>();

    public void setLineTrace(ArrayList<Point2D> t){
        this.lineTrace = t;
        this.traceCache.clear();
    }

    public void addCachePoint(Point2D p){
        this.traceCache.add(p);
    }

    @Override
    public void paintComponent( Graphics g ) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

//        Line2D line = new Line2D.Double(10, 10, 40, 40);
//        g2.setColor(Color.blue);
//        g2.setStroke(new BasicStroke(10));
//        g2.draw(line);

        if(lineTrace != null) {

            Path2D path = new Path2D.Double();

            Point[] simplifiedPath = new Point[0];
            simplifiedPath = new CurveMaker().simplifyPath(Point.convertPoint2DArrayList(lineTrace).toArray(new Point[lineTrace.size()]),1);
            Point[] flippedPath = new Point[simplifiedPath.length];
            for (int i=0;i<simplifiedPath.length;i++){
                flippedPath[simplifiedPath.length-i-1]=simplifiedPath[i];
            }
            System.out.println(CurveMaker.makeBezierCurve(simplifiedPath,130).toSvgString("red"));
            //System.out.println(CurveMaker.makeBezierCurve(flippedPath).toSvgString("black"));
            System.out.println(CurveMaker.findCorners(simplifiedPath,130).size());
//
 //           System.out.println(CurveMaker.cubicBezierFit(simplifiedPath).toSvgString());
   //         System.out.println(CurveMaker.cubicBezierFit(flippedPath).toSvgString());
  //          System.out.println(simplifiedPath.length);
            for (int i = 0; i<simplifiedPath.length;i++){
                if(i==0) {
                    path.moveTo(simplifiedPath[i].getX(), simplifiedPath[i].getY());
                }else{
                    path.lineTo(simplifiedPath[i].getX(), simplifiedPath[i].getY());
                }
            }
            g2.setColor(Color.black);
            g2.setStroke(new BasicStroke(1));

            g2.draw(path);

        }

        // draw cache
        Path2D cachePath = new Path2D.Double();
        boolean isFirst = true;

        for (Point2D p : traceCache) {
            if(isFirst) {
                cachePath.moveTo(p.getX(), p.getY());
                isFirst = false;
               // System.out.print ("<path d=\"M"+p.getX()+" "+p.getY()+" C");
            }else{
                cachePath.lineTo(p.getX(), p.getY());
               // System.out.print (" "+p.getX()+" "+p.getY());
            }
        }
        //TODO: clean this up
//System.out.println("/> style=\"stroke: black; fill: none;/>");
        if (!traceCache.isEmpty())
        {
            Point2D [] coordinates = traceCache.toArray(new Point2D [traceCache.size()]);
         //   System.out.println(CurveMaker.makeBezierCurve(coordinates).toSvgString());
        }
        g2.setColor(Color.red);
        g2.setStroke(new BasicStroke(1));
       //g2.draw(cachePath);

        //g2.dispose();
        //TODO: clean this up
        // draw cache
        Path2D cachePath2 = new Path2D.Double();
        isFirst = true;
        Point[] simplifiedPath = new Point[0];
        if (!traceCache.isEmpty())
        {
            simplifiedPath = new CurveMaker().simplifyPath(Point.convertPoint2DArrayList(traceCache).toArray(new Point[traceCache.size()]),1);
          //  System.out.println(CurveMaker.makeBezierCurve(simplifiedPath).toSvgString());
        }
      //  System.out.println(simplifiedPath.length);
        for (int i = 0; i<simplifiedPath.length;i++){
            if(i==0) {
                cachePath2.moveTo(simplifiedPath[i].getX(), simplifiedPath[i].getY());
            }else{
                cachePath2.lineTo(simplifiedPath[i].getX(), simplifiedPath[i].getY());
            }
        }
//System.out.println("/> style=\"stroke: black; fill: none;/>");
        g2.setColor(Color.black);
        g2.setStroke(new BasicStroke(1));
        g2.draw(cachePath2);

    }
}
