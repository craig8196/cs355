package cs355.solution;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Observable;

public class Model extends Observable {

	private ArrayList<Shape> shapesList = new ArrayList<Shape>();
	
	public Model() {}
	
	public int shapeFactory(Color c, double x1, double y1, ShapeType shape) {
		Shape s = null;
		switch(shape) {
			case LINE:
				s = makeLine(c, x1, y1, x1, y1);
			case SQUARE:
				s = makeSquare(c, x1, y1, x1, y1);
				break;
			case RECTANGLE:
				s = makeRectangle(c, x1, y1, x1, y1);
				break;
			case CIRCLE:
				s = makeCircle(c, x1, y1, x1, y1);
				break;
			case ELLIPSE:
				s = makeEllipse(c, x1, y1, x1, y1);
				break;
			case TRIANGLE:
				s = makeTriangle(c, x1, y1, x1, y1, x1, y1);
				break;
			default:
				return -1;
		}
		this.shapesList.add(s);
		this.hasChanged();
		this.notifyObservers();
		return this.getFrontShapeHandle();
	}
	
	private Shape makeLine(Color c, double x1, double y1, double x2, double y2) {
		return new Line(c, new Point2D.Double(x1, y1), new Point2D.Double(x2, y2));
	}
	
	private Shape makeSquare(Color c, double x1, double y1, double x2, double y2) {
		double side = 0.0;
		if(Math.abs(x2 - x1) < Math.abs(y2 - y1)) {
			side = x2 - x1;
		} else {
			side = y2 - y1;
		}
		return new Square(c, new Point2D.Double(x1, y1), side);
	}
	
	private Shape makeRectangle(Color c, double x1, double y1, double x2, double y2) {
		return new Rectangle(c, new Point2D.Double(x1, y1), new Point2D.Double(x2, y2));
	}
	
	private double signOf(double num) {
		if(num >= 0.0) {
			return 1.0;
		} else {
			return -1.0;
		}
	}
	
	private Shape makeCircle(Color c, double x1, double y1, double x2, double y2) {
		double signX = 1;
		double signY = 1;
		double radius = 0.0;
		double deltaX = x2 - x1;
		double deltaY = y2 - y1;
		if(Math.abs(x2 - x1) < Math.abs(y2 - y1)) {
			radius = Math.abs(deltaX/2);
		} else {
			radius = Math.abs(deltaY/2);
		}
		signX = this.signOf(deltaX);
		signY = this.signOf(deltaY);
		double cx = x1 + signX*radius;
		double cy = y1 + signY*radius;
		return new Circle(c, new Point2D.Double(cx, cy), radius);
	}
	
	private Shape makeEllipse(Color c, double x1, double y1, double x2, double y2) {
		return new Ellipse(c, new Point2D.Double(x1, y1), new Point2D.Double(x2, y2));
	}
	
	private Shape makeTriangle(Color c, double x1, double y1, double x2, double y2, double x3, double y3) {
		return new Triangle(c, new Point2D.Double(x1, y1), new Point2D.Double(x2, y2), new Point2D.Double(x3, y3));
	}
	
	public void updateShape(int handle, UpdateShape f) {
		if(this.inRange(this.shapesList, handle)) {
			f.updateShape("");
		}
		this.hasChanged();
		this.notifyObservers();
	}
	
	public boolean inRange(ArrayList<Shape> l, int i) {
		if(l == null) {
			return false;
		}
		
		return i >= 0 && i <= l.size() - 1;
	}
	
	public int getFrontShapeHandle() {
		return this.shapesList.size() - 1;
	}
	
	public void moveToFront(int handle) {
		// TODO allow moving the shape to the front
		
		this.hasChanged();
		this.notifyObservers();
	}
}
