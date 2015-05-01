package cs355.solution;

import java.awt.Color;
import java.awt.geom.Point2D;

public class Triangle extends Shape {

	private Point2D.Double p1 = null;
	private Point2D.Double p2 = null;
	private Point2D.Double p3 = null;
	
	public Triangle(Color c, Point2D.Double p1, Point2D.Double p2, Point2D.Double p3) {
		super(c);
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
	}

}
