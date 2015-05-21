package cs355.solution;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Observer;

import cs355.solution.shapes.AbstractShape;
import cs355.solution.shapes.AbstractShapeWrapper;
import cs355.solution.shapes.Circle;
import cs355.solution.shapes.CircleWrapper;
import cs355.solution.shapes.Ellipse;
import cs355.solution.shapes.EllipseWrapper;
import cs355.solution.shapes.Line;
import cs355.solution.shapes.LineWrapper;
import cs355.solution.shapes.Rectangle;
import cs355.solution.shapes.RectangleWrapper;
import cs355.solution.shapes.ShapeType;
import cs355.solution.shapes.Square;
import cs355.solution.shapes.SquareWrapper;
import cs355.solution.shapes.Triangle;
import cs355.solution.shapes.TriangleWrapper;
import cs355.solution.shapes.UpdateShape;
import cs355.solution.shapes.Utilities;

public class ModelWrapper {
	
	public static final double TOLERANCE = 4.0; // In view units.
	public static final double HANDLE_RADIUS = 10.0; // In view units.
	
	public static final double WORLD_X_MIN = 0.0;
	public static final double WORLD_X_MAX = 2048.0;
	public static final double WORLD_Y_MIN = 0.0;
	public static final double WORLD_Y_MAX = 2048.0;
	public static final double VIEW_WIDTH = 512.0; // In world units.
	public static final double VIEW_HEIGHT = 512.0; // In world units.
	
	private Model model = null;
	
	public ModelWrapper(Model m) {
		this.model = m;
	}
	
	public AbstractShapeWrapper hitShape(double x, double y) {
		return this.getShapeWrapperById(this.model.hitShape(x, y, ModelWrapper.TOLERANCE));
	}
	
	public boolean hitShapeRotateHandle(int handle, Point2D.Double p, double radius) {
		AbstractShape s = this.getShapeById(handle);
		Point2D.Double objSpace = new Point2D.Double();
		s.getWorldToObjectTransform().transform(p, objSpace);
		Point2D.Double rotatePoint = Utilities.getObjectSpaceVectorToRotationHandleCenter(s, radius);
		if(rotatePoint == null) {
			return false;
		}
		double dx = Math.abs(rotatePoint.x - objSpace.x);
		double dy = Math.abs(rotatePoint.y - objSpace.y);
		if(dx*dx + dy*dy <= radius*radius) {
			return true;
		} else {
			return false;
		}
	}
	
	public void setFirstTwoPoints(int handle, Point2D.Double p1, Point2D.Double p2) {
		this.model.updateShape(handle, (s)->{
			if(s instanceof Line) {
				Line l = (Line)s;
				l.setFirstPoint(p1.x, p1.y);
				l.setSecondPoint(p2.x, p2.y);
			} else if(s instanceof Square) {
				Square sq = (Square)s;
				double deltaX = p2.x - p1.x;
				double deltaY = p2.y - p1.y;
				double side = 0.0;
				if(Math.abs(deltaX) < Math.abs(deltaY)) {
					side = Math.abs(deltaX);
				} else {
					side = Math.abs(deltaY);
				}
				double x = Math.min(p1.x, p1.x + side*this.signOf(deltaX)) + side/2.0;
				double y = Math.min(p1.y, p1.y + side*this.signOf(deltaY)) + side/2.0;
				
				sq.setCenter(x, y);
				sq.setSide(side);
			} else if(s instanceof Rectangle) {
				Rectangle r = (Rectangle)s;
				double x = (p1.x + p2.x)/2.0;
				double y = (p1.y + p2.y)/2.0;
				double width = Math.abs(p1.x - p2.x);
				double height = Math.abs(p1.y - p2.y);
				r.setCenter(x, y);
				r.setWidth(width);
				r.setHeight(height);
			} else if(s instanceof Circle) {
				Circle c = (Circle)s;
				double deltaX = p2.x - p1.x;
				double deltaY = p2.y - p1.y;
				double radius = Math.min(Math.abs(deltaX), Math.abs(deltaY))/2.0;
				double cx = Math.min(p1.x, p1.x + radius*2.0*this.signOf(deltaX)) + radius;
				double cy = Math.min(p1.y, p1.y + radius*2.0*this.signOf(deltaY)) + radius;
				c.setCenter(cx, cy);
				c.setRadius(radius);
			} else if(s instanceof Ellipse) {
				Ellipse e = (Ellipse)s;
				double rx = Math.abs(p1.x - p2.x)/2.0;
				double ry = Math.abs(p1.y - p2.y)/2.0;
				double cx = (p1.x + p2.x)/2.0;
				double cy = (p1.y + p2.y)/2.0;
				e.setCenter(cx, cy);
				e.setRadiusX(rx);
				e.setRadiusY(ry);
			} else if(s instanceof Triangle) {
				Triangle t = (Triangle)s;
				double cx = (p1.x + p2.x)/2.0;
				double cy = (p1.y + p2.y)/2.0;
				t.setCenter(cx, cy);
				t.setFirstPoint(p1.x - cx, p1.y - cy);
				t.setSecondPoint(p2.x - cx, p2.y - cy);
				t.setThirdPoint(p2.x - cx, p2.y - cy);
			}
		});
	}
	
