package cs355.solution.shapes;

import java.awt.Color;

public class Rectangle extends AbstractShape {

	private Point2D topLeftCorner = new Point2D(0.0, 0.0);
	private double width = 0.0;
	private double height = 0.0;
	
	public Rectangle(Color c) {
		super(c);
	}
	
	public double getHeight() {
		return this.height;
	}
	
	public Point2D getTopLeftPoint() {
		return this.topLeftCorner;
	}
	
	public double getWidth() {
		return this.width;
	}
	
	public void setHeight(double height) {
		this.height = height;
	}
	
	public void setTopLeftCorner(double x, double y) {
		this.topLeftCorner = new Point2D(x, y);
	}
	
	public void setWidth(double width) {
		this.width = width;
	}
}
