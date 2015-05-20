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
	
	public Point2D.Double getCenter() {
		return this.center;
	}
	
	public void setColor(Color c) {
		if(c != null) {
			this.color = c;
		}
	}
	
	public abstract boolean isPointInShape(Point2D.Double p, double tolerance);
	
	public void setCenter(double x, double y) {
		this.center = new Point2D.Double(x, y);
	}
	
	public void setAngle(double a) {
		this.angle = a;
	}
	
	public AffineTransform getObjectToWorldTransform() {
		AffineTransform result = new AffineTransform();
		AffineTransform translate = new AffineTransform(1.0, 0.0, 0.0, 1.0, this.center.x, this.center.y);
		double cos = Math.cos(this.angle);
		double sin = Math.sin(this.angle);
		AffineTransform rotate = new AffineTransform(cos, sin, -sin, cos, 0.0, 0.0);
		result.concatenate(translate);
		result.concatenate(rotate);
		return result;
	}
	
	public AffineTransform getWorldToObjectTransform() {
		AffineTransform result = new AffineTransform();
		AffineTransform inverseTranslate = new AffineTransform(1.0, 0.0, 0.0, 1.0, -this.center.x, -this.center.y);
		double cos = Math.cos(this.angle);
		double sin = Math.sin(this.angle);
		AffineTransform inverseRotate = new AffineTransform(cos, -sin, sin, cos, 0.0, 0.0);
		result.concatenate(inverseRotate);
		result.concatenate(inverseTranslate);
		return result;
	}
	
}
