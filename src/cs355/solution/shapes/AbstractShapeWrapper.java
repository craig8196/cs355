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
	protected double originalAngle = 0.0;
	protected Point2D.Double originalCenter = new Point2D.Double(0.0, 0.0);
	protected Point2D.Double mouseDown = new Point2D.Double(0.0, 0.0);
	private boolean selected = false;
	private boolean translating = false;
	private boolean rotating = false;
	private boolean resizing = false;
	
	public AbstractShapeWrapper(Model m, int id) {
		this.model = m;
		this.id = id;
	}
	
	public Color getColor() {
		return this.model.getColorById(this.id);
	}
	
	public void setColor(Color c) {
		this.model.updateShape(this.id, (s)->{
			s.setColor(c);
		});
	}
	
	public void setMouseDown(Point2D.Double p) {
		this.mouseDown = p;
		this.originalAngle = this.model.getShapeById(this.id).getAngle();
		Point2D.Double center = this.model.getShapeById(this.id).getCenter();
		this.originalCenter = new Point2D.Double(center.x, center.y);
	}
	
	public void setAngle(double angle) {
		this.model.updateShape(this.id, (s)->{
			s.setAngle(angle);
		});
	}
	
	public void setMouseUp(Point2D.Double p) {
		if(this.rotating) {
			this.rotate(p);
		} else if(this.translating) {
			System.out.println("Translating line parent.");
			this.translate(p);
		} else if(this.resizing) {
			this.resize(p);
		}
	}
	
	public void rotate(Point2D.Double p) {
		AffineTransform a = this.getWorldToObjectTransform();
		Point2D.Double start = new Point2D.Double();
		Point2D.Double end = new Point2D.Double();
		a.transform(this.mouseDown, start);
		a.transform(p, end);
		double angleDelta = Math.atan2(end.y, end.x) - Math.atan2(start.y, start.x);
		this.setAngle(this.originalAngle + angleDelta);
	}
	
	public void translate(Point2D.Double p) {
		Point2D.Double c = this.originalCenter;
		Point2D.Double md = this.mouseDown;
		Point2D.Double diff = new Point2D.Double(p.x - md.x, p.y - md.y);
		this.model.updateShape(this.id, (s)->{
			s.setCenter(diff.x + c.x, diff.y + c.y);
		});
	}
	
	public void resize(Point2D.Double p) {
		// TODO
	}
	
	public void setSelected(boolean s) {
		if(s) {
			System.out.println("Selected");
		}
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
		return this.selected;
	}
	
	public boolean isTranslating() {
		return this.translating;
	}
	
	public boolean isRotating() {
		return this.rotating;
	}
	
	public boolean isResizing() {
		return this.resizing;
	}
	
	public AffineTransform getObjectToWorldTransform() {
		return this.model.getShapeById(this.id).getObjectToWorldTransform();
	}
	
	public AffineTransform getWorldToObjectTransform() {
		return this.model.getShapeById(this.id).getWorldToObjectTransform();
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
	
	public abstract Point2D.Double getRotateHandleCenter(double radius);
	public abstract Iterable<Point2D.Double> getResizeHandleCenters();
	
	public boolean hitRotateHandle(Point2D.Double click, double radius) {
		Point2D.Double handleCenter = this.getRotateHandleCenter(radius);
		if(handleCenter == null) {
			return false;
		}
		double dx = Math.abs(handleCenter.x - click.x);
		double dy = Math.abs(handleCenter.y - click.y);
		if(dx*dx + dy*dy <= radius*radius) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean hitResizeHandle(Point2D.Double click, double radius) {
		Iterable<Point2D.Double> resizePoints = this.getResizeHandleCenters();
		
		for(Point2D.Double p: resizePoints) {
			if(Utilities.isPointNearPoint(p, click, radius)) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean hitShape(Point2D.Double p, double tolerance) {
		return this.model.getShapeById(this.id).isPointInShape(p, tolerance);
	}
	
}
