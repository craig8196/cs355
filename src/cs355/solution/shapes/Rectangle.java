package cs355.solution.shapes;

import java.awt.Color;
import java.awt.geom.Point2D;

public class Rectangle extends AbstractShape {
	
	private double halfWidth = 0.0;
	private double halfHeight = 0.0;
	
	public Rectangle(Color c, double x, double y) {
		super(c, x, y);
	}
	
	public double getHalfHeight() {
		return this.halfHeight;
	}
	
	public double getHeight() {
		return this.halfHeight*2.0;
	}
	
	public double getHalfWidth() {
		return this.halfWidth;
	}
	
	public double getWidth() {
		return this.halfWidth*2.0;
	}
	
	public void setHeight(double height) {
		this.halfHeight = height/2.0;
	}
	
	public void setWidth(double width) {
		this.halfWidth = width/2.0;
	}

	@Override
	public boolean isPointInShape(Point2D.Double p) {
		Point2D.Double objPoint = new Point2D.Double(0.0, 0.0);
		this.getWorldToObjectTransform().transform(new Point2D.Double(p.x, p.y), objPoint);
		if(Math.abs(objPoint.x) <= this.halfWidth && Math.abs(objPoint.y) <= this.halfHeight) {
			return true;
		} else {
			return false;
		}
	}
}
