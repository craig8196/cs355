package cs355.solution.shapes;

import java.awt.Color;

public class Ellipse extends AbstractShape {

	private Point2D center = new Point2D(0.0, 0.0);
	private double w = 0.0;
	private double h = 0.0;
	
	public Ellipse(Color c) {
		super(c);
	}
	
	public Point2D getCenterPoint() {
		return this.center;
	}
	
	public double getHeight() {
		return this.h;
	}
	
	public double getWidth() {
		return this.w;
	}
	
	public void setCenterPoint(double x, double y) {
		this.center = new Point2D(x, y);
	}
	
	public void setHeight(double height) {
		this.h = height;
	}
	
	public void setWidth(double width) {
		this.w = width;
	}
}
