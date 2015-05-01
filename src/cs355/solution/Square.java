package cs355.solution;

import java.awt.Color;
import java.awt.geom.Point2D;

public class Square extends Shape {

	private Point2D.Double corner = null;
	private double side = 0.0;
	
	public Square(Color c, Point2D.Double corner, double side) {
		super(c);
		this.corner = corner;
		this.side = side;
	}

}
