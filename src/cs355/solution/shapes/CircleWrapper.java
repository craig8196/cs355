package cs355.solution.shapes;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

import cs355.solution.Model;

public class CircleWrapper extends AbstractShapeWrapper {

	public CircleWrapper(Model m, int id) {
		super(m, id);
	}
	
	private Circle getCircle() {
		return (Circle)this.model.getShapeById(this.id);
	}

	@Override
	public void setFirstTwoPoints(Double p1, Double p2) {
		this.model.updateShape(this.id, (s)->{
			Circle c = (Circle)s;
			double deltaX = p2.x - p1.x;
			double deltaY = p2.y - p1.y;
			double radius = Math.min(Math.abs(deltaX), Math.abs(deltaY))/2.0;
			double cx = Math.min(p1.x, p1.x + radius*2.0*Utilities.signOf(deltaX)) + radius;
			double cy = Math.min(p1.y, p1.y + radius*2.0*Utilities.signOf(deltaY)) + radius;
			c.setCenter(cx, cy);
			c.setRadius(radius);
		});
	}

	@Override
	public void setFirstThreePoints(Double p1, Double p2, Double p3) {
		return;
	}

	@Override
	public Shape getGraphicalShape() {
		Circle c = this.getCircle();
		double radius = c.getRadius();
		return new Ellipse2D.Double(-radius, -radius, radius*2.0, radius*2.0);
	}

	@Override
	public Double getRotateHandleCenter(double radius) {
		return null;
	}

	@Override
	public Iterable<Double> getResizeHandleCenters() {
		ArrayList<Double> result = new ArrayList<Double>();
		Circle c = this.getCircle();
		double radius = c.getRadius();
		double hw = radius;
		double hh = radius;
		result.add(new Point2D.Double(-hw, -hh));
		result.add(new Point2D.Double(-hw, hh));
		result.add(new Point2D.Double(hw, -hh));
		result.add(new Point2D.Double(hw, hh));
		return result;
	}
	
}
