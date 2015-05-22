package cs355.solution.shapes;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

import cs355.solution.Model;

public class EllipseWrapper extends AbstractShapeWrapper {

	public EllipseWrapper(Model m, int id) {
		super(m, id);
	}
	
	private Ellipse getEllipse() {
		return (Ellipse)this.model.getShapeById(this.id);
	}

	@Override
	public void setFirstTwoPoints(Double p1, Double p2) {
		AffineTransform oToW = this.getObjectToWorldTransform();
		this.model.updateShape(this.id, (s)->{
			Ellipse e = (Ellipse)s;
			double rx = Math.abs(p1.x - p2.x)/2.0;
			double ry = Math.abs(p1.y - p2.y)/2.0;
			double cx = (p1.x + p2.x)/2.0;
			double cy = (p1.y + p2.y)/2.0;
			Double c = new Double();
			oToW.transform(new Double(cx, cy), c);
			e.setCenter(c.x, c.y);
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
		Ellipse e = this.getEllipse();
		return new Double(e.getRadiusX() + radius*2.0, 0.0);
	}

	@Override
	public Iterable<Double> getResizeHandleCenters() {
		ArrayList<Double> result = new ArrayList<Double>();
		Ellipse e = this.getEllipse();
		double hw = e.getRadiusX();
		double hh = e.getRadiusY();
		result.add(new Point2D.Double(-hw, -hh));
		result.add(new Point2D.Double(-hw, hh));
		result.add(new Point2D.Double(hw, -hh));
		result.add(new Point2D.Double(hw, hh));
		return result;
	}

}
