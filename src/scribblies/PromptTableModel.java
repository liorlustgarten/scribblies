package scribblies;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

/**
 * Created by Lior on 21/02/2015.
 */
public class PromptTableModel extends AbstractTableModel{

    ArrayList<Prompt> prompts;

    String[] columnNames = {"prompts"};

    public PromptTableModel (ArrayList<Prompt> prompts){
        this.prompts = prompts;
    }

    @Override
    public String getColumnName(int col){
        return columnNames[col];
    }

    @Override
    public int getRowCount() {
        return prompts.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return prompts.get(rowIndex).getPromptMessage();
    }

    @Override
    public boolean isCellEditable(int row, int col){
        return false;
    }
}
