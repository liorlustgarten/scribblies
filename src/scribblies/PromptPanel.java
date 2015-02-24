package scribblies;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.ListIterator;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Lior on 21/02/2015.
 */
public class PromptPanel extends JPanel {
    private static final String CURVE_COLOR = "black";
    private static final String SVG_WIDTH = "500";
    private static final String SVG_HEIGHT = "400";
    private static final String SVG_HEADER_TEXT = "<?xml version=\"1.0\"?>\n" +
            "<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.0//EN\"\n" +
            "    \"http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd\">\n" +
            "\n";
    private static final String INITIAL_INSTRUCTION="Please load a prompt file (file->open prompt file)";
    private static final String NEXT_BUTTON_TEXT = "Next";
    private static final String CLEAR_BUTTON_TEXT = "Clear";
    private static final String UNDO_BUTTON_TEXT = "Undo";
    public static final String NEXT_BUTTON_ACTION_COMMAND = "next";
    public static final String CLEAR_BUTTON_ACTION_COMMAND = "clear";
    public static final String UNDO_BUTTON_ACTION_COMMAND = "undo";;
    private ArrayList<Prompt> prompts = new ArrayList<Prompt>();
    private int currentPromptIndex;
    private String saveDirectory="";

    public JPanel instructionPanel;
    public JLabel instructionLabel;

    public JPanel buttonPanel;
    public JButton nextButton;
    public JButton clearButton;
    public JButton undoButton;

    public PromptPanel (){
        super(new FlowLayout(FlowLayout.LEFT));
    }

    public void init(ActionListener l){

        buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        nextButton = new JButton(NEXT_BUTTON_TEXT);
        nextButton.addActionListener(l);
        buttonPanel.add(nextButton);

        buttonPanel.add(new JSeparator(SwingConstants.VERTICAL));

        clearButton = new JButton(CLEAR_BUTTON_TEXT);
        clearButton.addActionListener(l);
        buttonPanel.add(clearButton);

        undoButton = new JButton(UNDO_BUTTON_TEXT);
        undoButton.addActionListener(l);
        buttonPanel.add(undoButton);
        add(buttonPanel);


        instructionLabel = new JLabel(INITIAL_INSTRUCTION);
        instructionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        instructionPanel.add(instructionLabel);
        add(instructionPanel);
    }

    public void setPrompts (ArrayList<Prompt> prompts){
        this.prompts = prompts;
        currentPromptIndex = 0;
    }

    public String currentPromptText(){
        if (prompts==null)
            return "Prompt List null";
        if(prompts.size()==0)
            return "Prompt List Empty";
        return prompts.get(currentPromptIndex).getPromptMessage();
    }

    public void saveToSVG(ArrayList<CubicBezierCurve> curves){
        if(prompts == null)
            return;
        if(curves.isEmpty())//won't save a blank file
            return;
        BufferedWriter out;
        try {
            out = new BufferedWriter (new FileWriter(getSaveLocation(-1)));
            out.write(SVG_HEADER_TEXT);
            out.write("<svg width=\""+SVG_WIDTH+"\" height=\""+SVG_HEIGHT+"\">");
            out.newLine();
            for(CubicBezierCurve c:curves){
                out.write(c.toSvgString(CURVE_COLOR));
                out.newLine();
            }
            out.newLine();
            out.write("</svg>");
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paintComponent( Graphics g ) {
        super.paintComponent(g);
    }

    public ArrayList<Prompt> getPrompts() {
        return prompts;
    }

    public int getCurrentPromptIndex() {
        return currentPromptIndex;
    }

    /**
     * gets save location based on the currently selected prompt.
     * will returned the path shortened to a maximum number of characters
     * @param maxStringLength longest the string is allowed to be (-1 for no limit)
     * @return the absolute path of the save location
     */
    public String getSaveLocation(int maxStringLength) {
        if (saveDirectory!=null && !saveDirectory.isEmpty()){
            String fileName="\\"+prompts.get(currentPromptIndex).getSaveFileName();
            if (maxStringLength == -1 || (saveDirectory+fileName).length()<=maxStringLength)
                return saveDirectory+fileName;
            else
                return saveDirectory.substring(0,maxStringLength-fileName.length()-3)+"..."+fileName;
        }
        return prompts.get(currentPromptIndex).getSaveFileName();
    }

    public void setSaveDirectory(String saveDirectory) {
        this.saveDirectory = saveDirectory;
    }

    public ArrayList<CubicBezierCurve> goToPrompt(int index) {
        if(index>prompts.size())
            return null;

        setCurrentPromptIndex(index);
        setPromptMessage();

        if (prompts.get(currentPromptIndex).getCurves()==null)
            return new ArrayList<CubicBezierCurve>();
        return prompts.get(currentPromptIndex).getCurves();
    }

    public void setCurrentPromptIndex(int currentPromptIndex) {
        this.currentPromptIndex = currentPromptIndex;
    }

    public void finished() {
        instructionLabel.setText("You have reached the end of the file.");
    }

    public void setPromptMessage(){
        setMessage("Saving to: "+ getSaveLocation(65));
    }

    public Prompt getPrompt(){
        if (prompts==null || prompts.isEmpty())
            return null;
        return prompts.get(currentPromptIndex);
    }

    public void setMessage(String text){
        instructionLabel.setText(text);
    }
}
