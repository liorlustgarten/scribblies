package scribblies;
import bezierfit.BezierFit;
import com.goebl.simplify.Simplify;
import edu.rit.numeric.CurveSmoothing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ListIterator;

/**
 * Created by Lior on 16/02/2015.
 */

public class CurveMaker {
    /**
     * Simplifies a path using the Douglas Peucker algorithm
     * @param inputPath original path in the form of an array of Points
     * @param tolerance tolerance with which the algorithm runs (bigger number means more simplified path)
     * @return simplified list of points. Will return the input list by default if two or less points are provided
     */
    public static Point[] simplifyPath (Point[]inputPath,double tolerance){
        Simplify<Point> simplify = new Simplify<Point>(new Point[0]); //an empty "sample array" is passed to the Simplify constructor for type consistency
        if (inputPath.length<3)
            return inputPath;
        return simplify.simplify(inputPath,tolerance,true);
    }

    /**
     * Takes an array of points and uses the BezierFit class to return a single cubic bezier curve which fits the point
     * @param points list of points to which the curve is fit
     * @return single section CubicBezierCurve
     */
    public static CubicBezierCurve cubicBezierFit (Point[]points)
    {
        BezierFit bezierFit = new BezierFit();
        Point [] controlPoints = bezierFit.bestFit(new ArrayList<Point>(Arrays.asList(points)));

        return new CubicBezierCurve(controlPoints[0],controlPoints[1],controlPoints[2],controlPoints[3]);
    }

    /**
     * Finds the corners in a given path and returns the indexes of any points that are considered a corner
     * @param points
     * @param maxCornerAngle
     * @return
     */
    public static ArrayList<Integer> findCorners (Point[] points, double maxCornerAngle){
        double angleA,angleB,interiorAngle;

        double maxCornerAngleR = Math.toRadians(maxCornerAngle);

        ArrayList<Integer> cornerIndecies = new ArrayList<Integer>();
        //looks at all points except for the first and last (last point is considered a corner)
        for (int i = 1; i< points.length-1; i ++)
        {
            angleA = Math.atan2(points[i-1].getY()-points[i].getY(),points[i-1].getX()-points[i].getX());
            angleB = Math.atan2(points[i+1].getY()-points[i].getY(),points[i+1].getX()-points[i].getX());
//            System.out.println("========angle calc at point: "+i+"=======");
//            System.out.println("Angle back(a): "+Math.toDegrees(angleA)+ " ("+angleA+")");
//            System.out.println("Angle forward(b): "+Math.toDegrees(angleB)+ " ("+angleB+")");
            interiorAngle = Math.abs(angleA-angleB) %(2* Math.PI);
//            System.out.println("interior angle: "+Math.toDegrees(interiorAngle)+ " ("+interiorAngle+")");

            //check for whether the corner passes tolerance. must check both interior and exterior angles
            if (interiorAngle < maxCornerAngleR || interiorAngle > 2*Math.PI-maxCornerAngleR){
                cornerIndecies.add(i);
               // System.out.println("IS CORNER");
            }
           // System.out.println("isn't corner");
          //  System.out.println("***end***");
        }
        cornerIndecies.add(points.length-1);
        return cornerIndecies;
    }

    /**
     * Creates a Bezier Curve from a list of points. The smoothness is 'broken' at corners
     * @param inputPoints
     * @param cornerTolerance
     * @return
     */
    public static CubicBezierCurve makeBezierCurve (Point[]inputPoints,double cornerTolerance){
        //if there are no points or one point passed in, return empty curve
        if (inputPoints.length<2)
              return new CubicBezierCurve();

          double [] inputCoordinatesX = bPointArrayToCoordinateArrayX(inputPoints);
        double [] inputCoordinatesY = bPointArrayToCoordinateArrayY(inputPoints);

        double [] firstControlPointsX = new double [inputPoints.length-1];
        double [] secondControlPointsX = new double [inputPoints.length-1];

        double [] firstControlPointsY = new double [inputPoints.length-1];
        double [] secondControlPointsY = new double [inputPoints.length-1];

        ListIterator<Integer> p= findCorners(inputPoints,cornerTolerance).listIterator();
        int cornerIndex = 0;
        int lastCornerIndex = 0;
        while(p.hasNext()) {
            lastCornerIndex = cornerIndex;
            cornerIndex = p.next();
            CurveSmoothing.computeBezierOpen(inputCoordinatesX, firstControlPointsX, secondControlPointsX, lastCornerIndex, cornerIndex-lastCornerIndex+1);
            CurveSmoothing.computeBezierOpen(inputCoordinatesY, firstControlPointsY, secondControlPointsY, lastCornerIndex, cornerIndex-lastCornerIndex+1);
        }
        return new CubicBezierCurve(inputPoints,coordinateArraysToBPointArray(firstControlPointsX,firstControlPointsY),coordinateArraysToBPointArray(secondControlPointsX,secondControlPointsY));
    }


    public static double[] BPointArrayToDoubleArray (Point[] points){
        double[] ret = new double[points.length*2];
        for (int i = 0; i<points.length; i++) {
            ret[i*2] = points[i].getX();
            ret[i*2+1] = points[i].getY();
        }
        return ret;
    }

    public static Point[] doubleArrayToBPointArray (double [] dbl){
        Point[] ret = new Point[dbl.length/2];
        for (int i = 0; i<dbl.length/2; i++) {
            ret[i] = new Point(dbl[i * 2], dbl[i * 2 + 1]);
        }
        return ret;
    }

    public static double[] bPointArrayToCoordinateArrayX (Point[] points) {
        double[] ret = new double[points.length];
        for (int i = 0; i < points.length; i++) {
            ret[i] = points[i].getX();
        }
        return ret;
    }

    public static double[] bPointArrayToCoordinateArrayY (Point[] points){
        double [] ret = new double [points.length];
        for (int i = 0; i<points.length; i++) {
            ret[i] = points[i].getY();
        }
        return ret;
    }


    public static Point[] coordinateArraysToBPointArray (double xCoordinates[], double yCoordinates[]){
        Point[] ret = new Point[ Math.min(xCoordinates.length,yCoordinates.length)];
        for (int i = 0; i< Math.min(xCoordinates.length,yCoordinates.length);i++){
            ret[i] = new Point(xCoordinates[i],yCoordinates[i]);
        }
      return ret;
    }
}
