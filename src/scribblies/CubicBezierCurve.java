package scribblies;
import lombok.Getter;
import lombok.Setter;



/**
 * Created by Lior on 16/02/2015.
 */
public class CubicBezierCurve {
    @Getter @Setter int numPoints = 0;
    @Getter @Setter private Point[] coordinates;
    @Getter @Setter private Point[] firstControlPoints;
    @Getter @Setter private Point[] secondControlPoints;

    public CubicBezierCurve() {
    }



    public CubicBezierCurve(Point[] coord, Point[] firstCPs, Point[] secondCPs) {
        coordinates = coord;
        numPoints = coordinates.length;
        firstControlPoints = firstCPs;
        secondControlPoints = secondCPs;
    }

    /**
     * Constructor for Bezier Curve segment with only one section
     * @param coord1 start of the curve
     * @param firstCP point which defines slope for the line at coord1
     * @param secondCP point which defines slope for the line at coord2
     * @param coord2 end of the curve
     */
    public CubicBezierCurve(Point coord1, Point firstCP, Point secondCP, Point coord2) {
        coordinates = new Point[2];
        coordinates[0]= coord1;
        coordinates[1]= coord2;

        numPoints = coordinates.length;

        firstControlPoints = new Point[1];
        firstControlPoints[0] = firstCP;

        secondControlPoints = new Point[1];
        secondControlPoints[0] = firstCP;
    }

    public boolean isEmpty(){
        if (numPoints == 0)
            return true;
        return false;
    }

    public String toSvgString(String color){
        if (isEmpty())
            return "<empty curve>";

        String ret ="<path d=\"M";
        ret = ret + coordinates[0].getX() + " " + coordinates[0].getY();

        //C denotes cubic bezier curve. Could be changed to Q for quadratic
        ret = ret + " C ";

        for (int i=1; i<numPoints; i ++){
            ret = ret + " " + firstControlPoints[i-1].getX() + " "+ firstControlPoints[i-1].getY()
                    + " " + secondControlPoints[i-1].getX() + " "+ secondControlPoints[i-1].getY()
                    + " " + coordinates[i].getX() + " " + coordinates[i].getY();
        }

        ret = ret + "\" style=\"stroke: "+color+"; fill: none;\"/>";
        return ret;
    }
}
