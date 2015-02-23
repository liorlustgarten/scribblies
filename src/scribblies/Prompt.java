package scribblies;


import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Created by Lior on 21/02/2015.
 */
public class Prompt {
    private String promptMessage;
    private String saveFileName;
    private ArrayList<CubicBezierCurve> curves;
    private ArrayList<ArrayList<Point2D>> rawInput;

    public Prompt (String promptMessage, String saveFileName)
    {
        this.promptMessage = promptMessage;
        this.saveFileName = saveFileName;
        rawInput = new ArrayList<ArrayList<Point2D>>();
    }

    public String getPromptMessage() {
        return promptMessage;
    }

    public void setPromptMessage(String promptMessage) {
        this.promptMessage = promptMessage;
    }

    public String getSaveFileName() {
        return saveFileName;
    }

    public void setSaveFileName(String saveFileName) {
        this.saveFileName = saveFileName;
    }

    public ArrayList<CubicBezierCurve> getCurves() {
        return curves;
    }

    public void setCurves(ArrayList<CubicBezierCurve> curves) {
        this.curves = curves;
    }

    public ArrayList<ArrayList<Point2D>> getRawInput() {
        return rawInput;
    }

    public void setRawInput(ArrayList<ArrayList<Point2D>> rawInput) {
        this.rawInput = rawInput;
    }

    public boolean hasCurves() {
        if (curves==null)
            return false;
        if (curves.isEmpty())
            return false;
        return true;
    }
}
