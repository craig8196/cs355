package cs355.solution.shapes;

import java.awt.Color;

public class Square extends AbstractShape {

	private Point2D topLeftPoint = new Point2D(0.0, 0.0);
	private double side = 0.0;
	
	public Square(Color c) {
		super(c);
	}
	
	public void setTopLeftPoint(double x, double y) {
		this.topLeftPoint = new Point2D(x, y);
	}
	
	public void setSide(double side) {
		this.side = side;
	}
	
	public Point2D getTopLeftPoint() {
		return this.topLeftPoint;
	}
	
	public double getSide() {
		return this.side;
	}
}