	public void setThirdPoint(int handle, Point2D.Double p1, Point2D.Double p2, Point2D.Double p3) {
		this.model.updateShape(handle, (s)->{
			if(s instanceof Triangle) {
				Triangle t = (Triangle)s;
				double cx = (p1.x + p2.x + p3.x)/3.0;
				double cy = (p1.y + p2.y + p3.y)/3.0;
				t.setCenter(cx, cy);
				t.setFirstPoint(p1.x - cx, p1.y - cy);
				t.setSecondPoint(p2.x - cx, p2.y - cy);
				t.setThirdPoint(p3.x - cx, p3.y - cy);
			}
		});
	}
	
	public AbstractShapeWrapper getShapeWrapperById(int id) {
		AbstractShape s = this.model.getShapeById(id);
		AbstractShapeWrapper result = null;
		if(s instanceof Line) {
			Line l = (Line)s;
			result = new LineWrapper(l);
		} else if(s instanceof Square) {
			Square sq = (Square)s;
			result = new SquareWrapper(sq);
		} else if(s instanceof Rectangle) {
			Rectangle r = (Rectangle)s;
			result = new RectangleWrapper(r);
		} else if(s instanceof Circle) {
			Circle c = (Circle)s;
			result = new CircleWrapper(c);
		} else if(s instanceof Ellipse) {
			Ellipse e = (Ellipse)s;
			result = new EllipseWrapper(e);
		} else if(s instanceof Triangle) {
			Triangle t = (Triangle)s;
			result = new TriangleWrapper(t);
		} else {
			result = new ImaginaryShapeWrapper();
		}
		return result;
	}
	
	public AbstractShapeWrapper addShape(ShapeType shape) {
		int id = this.model.addShape(shape.makeShape());
		return this.getShapeWrapperById(id);
	}
	
	public boolean hitShapeHandle(int handle, Point2D.Double p) {
		return false;
	}
	
	private double signOf(double x) {
		if(x >= 0.0) {
			return 1.0;
		} else {
			return -1.0;
		}
	}
	
	public void addObserver(Observer o) {
		model.addObserver(o);
	}
	
	public void deleteObserver(Observer o) {
		model.deleteObserver(o);
	}
	
	public Iterable<Triple<Color, AffineTransform, Shape>> getGraphicalColorShapeTriples() {
		ArrayList<Triple<Color, AffineTransform, Shape>> result = new ArrayList<Triple<Color, AffineTransform, Shape>>();
		for(int id: this.model.getShapeIds()) {
			AbstractShapeWrapper s = this.getShapeWrapperById(id);
			result.add(new Triple<Color, AffineTransform, Shape>(s.getColor(), s.getObjectToWorldTransform(), s.getGraphicalShape()));
		}
		
		return result;
	}
	
	private Shape getGraphicalShape(AbstractShape s) {
		Shape result = null;
		if(s instanceof Line) {
			Line l = (Line)s;
			Point2D.Double p1 = l.getFirstPoint();
			Point2D.Double p2 = l.getSecondPoint();
			result = new Line2D.Double(p1.x, p1.y, p2.x, p2.y);
		} else if(s instanceof Square) {
			Square sq = (Square)s;
			double side = sq.getSide();
			double halfSide = sq.getHalfSide();
			result = new Rectangle2D.Double(-halfSide, -halfSide, side, side);
		} else if(s instanceof Rectangle) {
			Rectangle r = (Rectangle)s;
			result = new Rectangle2D.Double(-r.getHalfWidth(), -r.getHalfHeight(), r.getWidth(), r.getHeight());
		} else if(s instanceof Circle) {
			Circle c = (Circle)s;
			double radius = c.getRadius();
			result = new Ellipse2D.Double(-radius, -radius, radius*2.0, radius*2.0);
		} else if(s instanceof Ellipse) {
			Ellipse e = (Ellipse)s;
			double w = e.getRadiusX()*2.0;
			double h = e.getRadiusY()*2.0;
			result = new Ellipse2D.Double(-w/2.0, -h/2.0, w, h);
		} else if(s instanceof Triangle) {
			Triangle t = (Triangle)s;
			Point2D.Double p1 = t.getFirstPoint();
			Point2D.Double p2 = t.getSecondPoint();
			Point2D.Double p3 = t.getThirdPoint();
			int[] x = new int[3];
			int[] y = new int[3];
			x[0] = (int)Math.round(p1.x);
			x[1] = (int)Math.round(p2.x);
			x[2] = (int)Math.round(p3.x);
			y[0] = (int)Math.round(p1.y);
			y[1] = (int)Math.round(p2.y);
			y[2] = (int)Math.round(p3.y);
			result = new Polygon(x, y, 3);
		} else {
			result = null;
		}
		return result;
	}
	
