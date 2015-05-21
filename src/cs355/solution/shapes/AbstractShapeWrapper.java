package cs355.solution.shapes;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import cs355.solution.Model;

public abstract class AbstractShapeWrapper {
	
	// Indirect references to the shape.
	protected Model model = null;
	protected int id = -1;
	
	// Used for selection and modifying the shape.
	private double originalAngle = 0.0;
	private Point2D.Double originalCenter = new Point2D.Double(0.0, 0.0);
	private boolean selected = false;
	private boolean translating = false;
	private boolean rotating = false;
	private boolean resizing = false;
	
	public AbstractShapeWrapper(Model m, int id) {
		this.model = m;
		this.id = id;
	}
	
	public void setColor(Color c) {
		this.model.updateShape(this.id, (s)->{
			s.setColor(c);
		});
	}
	
	public void setSelected(boolean s) {
		this.selected = s;
	}
	
	public void setTranslating(boolean t) {
		this.translating = t;
	}
	
	public void setRotating(boolean r) {
		this.rotating = r;
	}
	
	public void setResizing(boolean r) {
		this.resizing = r;
	}
	
	public boolean isSelected() {
		return false;
	}
	
	public boolean isTranslating() {
		return false;
	}
	
	public boolean isRotating() {
		return false;
	}
	
	public boolean isResizing() {
		return false;
	}
	
	public AffineTransform getObjectToWorldTransform() {
		return this.model.getShapeById(this.id).getObjectToWorldTransform();
	}
	
	public Color getSelectedColor() {
		return new Color(255, 255, 0);
	}
	
	public Shape getSelectedOutlineShape() {
		return this.getGraphicalShape();
	}
	
	public abstract Iterable<Shape> getSelectedHandleShapes();
	
	public abstract void setFirstTwoPoints(Point2D.Double p1, Point2D.Double p2);
	
	public abstract void setFirstThreePoints(Point2D.Double p1, Point2D.Double p2, Point2D.Double p3);
	
	public abstract Shape getGraphicalShape();
}
