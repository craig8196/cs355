package cs355.solution;

import java.awt.Color;
import java.awt.geom.Point2D;

public class Line extends Shape {

	private Point2D.Double p1 = null;
	private Point2D.Double p2 = null;
	
	public Line(Color c, Point2D.Double p1, Point2D.Double p2) {
		super(c);
		this.p1 = p1;
		this.p2 = p2;
	}
}
