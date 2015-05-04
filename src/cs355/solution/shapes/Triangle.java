package cs355.solution.shapes;

import java.awt.Color;

public class Triangle extends AbstractShape {

	private Point2D p1 = new Point2D(0.0, 0.0);
	private Point2D p2 = new Point2D(0.0, 0.0);
	private Point2D p3 = new Point2D(0.0, 0.0);
	
	public Triangle(Color c) {
		super(c);
	}
	
	public Point2D getFirstPoint() {
		return this.p1;
	}
	
	public Point2D getSecondPoint() {
		return this.p2;
	}
	
	public Point2D getThirdPoint() {
		return this.p3;
	}
	
	public void setFirstPoint(double x, double y) {
		this.p1 = new Point2D(x, y);
	}
	
	public void setSecondPoint(double x, double y) {
		this.p2 = new Point2D(x, y);
	}
	
	public void setThirdPoint(double x, double y) {
		this.p3 = new Point2D(x, y);
	}
}
