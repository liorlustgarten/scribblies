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
        repaint();
    }

    public void addPath(ArrayList<Point2D> trace){
        rawInput.add(trace);
        Point[] simplifiedPath = CurveMaker.simplifyPath(Point.convertPoint2DArrayList(trace).toArray(new Point[trace.size()]),simplifyTolerance);
        curves.add(CurveMaker.makeBezierSplineCurve(simplifiedPath, cornerTolerance));
    }

    @Override
    public void paintComponent( Graphics g ) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        String lineType = MainWindow.lineDrawTypeGroup.getSelection().getActionCommand();

        if (lineType.equalsIgnoreCase(MainWindow.RAW_RADIO_BUTTON_ACTION_COMMAND)) {
            //draw the trace cache
            drawPath(traceCache,Color.blue, new BasicStroke(1), g2);

            if (rawInput!=null){
                for (ArrayList<Point2D> points : rawInput) {
                    drawPath(points, Color.blue, new BasicStroke(1), g2);
                }
            }
        }

        if (lineType.equalsIgnoreCase(MainWindow.BEZIER_RADIO_BUTTON_ACTION_COMMAND)) {
            //draw current trace cache as a bezier curve
            g2.draw(CurveMaker.makeBezierSplineCurve(CurveMaker.simplifyPath(Point.convertPoint2DArrayList(traceCache).toArray(new Point[traceCache.size()]), simplifyTolerance), cornerTolerance).getPath2D());

            //draw the previously draw curves
            for (CubicBezierCurve curve:curves)
            {
                g2.setColor(Color.black);
                g2.draw(curve.getPath2D());
            }
        }


    }

    public void drawPath (ArrayList<Point2D> path,Color col,Stroke stroke, Graphics2D g2){
        if (path==null)
            return;
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

    public void undo() {
        if (rawInput!=null&&rawInput.size()>0)
            rawInput.remove(rawInput.size()-1);
        if (curves!=null&&curves.size()>0)
            curves.remove(curves.size()-1);
        traceCache = new ArrayList<Point2D>();
    }
}
