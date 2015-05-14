package cs355.solution.shapes;

import java.awt.Color;
import java.awt.geom.Point2D;

public class Ellipse extends AbstractShape {

	private double radiusX = 0.0;
	private double radiusY = 0.0;
	
	public Ellipse(Color c, double x, double y) {
		super(c, x, y);
	}
	
	public double getRadiusX() {
		return this.radiusX;
	}
	
	public double getRadiusY() {
		return this.radiusY;
	}
	
	public void setRadiusX(double xr) {
		this.radiusX = xr;
	}
	
	public void setRadiusY(double yr) {
		this.radiusY = yr;
	}

	@Override
	public boolean isPointInShape(Point2D.Double p, double tolerance) {
		Point2D.Double objSpace = new Point2D.Double();
		this.getWorldToObjectTransform().transform(p, objSpace);
		if((  ((objSpace.x*objSpace.x)/(this.radiusX*this.radiusX))
			+ ((objSpace.y*objSpace.y)/(this.radiusY*this.radiusY))  ) <= 1.0) {
			return true;
		}
		return false;
	}
}
