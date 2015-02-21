package scribblies;

import javax.swing.*;
import java.awt.*;
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
    private static final String SVG_HEIGHT = "500";
    private static final String SVG_HEADER_TEXT = "<?xml version=\"1.0\"?>\n" +
            "<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.0//EN\"\n" +
            "    \"http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd\">\n" +
            "\n";
    private ArrayList<Prompt> prompts = new ArrayList<Prompt>();
    public int currentPromptIndex;

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
        BufferedWriter out;
        String fileName = prompts.get(currentPromptIndex).getSaveFileName();
        try {
            out = new BufferedWriter (new FileWriter(fileName));
            out.write(SVG_HEADER_TEXT);
            out.write("<svg width="+SVG_WIDTH+" height="+SVG_HEIGHT+">");

            ListIterator<CubicBezierCurve> i = curves.listIterator();
            while (i.hasNext()){
                out.write(i.next().toSvgString(CURVE_COLOR)+"\n");
            }
            out.write("</svg>");
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paintComponent( Graphics g ) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
    }

}
