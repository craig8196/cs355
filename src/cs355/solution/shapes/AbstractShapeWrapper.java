package cs355.solution.shapes;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import cs355.solution.Model;

public abstract class AbstractShapeWrapper {
	
	// Indirect references to the shape.
	protected Model model = null;
	protected int id = -1;
	
	// Used for selection and modifying the shape.
	protected double originalAngle = 0.0;
	protected Point2D.Double originalCenter = new Point2D.Double(0.0, 0.0);
	protected Point2D.Double mouseDown = new Point2D.Double(0.0, 0.0);
	protected Point2D.Double nearestResize = new Point2D.Double(0.0, 0.0);
	protected Point2D.Double furthestResize = new Point2D.Double(0.0, 0.0);
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
		Iterable<Point2D.Double> hc = this.getResizeHandleCenters();
		AffineTransform objToWorld = this.getObjectToWorldTransform();
		AffineTransform worldToObject = this.getWorldToObjectTransform();
		Point2D.Double pObj = new Point2D.Double();
		worldToObject.transform(p, pObj);
		Point2D.Double nearest = Utilities.getNearestPoint(pObj, hc);
		objToWorld.transform(nearest, this.nearestResize);
		Point2D.Double furthest = Utilities.getFurthestPoint(pObj, hc);
		objToWorld.transform(furthest, this.furthestResize);
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
		Point2D.Double md = this.mouseDown;
		Point2D.Double diff = new Point2D.Double(p.x - md.x, p.y - md.y);
		Point2D.Double newNearest = new Point2D.Double(this.nearestResize.x + diff.x, this.nearestResize.y + diff.y);
		this.setFirstTwoPointsWorld(this.furthestResize, newNearest);
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
	
	public Iterable<Shape> getSelectedHandleShapes(double radius) {
		ArrayList<Shape> result = new ArrayList<Shape>();
		double hr = radius;
		double hd = hr*2.0;
		for(Point2D.Double p: getAllHandleCenters(radius)) {
			result.add(new Ellipse2D.Double(p.x - hr, p.y - hr, hd, hd));
		}
		return result;
	}
	
	protected abstract void setFirstTwoPoints(Point2D.Double p1, Point2D.Double p2);
	
	protected abstract void setFirstThreePoints(Point2D.Double p1, Point2D.Double p2, Point2D.Double p3);
	
	public abstract Shape getGraphicalShape();
	
	public abstract Point2D.Double getRotateHandleCenter(double radius);
	public abstract Iterable<Point2D.Double> getResizeHandleCenters();
	
	public boolean hitRotateHandle(Point2D.Double click, double radius) {
		Point2D.Double handleCenter = this.getRotateHandleCenter(radius);
		if(handleCenter == null) {
			return false;
		}
		AffineTransform objToWorld = this.getObjectToWorldTransform();
		objToWorld.transform(handleCenter, handleCenter);
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
		
		if(resizePoints == null) {
			return false;
		}
		
		AffineTransform objToWorld = this.getObjectToWorldTransform();
		for(Point2D.Double p: resizePoints) {
			Point2D.Double pWorld = new Point2D.Double();
			objToWorld.transform(p, pWorld);
			if(Utilities.isPointNearPoint(pWorld, click, radius)) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean hitShape(Point2D.Double p, double tolerance) {
		return this.model.getShapeById(this.id).isPointInShape(p, tolerance);
	}
	
	public Iterable<Point2D.Double> getAllHandleCenters(double radius) {
		ArrayList<Point2D.Double> result = new ArrayList<Point2D.Double>();
		Iterable<Point2D.Double> hcs = this.getResizeHandleCenters();
		Point2D.Double hc = this.getRotateHandleCenter(radius);
		if(hcs != null) {
			for(Point2D.Double p: hcs) {
				if(p != null) {
					result.add(p);
				}
			}
		}
		if(hc != null) {
			result.add(hc);
		}
		return result;
	}
	
	public void resetAllSelectingFeatures() {
		this.resizing = false;
		this.translating = false;
		this.rotating = false;
	}
	
	public void setFirstTwoPointsWorld(Point2D.Double p1, Point2D.Double p2) {
		Point2D.Double np1 = new Point2D.Double();
		Point2D.Double np2 = new Point2D.Double();
		AffineTransform wToO = this.getWorldToObjectTransform();
		wToO.transform(p1, np1);
		wToO.transform(p2, np2);
		this.setFirstTwoPoints(np1, np2);;
	}
	
	public void setFirstThreePointsWorld(Point2D.Double p1, Point2D.Double p2, Point2D.Double p3) {
		Point2D.Double np1 = new Point2D.Double();
		Point2D.Double np2 = new Point2D.Double();
		Point2D.Double np3 = new Point2D.Double();
		AffineTransform wToO = this.getWorldToObjectTransform();
		wToO.transform(p1, np1);
		wToO.transform(p2, np2);
		wToO.transform(p3, np3);
		this.setFirstThreePoints(np1, np2, np3);
	}
	
}
