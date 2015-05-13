package cs355.solution;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Observable;
import java.util.Observer;

import cs355.GUIFunctions;
import cs355.ViewRefresher;
import cs355.solution.shapes.AbstractShape;
import cs355.solution.shapes.Circle;
import cs355.solution.shapes.Ellipse;
import cs355.solution.shapes.Line;
import cs355.solution.shapes.Rectangle;
import cs355.solution.shapes.Square;
import cs355.solution.shapes.Triangle;
import cs355.solution.shapes.Utilities;


public class View implements Observer, ViewRefresher {

	private ViewModelWrapper model = null;
	private Controller controller = null;
	private boolean initialized = false;
	
	public View(ViewModelWrapper m) {
		this.model = m;
		m.addObserver(this);
	}
	
	public void setController(Controller c) {
		this.controller = c;
	}
	
	@Override
	public void refreshView(Graphics2D g2d) {
		if(!this.initialized) { // Set the color swatch the first time through.
			this.setColorSwatch(this.controller.getCurrentColor());
			this.initialized = true;
		}
		for(Triple<Color, AffineTransform, Shape> p: this.model.getGraphicalColorShapeTriples()) {
			g2d.setColor(p.first);
			g2d.setTransform(p.second);
			g2d.draw(p.third);
			g2d.fill(p.third);
		}
		if(this.controller.isSelecting()) {
			if(this.controller.getCurrentShapeHandle() != Model.INVALID_HANDLE) {
				this.drawShapeHandles(g2d, this.controller.getCurrentShapeHandle());
			}
		}
	}
	
	private double handleRadius = 10.0;
	
	public void drawShapeHandles(Graphics2D g2d, int handle) {
		AbstractShape s = this.model.getShapeByHandle(handle);
		Color selectedColor = new Color(255, 255, 0);
		g2d.setColor(selectedColor);
		AffineTransform a = s.getObjectToWorldTransform();
		g2d.setTransform(a);
		double hr = this.handleRadius; // to reduce the amount of typing
		double hd = hr*2.0; // to reduce the amount of typing
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
			Ellipse2D.Double h1 = new Ellipse2D.Double(-halfSide-hd, -halfSide-hd, hd, hd);
			Ellipse2D.Double h2 = new Ellipse2D.Double(-halfSide-hd, halfSide, hd, hd);
			Ellipse2D.Double h3 = new Ellipse2D.Double(halfSide, -halfSide-hd, hd, hd);
			Ellipse2D.Double h4 = new Ellipse2D.Double(halfSide, halfSide, hd, hd);
			Ellipse2D.Double handleAngle = new Ellipse2D.Double(halfSide + hd, -hr, hd, hd);
			Rectangle2D.Double outline = new Rectangle2D.Double(-halfSide, -halfSide, side, side);
			g2d.draw(h1);
			g2d.draw(h2);
			g2d.draw(h3);
			g2d.draw(h4);
			g2d.draw(handleAngle);
			g2d.draw(outline);
		} else if(s instanceof Rectangle) {
			Rectangle r = (Rectangle)s;
			double hw = r.getHalfWidth();
			double hh = r.getHalfHeight();
			Ellipse2D.Double h1 = new Ellipse2D.Double(-hw - hd, -hh - hd, hd, hd);
			Ellipse2D.Double h2 = new Ellipse2D.Double(-hw - hd, hh, hd, hd);
			Ellipse2D.Double h3 = new Ellipse2D.Double(hw, -hh - hd, hd, hd);
			Ellipse2D.Double h4 = new Ellipse2D.Double(hw, hh, hd, hd);
			Ellipse2D.Double handleAngle = new Ellipse2D.Double(hw + hd, -hr, hd, hd);
			Rectangle2D.Double outline = new Rectangle2D.Double(-hw, -hh, hw*2.0, hh*2.0);
			g2d.draw(h1);
			g2d.draw(h2);
			g2d.draw(h3);
			g2d.draw(h4);
			g2d.draw(handleAngle);
			g2d.draw(outline);
		} else if(s instanceof Circle) {
			Circle c = (Circle)s;
			double radius = c.getRadius();
			double hw = radius;
			double hh = radius;
			Ellipse2D.Double h1 = new Ellipse2D.Double(-hw - hd, -hh - hd, hd, hd);
			Ellipse2D.Double h2 = new Ellipse2D.Double(-hw - hd, hh, hd, hd);
			Ellipse2D.Double h3 = new Ellipse2D.Double(hw, -hh - hd, hd, hd);
			Ellipse2D.Double h4 = new Ellipse2D.Double(hw, hh, hd, hd);
			Ellipse2D.Double handleAngle = new Ellipse2D.Double(hw + hd, -hr, hd, hd);
			Ellipse2D.Double outline = new Ellipse2D.Double(-radius, -radius, radius*2.0, radius*2.0);
			g2d.draw(h1);
			g2d.draw(h2);
			g2d.draw(h3);
			g2d.draw(h4);
			g2d.draw(handleAngle);
			g2d.draw(outline);
		} else if(s instanceof Ellipse) {
			Ellipse e = (Ellipse)s;
			double hw = e.getRadiusX();
			double hh = e.getRadiusY();
			Ellipse2D.Double h1 = new Ellipse2D.Double(-hw - hd, -hh - hd, hd, hd);
			Ellipse2D.Double h2 = new Ellipse2D.Double(-hw - hd, hh, hd, hd);
			Ellipse2D.Double h3 = new Ellipse2D.Double(hw, -hh - hd, hd, hd);
			Ellipse2D.Double h4 = new Ellipse2D.Double(hw, hh, hd, hd);
			Ellipse2D.Double handleAngle = new Ellipse2D.Double(hw + hd, -hr, hd, hd);
			Ellipse2D.Double outline = new Ellipse2D.Double(-hw, -hh, hw*2.0, hh*2.0);
			g2d.draw(h1);
			g2d.draw(h2);
			g2d.draw(h3);
			g2d.draw(h4);
			g2d.draw(handleAngle);
			g2d.draw(outline);
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
			Polygon outline = new Polygon(x, y, 3);
			Point2D.Double p1n = Utilities.normalize(p1);
			Point2D.Double p2n = Utilities.normalize(p2);
			Point2D.Double p3n = Utilities.normalize(p3);
			Point2D.Double p1c = new Point2D.Double(p1.x + p1n.x*hr, p1.y + p1n.y*hr);
			Point2D.Double p2c = new Point2D.Double(p2.x + p2n.x*hr, p2.y + p2n.y*hr);
			Point2D.Double p3c = new Point2D.Double(p3.x + p3n.x*hr, p3.y + p3n.y*hr);
			Ellipse2D.Double h1 = new Ellipse2D.Double(p1c.x - hr, p1c.y - hr, hd, hd);
			Ellipse2D.Double h2 = new Ellipse2D.Double(p2c.x - hr, p2c.y - hr, hd, hd);
			Ellipse2D.Double h3 = new Ellipse2D.Double(p3c.x - hr, p3c.y - hr, hd, hd);
			g2d.draw(h1);
			g2d.draw(h2);
			g2d.draw(h3);
			g2d.draw(outline);
		}
	}
	
	public void update() {
		GUIFunctions.refresh();
	}

	@Override
	public void update(Observable o, Object arg) {
		GUIFunctions.refresh();
	}
	
	public void setColorSwatch(Color c) {
		GUIFunctions.changeSelectedColor(c);
	}
}
