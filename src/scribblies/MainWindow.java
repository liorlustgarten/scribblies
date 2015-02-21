package scribblies;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Created by dgli on 16/02/15.
 */
public class MainWindow extends JFrame implements MouseListener, MouseMotionListener {

    DrawingPanel canvas;
    PromptPanel promptBar;
    JTable sampleTable;

    JMenuBar menuBar;
    JMenu fileMenu;
    JMenu settings;
    JMenuItem fileMenuSetSaveLocation;
    JMenuItem fileMenuOpenPrompt;

    //the file chooser dialogue for picking where save and open files
    final JFileChooser fc = new JFileChooser();

    public MainWindow(){

        // set window options
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initializeMenuBar();

        // make the drawing panel
        canvas = new DrawingPanel();
        canvas.setMinimumSize(new Dimension(500, 400));
        canvas.setPreferredSize(new Dimension(500, 400));
        canvas.addMouseListener(this);
        canvas.addMouseMotionListener(this);
        canvas.setBorder(new LineBorder(Color.RED, 3));
        add(canvas, BorderLayout.WEST);

        //initialize the prompt bar
        promptBar = new PromptPanel();


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

    public void initializeMenuBar(){
        //make the menubar
        menuBar = new JMenuBar();

        //make the File Menu
        fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        fileMenuOpenPrompt = new JMenuItem("Open Prompt File");
        fileMenu.add(fileMenuOpenPrompt);

        fileMenuSetSaveLocation = new JMenuItem("Set Vector File Save Location");
        fileMenu.add(fileMenuSetSaveLocation);

        //make the settings menu
        settings = new JMenu("Settings");
        menuBar.add(settings);

        setJMenuBar(menuBar);

    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        // create the cache
        canvas.traceCache = new ArrayList<Point2D>();

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        canvas.addPath(canvas.traceCache);
        canvas.repaint();

        ArrayList<Prompt> p = new ArrayList<Prompt>();
        p.add(new Prompt("TEST ONE","theFirstTest.SVG"));
        promptBar.setPrompts(p);
        promptBar.saveToSVG(canvas.curves);
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
        canvas.addCachePoint(newPoint);
        canvas.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {

    }

    //opens and reads in a file
    private void openFile() {
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            BufferedReader in;
            try {
                in = new BufferedReader (new FileReader(file));
                String line = in.readLine();
                while (line!=null && line.trim()!=""){

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
