package cs355.solution.shapes;

import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import cs355.solution.Model;

public class RectangleWrapper extends AbstractShapeWrapper {

	public RectangleWrapper(Model m, int id) {
		super(m, id);
	}
	
	private Rectangle getRectangle() {
		return (Rectangle)this.model.getShapeById(this.id);
	}

	@Override
	public void setFirstTwoPoints(Double p1, Double p2) {
		this.model.updateShape(this.id, (s)->{
			Rectangle r = (Rectangle)s;
			double x = (p1.x + p2.x)/2.0;
			double y = (p1.y + p2.y)/2.0;
			double width = Math.abs(p1.x - p2.x);
			double height = Math.abs(p1.y - p2.y);
			r.setCenter(x, y);
			r.setWidth(width);
			r.setHeight(height);
		});
	}

	@Override
	public void setFirstThreePoints(Double p1, Double p2, Double p3) {
		return;
	}

	@Override
	public Shape getGraphicalShape() {
		Rectangle r = this.getRectangle();
		return new Rectangle2D.Double(-r.getHalfWidth(), -r.getHalfHeight(), r.getWidth(), r.getHeight());
	}

	@Override
	public Double getRotateHandleCenter(double radius) {
		Rectangle r = this.getRectangle();
		return new Double(r.getHalfWidth() + radius*2.0, 0.0);
	}

	@Override
	public Iterable<Double> getResizeHandleCenters() {
		ArrayList<Double> result = new ArrayList<Double>();
		Rectangle r = this.getRectangle();
		double hw = r.getHalfWidth();
		double hh = r.getHalfHeight();
		result.add(new Point2D.Double(-hw, -hh));
		result.add(new Point2D.Double(-hw, hh));
		result.add(new Point2D.Double(hw, -hh));
		result.add(new Point2D.Double(hw, hh));
		return result;
	}

}
