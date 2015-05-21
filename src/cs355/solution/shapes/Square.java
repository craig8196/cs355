package cs355.solution.shapes;

import java.awt.Color;
import java.awt.geom.Point2D;

public class Square extends AbstractShape {

	private double halfSide = 0.0;
	
	public Square() {
		super();
	}
	
	public Square(Color c, double x, double y) {
		super(c, x, y);
	}
	
	public double getHalfSide() {
		return this.halfSide;
	}
	
	public double getSide() {
		return this.halfSide*2.0;
	}
	
	public void setSide(double side) {
		this.halfSide = side/2.0;
	}

	@Override
	public boolean isPointInShape(Point2D.Double p, double tolerance) {
		Point2D.Double objSpace = new Point2D.Double();
		this.getWorldToObjectTransform().transform(p, objSpace);
		if(Math.abs(objSpace.x) <= this.halfSide && Math.abs(objSpace.y) <= this.halfSide) {
			return true;
		} else {
			return false;
		}
	}
}
