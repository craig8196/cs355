package cs355.solution.shapes;

import java.awt.geom.Ellipse2D;
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
	
	public static Ellipse2D.Double createCircleAtEndOfVector(Point2D.Double vector, double radius) {
		double diameter = radius*2.0;
		Point2D.Double center = Utilities.getCircleCenterAtEndOfVector(vector, radius);
		return new Ellipse2D.Double(center.x - radius, center.y - radius, diameter, diameter);
	}
	
	public static Point2D.Double getCircleCenterAtEndOfVector(Point2D.Double vector, double radius) {
		Point2D.Double norm = Utilities.normalize(vector);
		return new Point2D.Double(vector.x + norm.x*radius, vector.y + norm.y*radius);
	}
	
	public static int max(int[] values) {
		int result = values[0];
		for(int i = 1; i < values.length; i++) {
			if(values[i] > result) {
				result = values[i];
			}
		}
		return result;
	}
	
	public static Point2D.Double getObjectSpaceVectorToRotationHandleCenter(AbstractShape s, double radius) {
		Point2D.Double result = new Point2D.Double();
		
		if(s instanceof Triangle || s instanceof Circle) {
			result = null;
		} else if(s instanceof Square) {
			Square sq = (Square)s;
			result.x = sq.getHalfSide() + radius*3.0;
		} else if(s instanceof Rectangle) {
			Rectangle r = (Rectangle)s;
			result.x = r.getHalfWidth() + radius*3.0;
		} else if(s instanceof Ellipse) {
			Ellipse e = (Ellipse)s;
			result.x = e.getRadiusX() + radius*3.0;
		} else if(s instanceof Triangle) {
			Triangle t = (Triangle)s;
			int maxX = Utilities.max(t.getXCoords());
			result.x = maxX + radius*3.0;
		}
		
		return result;
	}
	
}
