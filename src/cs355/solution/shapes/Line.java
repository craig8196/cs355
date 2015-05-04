package cs355.solution.shapes;

import java.awt.Color;
import cs355.solution.shapes.Point2D;

public class Line extends AbstractShape {

	private Point2D p1 = new Point2D(0.0, 0.0);
	private Point2D p2 = new Point2D(0.0, 0.0);
	
	public Line(Color c) {
		super(c);
	}
	
	public void setFirstPoint(double x, double y) {
		this.p1 = new Point2D(x, y);
	}
	
	public void setSecondPoint(double x, double y) {
		this.p2 = new Point2D(x, y);
	}
	
	public Point2D getFirstPoint() {
		return this.p1;
	}
	
	public Point2D getSecondPoint() {
		return this.p2;
	}
}
