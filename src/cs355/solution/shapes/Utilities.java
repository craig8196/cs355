package cs355.solution.shapes;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.Arrays;

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
	
	public static void matrixMultiply(double[][] left, double[][] right, double[][] result) {
		assert left.length != 0;
		assert right.length != 0;
		assert result.length != 0;
		int rowsLeft = left.length;
		int rowsRight = right.length;
		int colsLeft = left[0].length;
		int colsRight = right[0].length;
		assert colsLeft == rowsRight;
		assert rowsLeft == result.length;
		assert colsRight == result[0].length;
		Utilities.zeroMatrix(result);
		for(int i = 0; i < rowsLeft; i++) {
			for(int j = 0; j < colsLeft; j++) {
				for(int k = 0; k < colsRight; k++) {
					result[i][k] += left[i][j]*right[j][k];
				}
			}
		}
	}
	
	
	private static final double IN = 0.0;
	private static final double OUT_NEG = -1.0;
	private static final double OUT_POS = 1.0;
	// Help changes coordinates to demonstrate if the point is in bounds or not.
	private static void modBounds(double[][] p) {
//		System.out.println(p[3][0]);
		for(int i = 0; i < p.length-1; i++) {
			double val = p[i][0];
			if(val < -p[3][0]) {
				val = OUT_NEG;
			} else if(val > p[3][0]) {
				val = OUT_POS;
			} else {
				val = IN;
			}
			p[i][0] = val;
		}
//		Utilities.printMatrix(p);
	}
	
	// p1 and p2 should already be clipped
	public static boolean showLine(double[][] p1, double[][] p2) {
		boolean result = true;
		double[][] p1n = Utilities.copyMatrix(p1);
		double[][] p2n = Utilities.copyMatrix(p2);
		modBounds(p1n);
		modBounds(p2n);
		if(p1n[0][0] == p2n[0][0] && p1n[0][0] != IN) {
			result = false;
		}
		if(p1n[1][0] == p2n[1][0] && p1n[1][0] != IN) {
			result = false;
		}
		if(p1n[2][0] != IN || p2n[2][0] != IN) {
			result = false;
		}
		return result;
	}

	public static void homogenousDivide(double[][] p) {
		for(int i = 0; i < p.length - 1; i++) {
			p[i][0] = p[i][0]/p[3][0];
		}
		p[3][0] = 1.0;
	}
	
	public static void printMatrix(double[][] m) {
		System.out.println("Matrix:");
		for(int i = 0; i < m.length; i++) {
			for(int j = 0; j < m[i].length; j++) {
				System.out.print(m[i][j] + ", ");
			}
			System.out.println();
		}
	}
	
	
	public static void pixelLevelOperation(int width, int height, int[][] in, PixelShader p) {
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				in[y][x] = p.shadePixel(in[y][x]);
			}
		}
	}
	
	public static int clampToRange(int value, int low, int high) {
		if(value > high) {
			value = high;
		} else if (value < low) {
			value = low;
		}
		return value;
	}
	
	/**
	 * 
	 * @param width
	 * @param height
	 * @param in
	 * @param kernel 3x3 rows by cols
	 * @param edgeValue
	 * @return
	 */
	public static int[][] applyKernel(int width, int height, int[][] in, int[][] kernel, int edgeValue) {
		int[][] result = new int[height][width];
		int kx = 1;
		int ky = 1;
		int modLen = 3;
		int[] modifiers = {-1, 0, 1};
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				int accum = 0;
				for(int i = 0; i < modLen; i++) {
					for(int j = 0; j < modLen; j++) {
						int dy = modifiers[i];
						int dx = modifiers[j];
						int ny = dy+y;
						int nx = dx+x;
						int val = edgeValue;
						if(Utilities.inBounds(height, ny) && Utilities.inBounds(width, nx)) {
							val = in[ny][nx];
						}
						accum += val*kernel[ky+dy][kx+dx];
					}
				}
				result[y][x] = accum;
			}
		}
		return result;
	}
	
	public static int[][] findMedians(int width, int height, int[][] in, int edgeValue) {
		int[][] result = new int[height][width];
		int mx = 1;
		int my = 1;
		int modLen = 3;
		int[] modifiers = {-1, 0, 1};
		int[] medians = new int[9];
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				for(int i = 0; i < modLen; i++) {
					for(int j = 0; j < modLen; j++) {
						int dy = modifiers[i];
						int dx = modifiers[j];
						int ny = dy+y;
						int nx = dx+x;
						int val = edgeValue;
						if(Utilities.inBounds(height, ny) && Utilities.inBounds(width, nx)) {
							val = in[ny][nx];
						}
						my = 1+dy;
						mx = 1+dx;
						medians[my+(my+1)*mx] = val;
					}
				}
				Arrays.sort(medians);
				result[y][x] = medians[4];
			}
		}
		return result;
	}
	
	public static boolean inBounds(int length, int index) {
		if(index >= length || index < 0) {
			return false;
		} else {
			return true;
		}
	}
}
