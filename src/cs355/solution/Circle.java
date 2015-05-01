package cs355.solution;

import java.awt.Color;
import java.awt.geom.Point2D;

public class Circle extends Shape {
	
	private Point2D.Double center = null;
	private double radius = 0.0;

	public Circle(Color c, Point2D.Double center, double radius) {
		super(c);
		this.center = center;
		this.radius = radius;
	}

}
