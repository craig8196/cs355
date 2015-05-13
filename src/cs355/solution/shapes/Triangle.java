package cs355.solution.shapes;

import java.awt.Color;
import java.awt.geom.Point2D;

public class Triangle extends AbstractShape {

	private Point2D.Double p1 = new Point2D.Double(0.0, 0.0);
	private Point2D.Double p2 = new Point2D.Double(0.0, 0.0);
	private Point2D.Double p3 = new Point2D.Double(0.0, 0.0);
	
	public Triangle(Color c, double x, double y) {
		super(c, x, y);
	}
	
	public Point2D.Double getFirstPoint() {
		return this.p1;
	}
	
	public Point2D.Double getSecondPoint() {
		return this.p2;
	}
	
	public Point2D.Double getThirdPoint() {
		return this.p3;
	}
	
	public void setFirstPoint(double x, double y) {
		this.p1 = new Point2D.Double(x, y);
	}
	
	public void setSecondPoint(double x, double y) {
		this.p2 = new Point2D.Double(x, y);
	}
	
	public void setThirdPoint(double x, double y) {
		this.p3 = new Point2D.Double(x, y);
	}

	@Override
	public boolean isPointInShape(Point2D.Double p) {
		Point2D.Double objSpace = new Point2D.Double();
		this.getWorldToObjectTransform().transform(p, objSpace);
		Point2D.Double[] pts = new Point2D.Double[3];
		pts[0] = this.p1;
		pts[1] = this.p2;
		pts[2] = this.p3;
		double sign = 0.0; // No sign.
		for(int i = 0; i < pts.length; i++) {
			Point2D.Double p1 = pts[i];
			Point2D.Double p2 = null;
			if(i == pts.length - 1) {
				p2 = pts[0];
			} else {
				p2 = pts[i+1];
			}
			Point2D.Double objSpacePrime = new Point2D.Double(objSpace.x - p1.x, objSpace.y - p1.y);
			Point2D.Double perpVec = new Point2D.Double(p1.y - p2.y, p2.x - p1.x);
			double side = Utilities.dot(perpVec, objSpacePrime);
			if(sign == 0.0) {
				sign = side;
			} else {
				if((sign < 0.0 && side > 0.0) || (sign > 0.0 && side < 0.0)) {
					return false;
				}
			}
		}
		return true;
	}
}
