package cs355.solution.shapes;

import java.awt.Color;
import java.awt.geom.Point2D;

public class Circle extends AbstractShape {
	
	private double radius = 0.0;
	
	public Circle(Color c, double x, double y) {
		super(c, x, y);
	}
	
	public double getRadius() {
		return this.radius;
	}
	
	public void setRadius(double radius) {
		this.radius = radius;
	}
	
	public double getDiameter() {
		return this.radius*2.0;
	}

	@Override
	public boolean isPointInShape(Point2D.Double p) {
		Point2D.Double objSpace = new Point2D.Double();
		this.getWorldToObjectTransform().transform(p, objSpace);
		if((objSpace.x*objSpace.x + objSpace.y*objSpace.y) <= (this.radius*this.radius)) {
			return true;
		} else {
			return false;
		}
	}

}
