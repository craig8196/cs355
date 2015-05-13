package cs355.solution.shapes;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public abstract class AbstractShape {
	private Color color = null;
	protected Point2D.Double center = null; // World coordinates.
	private double angle = 0.0; // Radians.
	
	public AbstractShape() {
		this.setColor(new Color(128, 128, 128));
		this.setCenter(0.0, 0.0);
	}
	
	public AbstractShape(Color c, double x, double y) {
		this.setColor(c);
		this.setCenter(x, y);
	}
	
	public Color getColor() {
		return this.color;
	}
	
	public double getAngle() {
		return this.angle;
	}
	
	public Point2D getCenter() {
		return this.center;
	}
	
	public void setColor(Color c) {
		if(c != null) {
			this.color = c;
		}
	}
	
	public abstract boolean isPointInShape(Point2D.Double p);
	
	public void setCenter(double x, double y) {
		this.center = new Point2D.Double(x, y);
	}
	
	public AffineTransform getObjectToWorldTransform() {
		AffineTransform result = new AffineTransform();
		result.translate(this.center.x, this.center.y);
		result.rotate(this.angle);
		return result;
	}
	
	public AffineTransform getWorldToObjectTransform() {
		AffineTransform result = new AffineTransform();
		result.rotate(-this.angle);
		result.translate(-this.center.x, -this.center.y);
		return result;
	}
	
}
