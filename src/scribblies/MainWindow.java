package scribblies;

import javafx.stage.FileChooser;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Created by dgli on 16/02/15.
 */
public class MainWindow extends JFrame implements MouseListener, MouseMotionListener ,ActionListener {

    public static final String OPEN_FILE_ACTION_COMMAND = "open";
    public static final String SAVE_LOCATION_ACTION_COMMAND = "save";
    public static final String RAW_RADIO_BUTTON_ACTION_COMMAND = "raw";
    public static final String BEZIER_RADIO_BUTTON_ACTION_COMMAND = "bezier";
    PromptTableModel promptModel;
    DrawingPanel canvas;
    PromptPanel promptBar;
    JTable promptTable;
    ListSelectionModel promptListSelectionModel;

    JMenuBar menuBar;
    JMenu fileMenu;
    JMenu settings;
    JMenuItem fileMenuSetSaveLocation;
    JMenuItem fileMenuOpenPrompt;

    //radio buttons for selecting line type in settings
    static ButtonGroup lineDrawTypeGroup;
    JRadioButtonMenuItem rawInputRadioButton;
    JRadioButtonMenuItem simplePathRadioButton;
    JRadioButtonMenuItem bezierCurveRadioButton;

    //the file chooser dialogue for picking where save and open files
    final JFileChooser fc = new JFileChooser();

    public MainWindow(){
        setTitle("Handwriting Vectorization");

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
        promptBar.init(this);
        add(promptBar, BorderLayout.SOUTH);


        // make prompt table
        String [] columns = {"Prompts"};
        Object [][] sampleData = {};
        promptTable = new JTable(sampleData, columns){
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
                Component comp = super.prepareRenderer(renderer, row, col);
                Object value = getModel().getValueAt(row, col);
                if (promptBar.getCurrentPromptIndex() == row) {
                    comp.setBackground(Color.green);
                } else if(promptBar.getPrompts().get(row).hasCurves()){
                    comp.setBackground(Color.white);
                } else {
                    comp.setBackground(Color.lightGray);
                }
                return comp;
            }
        };

        promptListSelectionModel = promptTable.getSelectionModel();
        promptListSelectionModel.addListSelectionListener(new SharedListSelectionHandler());
        promptTable.setSelectionModel(promptListSelectionModel);


        JScrollPane tableScroll = new JScrollPane(promptTable);
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
        fileMenuOpenPrompt.setActionCommand(OPEN_FILE_ACTION_COMMAND);
        fileMenuOpenPrompt.addActionListener(this);
        fileMenu.add(fileMenuOpenPrompt);

        fileMenuSetSaveLocation = new JMenuItem("Set Vector File Save Location");
        fileMenuSetSaveLocation.setActionCommand(SAVE_LOCATION_ACTION_COMMAND);
        fileMenuSetSaveLocation.addActionListener(this);
        fileMenu.add(fileMenuSetSaveLocation);

        //make the settings menu
        settings = new JMenu("Settings");
        menuBar.add(settings);

        lineDrawTypeGroup = new ButtonGroup();
        rawInputRadioButton = new JRadioButtonMenuItem("Show raw input");
        rawInputRadioButton.setActionCommand(RAW_RADIO_BUTTON_ACTION_COMMAND);
        rawInputRadioButton.addActionListener(this);
        settings.add(rawInputRadioButton);
        lineDrawTypeGroup.add(rawInputRadioButton);

        bezierCurveRadioButton = new JRadioButtonMenuItem("Show bezier curve",true);
        bezierCurveRadioButton.setActionCommand(BEZIER_RADIO_BUTTON_ACTION_COMMAND);
        bezierCurveRadioButton.addActionListener(this);
        settings.add(bezierCurveRadioButton);
        lineDrawTypeGroup.add(bezierCurveRadioButton);

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
                ArrayList<Prompt> inputPrompts = new ArrayList<Prompt>();
                String line = in.readLine();
                while (line!=null && line.trim()!=""){
                    String [] splitLine = line.split(" ");
                    if (splitLine.length>1)
                    inputPrompts.add(new Prompt(splitLine[0],splitLine[1]));
                    line = in.readLine();
                }
                promptBar.setPrompts(inputPrompts);
                promptTable.setModel(new PromptTableModel(promptBar.getPrompts()));
                promptTable.setCellSelectionEnabled(true);
                promptTable.setColumnSelectionInterval(0,0);
                promptTable.setRowSelectionInterval(0,0);
                promptBar.setMessage("Opened: "+file);
                canvas.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

   public String selectDirectory() {
       JFileChooser f = new JFileChooser();
       f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = f.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            return f.getSelectedFile().getAbsolutePath();
        }
       return "";
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String ac = e.getActionCommand();
        if (ac.equalsIgnoreCase(PromptPanel.NEXT_BUTTON_ACTION_COMMAND) && promptBar.getPrompt()!=null){
            promptBar.getPrompt().setCurves(canvas.curves);
            promptBar.getPrompt().setRawInput(canvas.rawInput);
            promptBar.saveToSVG(canvas.curves);
            if(promptBar.getCurrentPromptIndex()<promptBar.getPrompts().size()-1){
                canvas.clear();
                canvas.curves = promptBar.goToPrompt(promptBar.getCurrentPromptIndex() + 1);
                canvas.rawInput = promptBar.getPrompt().getRawInput();
                promptTable.setColumnSelectionInterval(0,0);
                promptTable.setRowSelectionInterval(promptBar.getCurrentPromptIndex(),promptBar.getCurrentPromptIndex());
            }else{
                promptBar.finished();
            }
        } else if (ac.equalsIgnoreCase(PromptPanel.CLEAR_BUTTON_ACTION_COMMAND)){
            canvas.clear();
        } else if (ac.equalsIgnoreCase(PromptPanel.UNDO_BUTTON_ACTION_COMMAND)){
            canvas.undo();
        } else if (ac.equalsIgnoreCase(OPEN_FILE_ACTION_COMMAND)){
            openFile();
        } else if (ac.equalsIgnoreCase(SAVE_LOCATION_ACTION_COMMAND)){
            promptBar.setSaveDirectory(selectDirectory());
        }
        canvas.repaint();
    }

    class SharedListSelectionHandler implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {
            ListSelectionModel lsm = (ListSelectionModel)e.getSource();

            int firstIndex = e.getFirstIndex();
            int lastIndex = e.getLastIndex();
            boolean isAdjusting = e.getValueIsAdjusting();

            if (lsm.isSelectionEmpty()) {
                promptTable.setColumnSelectionInterval(0,0);
                promptTable.setRowSelectionInterval(promptBar.getCurrentPromptIndex(),promptBar.getCurrentPromptIndex());
            } else {
                int minIndex = lsm.getMinSelectionIndex();
                int maxIndex = lsm.getMaxSelectionIndex();
                if (maxIndex!=minIndex)
                {
                    promptTable.setRowSelectionInterval(promptBar.getCurrentPromptIndex(),promptBar.getCurrentPromptIndex());
                }else{
                    if (minIndex!=promptBar.getCurrentPromptIndex()){
                        promptBar.getPrompt().setCurves(canvas.curves);
                        promptBar.saveToSVG(canvas.curves);
                        canvas.clear();
                        canvas.curves=promptBar.goToPrompt(minIndex);
                        canvas.rawInput = promptBar.getPrompt().getRawInput();
                    }
                }
            }
        }
    }
}
