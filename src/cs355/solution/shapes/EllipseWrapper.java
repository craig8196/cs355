package cs355.solution.shapes;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

import cs355.solution.Model;
import cs355.solution.ModelWrapper;

public class EllipseWrapper extends AbstractShapeWrapper {

	public EllipseWrapper(Model m, int id) {
		super(m, id);
	}
	
	private Ellipse getEllipse() {
		return (Ellipse)this.model.getShapeById(this.id);
	}

	@Override
	public Iterable<Shape> getSelectedHandleShapes() {
		ArrayList<Shape> result = new ArrayList<Shape>();
		double hr = ModelWrapper.HANDLE_RADIUS;
		double hd = hr*2.0;
		Ellipse e = this.getEllipse();
		double hw = e.getRadiusX();
		double hh = e.getRadiusY();
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
			Ellipse e = (Ellipse)s;
			double rx = Math.abs(p1.x - p2.x)/2.0;
			double ry = Math.abs(p1.y - p2.y)/2.0;
			double cx = (p1.x + p2.x)/2.0;
			double cy = (p1.y + p2.y)/2.0;
			e.setCenter(cx, cy);
			e.setRadiusX(rx);
			e.setRadiusY(ry);
		});
	}

	@Override
	public void setFirstThreePoints(Double p1, Double p2, Double p3) {
		return;
	}

	@Override
	public Shape getGraphicalShape() {
		Ellipse e = getEllipse();
		double w = e.getRadiusX()*2.0;
		double h = e.getRadiusY()*2.0;
		return new Ellipse2D.Double(-w/2.0, -h/2.0, w, h);
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
