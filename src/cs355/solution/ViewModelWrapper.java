package cs355.solution;

import java.awt.Color;
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
import cs355.solution.shapes.Circle;
import cs355.solution.shapes.Ellipse;
import cs355.solution.shapes.Line;
import cs355.solution.shapes.Rectangle;
import cs355.solution.shapes.Square;
import cs355.solution.shapes.Triangle;

public class ViewModelWrapper {
	
	public AbstractShape getShapeByHandle(int i) {
		return model.getShapeByHandle(i);
	}

	private Model model = null;

	public ViewModelWrapper(Model m) {
		this.model = m;
	}

	public void addObserver(Observer o) {
		model.addObserver(o);
	}
	
	public void deleteObserver(Observer o) {
		model.deleteObserver(o);
	}
	
	public Iterable<Triple<Color, AffineTransform, Shape>> getGraphicalColorShapeTriples() {
		ArrayList<Triple<Color, AffineTransform, Shape>> result = new ArrayList<Triple<Color, AffineTransform, Shape>>();
		for(int h: this.model.getShapeHandles()) {
			Color c = this.model.getColorByHandle(h);
			Shape s = this.getGraphicalShape(this.model.getShapeByHandle(h));
			AffineTransform t = this.model.getShapeByHandle(h).getObjectToWorldTransform();
			if(s == null) {
				continue;
			}
			result.add(new Triple<Color, AffineTransform, Shape>(c, t, s));
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

}
