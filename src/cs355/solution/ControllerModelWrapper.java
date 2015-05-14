package cs355.solution;

import java.awt.Color;
import java.awt.geom.Point2D;

import cs355.solution.shapes.AbstractShape;
import cs355.solution.shapes.Circle;
import cs355.solution.shapes.Ellipse;
import cs355.solution.shapes.Line;
import cs355.solution.shapes.Rectangle;
import cs355.solution.shapes.ShapeType;
import cs355.solution.shapes.Square;
import cs355.solution.shapes.Triangle;
import cs355.solution.shapes.UpdateShape;
import cs355.solution.shapes.Utilities;

public class ControllerModelWrapper {
	
	public Color getColorByHandle(int i) {
		return model.getColorByHandle(i);
	}

	public void updateShape(int handle, UpdateShape f) {
		model.updateShape(handle, f);
	}

	public AbstractShape getShapeByHandle(int i) {
		return model.getShapeByHandle(i);
	}
	
	private Model model = null;

	public ControllerModelWrapper(Model m) {
		this.model = m;
	}
	
	public int hitShape(double x, double y) {
		return this.model.hitShape(x, y, Controller.TOLERANCE);
	}
	
	public boolean hitShapeRotateHandle(int handle, Point2D.Double p, double radius) {
		AbstractShape s = this.getShapeByHandle(handle);
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
	
	public int shapeFactory(Color c, double x1, double y1, ShapeType shape) {
		if(shape == null) {
			return -1;
		}
		AbstractShape s = null;
		switch(shape) {
			case LINE:
				s = new Line(c, x1, y1);
				break;
			case SQUARE:
				s = new Square(c, x1, y1);
				break;
			case RECTANGLE:
				s = new Rectangle(c, x1, y1);
				break;
			case CIRCLE:
				s = new Circle(c, x1, y1);
				break;
			case ELLIPSE:
				s = new Ellipse(c, x1, y1);
				break;
			case TRIANGLE:
				s = new Triangle(c, x1, y1);
				break;
			default:
				return -1;
		}
		int handle = this.model.addShape(s);
		this.setFirstTwoPoints(handle, new Point2D.Double(x1, y1), new Point2D.Double(x1, y1));
		
		return this.model.getFrontShapeHandle();
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
}
