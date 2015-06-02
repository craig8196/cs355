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
		
		if(s instanceof Line || s instanceof Circle) {
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
	
	public static double signOf(double x) {
		if(x >= 0.0) {
			return 1.0;
		} else {
			return -1.0;
		}
	}

	public static boolean isPointNearPoint(Point2D.Double p1, Point2D.Double p2, double radius) {
		double dx = p1.x - p2.x;
		double dy = p1.y - p2.y;
		if(dx*dx + dy*dy <= radius*radius) {
			return true;
		} else {
			return false;
		}
	}

	public static Point2D.Double getNearestPoint(Point2D.Double p, Iterable<Point2D.Double> hc) {
		Point2D.Double nearest = null;
		double smallest = Double.MAX_VALUE;
		for(Point2D.Double h: hc) {
			double dx = h.x - p.x;
			double dy = h.y - p.y;
			double distSquared = dx*dx + dy*dy;
			if(distSquared < smallest) {
				nearest = h;
				smallest = distSquared;
			}
		}
		return nearest;
	}
	
	public static Point2D.Double getFurthestPoint(Point2D.Double p, Iterable<Point2D.Double> hc) {
		Point2D.Double furthest = null;
		double largest = -Double.MAX_VALUE;
		for(Point2D.Double h: hc) {
			double dx = h.x - p.x;
			double dy = h.y - p.y;
			double distSquared = dx*dx + dy*dy;
			if(distSquared > largest) {
				furthest = h;
				largest = distSquared;
			}
		}
		return furthest;
	}
	
	public static double[][] new3dIdentityMatrix() {
		return new double[][]{
			{1.0, 0.0, 0.0, 0.0},
			{0.0, 1.0, 0.0, 0.0},
			{0.0, 0.0, 1.0, 0.0},
			{0.0, 0.0, 0.0, 1.0},
		};
	}
	
	// Copy a non-jagged 2d array.
	public static double[][] copyMatrix(double[][] matrix) {
		int rows = matrix.length;
		int cols = matrix[0].length;
		double[][] result = new double[rows][cols];
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				result[i][j] = matrix[i][j];
			}
		}
		return result;
	}
	
	public static void zeroMatrix(double[][] matrix) {
		for(int i = 0; i < matrix.length; i++) {
			for(int j = 0; j < matrix[i].length; j++) {
				matrix[i][j] = 0.0;
			}
		}
	}
	
	public static double[][] matrixMultiply(double[][] left, double[][] right) {
		assert left.length != 0;
		assert right.length != 0;
		int rowsLeft = left.length;
		int rowsRight = right.length;
		int colsLeft = left[0].length;
		int colsRight = right[0].length;
		assert colsLeft == rowsRight;
		double[][] result = new double[rowsLeft][colsRight];
		Utilities.zeroMatrix(result);
		for(int i = 0; i < rowsLeft; i++) {
			for(int j = 0; j < colsLeft; j++) {
				for(int k = 0; k < colsRight; k++) {
					result[i][k] += left[i][j]*right[j][k];
				}
			}
		}
		return result;
	}
	
}
