package cs355.solution;

import java.awt.Color;

import cs355.solution.shapes.AbstractShape;
import cs355.solution.shapes.Circle;
import cs355.solution.shapes.Ellipse;
import cs355.solution.shapes.Line;
import cs355.solution.shapes.Point2D;
import cs355.solution.shapes.Rectangle;
import cs355.solution.shapes.ShapeType;
import cs355.solution.shapes.Square;
import cs355.solution.shapes.Triangle;

public class ControllerModelWrapper {
	
	private Model model = null;

	public ControllerModelWrapper(Model m) {
		this.model = m;
	}
	
	public void setFirstTwoPoints(int handle, Point2D p1, Point2D p2) {
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
				double x = Math.min(p1.x, p1.x + side*this.signOf(deltaX));
				double y = Math.min(p1.y, p1.y + side*this.signOf(deltaY));
				
				sq.setTopLeftPoint(x, y);
				sq.setSide(side);
			} else if(s instanceof Rectangle) {
				Rectangle r = (Rectangle)s;
				double x = Math.min(p1.x, p2.x);
				double y = Math.min(p1.y, p2.y);
				double width = Math.abs(p1.x - p2.x);
				double height = Math.abs(p1.y - p2.y);
				r.setTopLeftCorner(x, y);
				r.setWidth(width);
				r.setHeight(height);
			} else if(s instanceof Circle) {
				Circle c = (Circle)s;
				double deltaX = p2.x - p1.x;
				double deltaY = p2.y - p1.y;
				double radius = Math.min(Math.abs(deltaX), Math.abs(deltaY))/2.0;
				double cx = Math.min(p1.x, p1.x + radius*2.0*this.signOf(deltaX)) + radius;
				double cy = Math.min(p1.y, p1.y + radius*2.0*this.signOf(deltaY)) + radius;
				c.setCenterPoint(cx, cy);
				c.setRadius(radius);
			} else if(s instanceof Ellipse) {
				Ellipse e = (Ellipse)s;
				double w = Math.abs(p1.x - p2.x);
				double h = Math.abs(p1.y - p2.y);
				double cx = Math.min(p1.x, p2.x) + w/2.0;
				double cy = Math.min(p1.y, p2.y) + h/2.0;
				e.setCenterPoint(cx, cy);
				e.setWidth(w);
				e.setHeight(h);
			} else if(s instanceof Triangle) {
				Triangle t = (Triangle)s;
				t.setFirstPoint(p1.x, p1.y);
				t.setSecondPoint(p2.x, p2.y);
				t.setThirdPoint(p2.x, p2.y);
			}
		});
	}
	
	public void setThirdPoint(int handle, Point2D p3) {
		this.model.updateShape(handle, (s)->{
			if(s instanceof Triangle) {
				Triangle t = (Triangle)s;
				t.setThirdPoint(p3.x, p3.y);
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
				s = new Line(c);
				break;
			case SQUARE:
				s = new Square(c);
				break;
			case RECTANGLE:
				s = new Rectangle(c);
				break;
			case CIRCLE:
				s = new Circle(c);
				break;
			case ELLIPSE:
				s = new Ellipse(c);
				break;
			case TRIANGLE:
				s = new Triangle(c);
				break;
			default:
				return -1;
		}
		int handle = this.model.addShape(s);
		this.setFirstTwoPoints(handle, new Point2D(x1, y1), new Point2D(x1, y1));
		
		return this.model.getFrontShapeHandle();
	}
	
	private double signOf(double x) {
		if(x >= 0.0) {
			return 1.0;
		} else {
			return -1.0;
		}
	}
}
