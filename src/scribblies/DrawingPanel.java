package scribblies;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.lang.reflect.Array;
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
            boolean isFirst = true;

            for (Point2D p : lineTrace) {
                if(isFirst) {
                    path.moveTo(p.getX(), p.getY());
                    isFirst = false;
                }else{
                    path.lineTo(p.getX(), p.getY());
                }
            }

            g2.setColor(Color.blue);
            g2.setStroke(new BasicStroke(2));
            g2.draw(path);

        }

        // draw cache
        Path2D cachePath = new Path2D.Double();
        boolean isFirst = true;

        for (Point2D p : traceCache) {
            if(isFirst) {
                cachePath.moveTo(p.getX(), p.getY());
                isFirst = false;
            }else{
                cachePath.lineTo(p.getX(), p.getY());
            }
        }

        g2.setColor(Color.red);
        g2.setStroke(new BasicStroke(1));
        g2.draw(cachePath);

        //g2.dispose();

    }
}
