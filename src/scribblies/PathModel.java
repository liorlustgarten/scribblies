package scribblies;

import javax.swing.table.AbstractTableModel;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Created by dgli on 16/02/15.
 */
public class PathModel extends AbstractTableModel {

    ArrayList<Point2D> path;

    String[] columnNames = {"X", "Y"};

    public PathModel (ArrayList<Point2D> path){
        this.path = path;
    }

    public String getColumnName(int col) {
        return columnNames[col].toString();
    }

    public int getRowCount() {
        return path.size();
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public Object getValueAt(int row, int col) {
        if(col == 0){
            return path.get(row).getX();

        }else if(col == 1){
            return path.get(row).getY();

        }

        return -999999;
    }

    public boolean isCellEditable(int row, int col) {
        return false;
    }

    public void setValueAt(Object value, int row, int col) {

    }

}
