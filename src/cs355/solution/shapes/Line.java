package cs355.solution.shapes;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class Line extends AbstractShape {

	private Point2D.Double p1 = new Point2D.Double(0.0, 0.0);
	private Point2D.Double p2 = new Point2D.Double(0.0, 0.0);
	
	public Line(Color c, double x, double y) {
		super(c, x, y);
	}
	
	public void setFirstPoint(double x, double y) {
		this.p1 = new Point2D.Double(x, y);
	}
	
	public void setSecondPoint(double x, double y) {
		this.p2 = new Point2D.Double(x, y);
	}
	
	public Point2D.Double getFirstPoint() {
		return this.p1;
	}
	
	public Point2D.Double getSecondPoint() {
		return this.p2;
	}

	// Helper function to isPointInShape.
	private boolean isPointBetween(Point2D.Double p, Point2D.Double norm) {
		double minX = Math.min(Math.min(p1.x + norm.x*4, p1.x - norm.x*4), 
							   Math.min(p2.x + norm.x*4, p2.x - norm.x*4));
		double maxX = Math.max(Math.max(p1.x + norm.x*4, p1.x - norm.x*4), 
				   			   Math.max(p2.x + norm.x*4, p2.x - norm.x*4));
		double minY = Math.min(Math.min(p1.y + norm.y*4, p1.y - norm.y*4), 
				   			   Math.min(p2.y + norm.y*4, p2.y - norm.y*4));
		double maxY = Math.max(Math.max(p1.y + norm.y*4, p1.y - norm.y*4), 
							   Math.max(p2.y + norm.y*4, p2.y - norm.y*4));
		
		if(p.x > minX && p.x < maxX && p.y > minY && p.y < maxY) {
			return true;
		} else {
			return false;
		}
	}
	
	// Helper function to isPointInShape.
	private boolean isPointNear(Point2D.Double p, Point2D.Double o, double tolerance) {
		double dx = Math.abs(p.x - o.x);
		double dy = Math.abs(p.y - o.y);
		if(dx*dx + dy*dy <= tolerance*tolerance) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public boolean isPointInShape(Point2D.Double p, double tolerance) {
		Point2D.Double perpVector = new Point2D.Double(this.p1.y - this.p2.y, this.p2.x - this.p1.x);
		double length = Math.sqrt(perpVector.x*perpVector.x + perpVector.y*perpVector.y);
		double epsilon = 10e-5;
		
		boolean isPointOnLine = false;
		if(length > epsilon) {
			Point2D.Double normPerpVector = new Point2D.Double(perpVector.x/length, perpVector.y/length);
			double lineDistanceFromOrigin = this.p1.x*normPerpVector.x + this.p1.y*normPerpVector.y;
			double pointDistanceFromOrigin = p.x*normPerpVector.x + p.y*normPerpVector.y;
			isPointOnLine = Math.abs(lineDistanceFromOrigin - pointDistanceFromOrigin) <= tolerance;
			if(isPointOnLine && this.isPointBetween(p, normPerpVector)) {
				return true;
			}
		} else {
			isPointOnLine = true;
		}
		
		// If it isn't on the line we're done.
		if(!isPointOnLine) {
			return false;
		}
		
		// Check to see if the point is near one of the two end points
		if(this.isPointNear(p, this.p1, tolerance) || this.isPointNear(p, this.p2, tolerance)) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public AffineTransform getObjectToWorldTransform() {
		return new AffineTransform();
	}
	
	@Override
	public AffineTransform getWorldToObjectTransform() {
		return new AffineTransform();
	}
}
