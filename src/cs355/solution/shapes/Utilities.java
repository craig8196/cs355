package cs355.solution.shapes;

import java.awt.geom.Point2D;

public class Utilities {
	public static double dot(Point2D.Double p1, Point2D.Double p2) {
		return p1.x*p2.x + p1.y*p2.y;
	}
	
	public static Point2D.Double normalize(Point2D.Double vector) {
		double length = Math.sqrt(vector.x*vector.x + vector.y*vector.y);
		Point2D.Double result = new Point2D.Double();
		if(length != 0.0) {
			result.x = vector.x/length;
			result.y = vector.y/length;
		}
		return result;
	}
}
