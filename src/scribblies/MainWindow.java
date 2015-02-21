package scribblies;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Created by dgli on 16/02/15.
 */
public class MainWindow extends JFrame implements MouseListener, MouseMotionListener {

    DrawingPanel canvas;
    JTable sampleTable;

    ArrayList<Point2D> traceCache;
    PathModel pathModel;

    public MainWindow(){

        // set window options
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // make the drawing panel
        canvas = new DrawingPanel();
        canvas.setMinimumSize(new Dimension(500, 400));
        canvas.setPreferredSize(new Dimension(500, 400));
        canvas.addMouseListener(this);
        canvas.addMouseMotionListener(this);
        canvas.setBorder(new LineBorder(Color.RED, 3));
        add(canvas, BorderLayout.WEST);

        // make table
        String [] columns = {"X", "Y"};
        Object [][] sampleData = {};
        sampleTable = new JTable(sampleData, columns);

        JScrollPane tableScroll = new JScrollPane(sampleTable);
        tableScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        tableScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        add(tableScroll, BorderLayout.CENTER);


        setSize(750, 400);
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        // create the cache
        traceCache = new ArrayList<Point2D>();

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

        sampleTable.setModel(new PathModel(traceCache));

        canvas.setLineTrace(traceCache);
        canvas.repaint();
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        Point2D newPoint = new Point2D.Double(mouseEvent.getX(), mouseEvent.getY());
        traceCache.add(newPoint);
        canvas.addCachePoint(newPoint);
        canvas.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {

    }
}