	public void drawShapeHandles(Graphics2D g2d, int handle, AffineTransform worldToView) {
		AbstractShape s = this.model.getShapeByHandle(handle);
		Color selectedColor = new Color(255, 255, 0);
		g2d.setColor(selectedColor);
		AffineTransform a = s.getObjectToWorldTransform();
		g2d.setTransform(a);
		double hr = this.controller.getVisualHandleRadius();
		double hd = hr*2.0;
		if(s instanceof Line) {
			Line l = (Line)s;
			Point2D.Double p1 = l.getFirstPoint();
			Point2D.Double p2 = l.getSecondPoint();
			Ellipse2D.Double h1 = new Ellipse2D.Double(p1.x - hr, p1.y - hr, hd, hd);
			Ellipse2D.Double h2 = new Ellipse2D.Double(p2.x - hr, p2.y - hr, hd, hd);
			g2d.draw(h1);
			g2d.draw(h2);
		} else if(s instanceof Square) {
			Square sq = (Square)s;
			double halfSide = sq.getHalfSide();
			double side = sq.getSide();
			Ellipse2D.Double handleAngle = new Ellipse2D.Double(halfSide + hd, -hr, hd, hd);
			Rectangle2D.Double outline = new Rectangle2D.Double(-halfSide, -halfSide, side, side);
			g2d.draw(Utilities.createCircleAtEndOfVector(new Point2D.Double(-halfSide, -halfSide), hr));
			g2d.draw(Utilities.createCircleAtEndOfVector(new Point2D.Double(halfSide, -halfSide), hr));
			g2d.draw(Utilities.createCircleAtEndOfVector(new Point2D.Double(-halfSide, halfSide), hr));
			g2d.draw(Utilities.createCircleAtEndOfVector(new Point2D.Double(halfSide, halfSide), hr));
			g2d.draw(handleAngle);
			g2d.draw(outline);
		} else if(s instanceof Rectangle) {
			Rectangle r = (Rectangle)s;
			double hw = r.getHalfWidth();
			double hh = r.getHalfHeight();
			Ellipse2D.Double handleAngle = new Ellipse2D.Double(hw + hd, -hr, hd, hd);
			Rectangle2D.Double outline = new Rectangle2D.Double(-hw, -hh, hw*2.0, hh*2.0);
			g2d.draw(Utilities.createCircleAtEndOfVector(new Point2D.Double(-hw, -hh), hr));
			g2d.draw(Utilities.createCircleAtEndOfVector(new Point2D.Double(-hw, hh), hr));
			g2d.draw(Utilities.createCircleAtEndOfVector(new Point2D.Double(hw, -hh), hr));
			g2d.draw(Utilities.createCircleAtEndOfVector(new Point2D.Double(hw, hh), hr));
			g2d.draw(handleAngle);
			g2d.draw(outline);
		} else if(s instanceof Circle) {
			Circle c = (Circle)s;
			double radius = c.getRadius();
			double hw = radius;
			double hh = radius;
			Ellipse2D.Double outline = new Ellipse2D.Double(-radius, -radius, radius*2.0, radius*2.0);
			g2d.draw(Utilities.createCircleAtEndOfVector(new Point2D.Double(-hw, -hh), hr));
			g2d.draw(Utilities.createCircleAtEndOfVector(new Point2D.Double(-hw, hh), hr));
			g2d.draw(Utilities.createCircleAtEndOfVector(new Point2D.Double(hw, -hh), hr));
			g2d.draw(Utilities.createCircleAtEndOfVector(new Point2D.Double(hw, hh), hr));
			g2d.draw(outline);
		} else if(s instanceof Ellipse) {
			Ellipse e = (Ellipse)s;
			double hw = e.getRadiusX();
			double hh = e.getRadiusY();
			Ellipse2D.Double handleAngle = new Ellipse2D.Double(hw + hd, -hr, hd, hd);
			Ellipse2D.Double outline = new Ellipse2D.Double(-hw, -hh, hw*2.0, hh*2.0);
			g2d.draw(Utilities.createCircleAtEndOfVector(new Point2D.Double(-hw, -hh), hr));
			g2d.draw(Utilities.createCircleAtEndOfVector(new Point2D.Double(-hw, hh), hr));
			g2d.draw(Utilities.createCircleAtEndOfVector(new Point2D.Double(hw, -hh), hr));
			g2d.draw(Utilities.createCircleAtEndOfVector(new Point2D.Double(hw, hh), hr));
			g2d.draw(handleAngle);
			g2d.draw(outline);
		} else if(s instanceof Triangle) {
			Triangle t = (Triangle)s;
			Point2D.Double p1 = t.getFirstPoint();
			Point2D.Double p2 = t.getSecondPoint();
			Point2D.Double p3 = t.getThirdPoint();
			int maxX = Utilities.max(t.getXCoords());
			Point2D.Double vector = new Point2D.Double(maxX + hd, 0);
			Polygon outline = new Polygon(t.getXCoords(), t.getYCoords(), 3);
			g2d.draw(Utilities.createCircleAtEndOfVector(p1, hr));
			g2d.draw(Utilities.createCircleAtEndOfVector(p2, hr));
			g2d.draw(Utilities.createCircleAtEndOfVector(p3, hr));
			g2d.draw(outline);
			g2d.draw(Utilities.createCircleAtEndOfVector(vector, hr));
		}
	
}
