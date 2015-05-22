package cs355.solution.shapes;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

import cs355.solution.Model;
import cs355.solution.ModelWrapper;

public class RectangleWrapper extends AbstractShapeWrapper {

	public RectangleWrapper(Model m, int id) {
		super(m, id);
	}
	
	private Rectangle getRectangle() {
		return (Rectangle)this.model.getShapeById(this.id);
	}

	@Override
	public Iterable<Shape> getSelectedHandleShapes() {
		ArrayList<Shape> result = new ArrayList<Shape>();
		double hr = ModelWrapper.HANDLE_RADIUS;
		double hd = hr*2.0;
		Rectangle r = this.getRectangle();
		double hw = r.getHalfWidth();
		double hh = r.getHalfHeight();
		Ellipse2D.Double handleAngle = new Ellipse2D.Double(hw + hd, -hr, hd, hd);
		result.add(Utilities.createCircleAtEndOfVector(new Point2D.Double(-hw, -hh), hr));
		result.add(Utilities.createCircleAtEndOfVector(new Point2D.Double(-hw, hh), hr));
		result.add(Utilities.createCircleAtEndOfVector(new Point2D.Double(hw, -hh), hr));
		result.add(Utilities.createCircleAtEndOfVector(new Point2D.Double(hw, hh), hr));
		result.add(handleAngle);
		return result;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<Double> getResizeHandleCenters() {
		// TODO Auto-generated method stub
		return null;
	}

}
