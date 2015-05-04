package cs355.solution.shapes;

import java.awt.Color;

public class Circle extends AbstractShape {
	
	private Point2D center = new Point2D(0.0, 0.0);
	private double radius = 0.0;

	public Circle(Color c) {
		super(c);
	}
	
	public Point2D getCenterPoint() {
		return this.center;
	}
	
	public double getRadius() {
		return this.radius;
	}
	
	public void setCenterPoint(double x, double y) {
		this.center = new Point2D(x, y);
	}
	
	public void setRadius(double radius) {
		this.radius = radius;
	}
}
