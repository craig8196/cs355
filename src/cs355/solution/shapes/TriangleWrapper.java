package cs355.solution.shapes;

import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

import cs355.solution.Model;
import cs355.solution.ModelWrapper;

public class TriangleWrapper extends AbstractShapeWrapper {

	public TriangleWrapper(Model m, int id) {
		super(m, id);
	}
	
	private Triangle getTriangle() {
		return (Triangle)this.model.getShapeById(this.id);
	}

	@Override
	public Iterable<Shape> getSelectedHandleShapes() {
		ArrayList<Shape> result = new ArrayList<Shape>();
		double hr = ModelWrapper.HANDLE_RADIUS;
		double hd = hr*2.0;
		Triangle t = this.getTriangle();
		Point2D.Double p1 = t.getFirstPoint();
		Point2D.Double p2 = t.getSecondPoint();
		Point2D.Double p3 = t.getThirdPoint();
		int maxX = Utilities.max(t.getXCoords());
		Point2D.Double vector = new Point2D.Double(maxX + hd, 0);
		result.add(Utilities.createCircleAtEndOfVector(p1, hr));
		result.add(Utilities.createCircleAtEndOfVector(p2, hr));
		result.add(Utilities.createCircleAtEndOfVector(p3, hr));
		result.add(Utilities.createCircleAtEndOfVector(vector, hr));
		return null;
	}

	@Override
	public void setFirstTwoPoints(Double p1, Double p2) {
		this.model.updateShape(this.id, (s)->{
			Triangle t = (Triangle)s;
			double cx = (p1.x + p2.x)/2.0;
			double cy = (p1.y + p2.y)/2.0;
			t.setCenter(cx, cy);
			t.setFirstPoint(p1.x - cx, p1.y - cy);
			t.setSecondPoint(p2.x - cx, p2.y - cy);
			t.setThirdPoint(p2.x - cx, p2.y - cy);
		});
	}

	@Override
	public void setFirstThreePoints(Double p1, Double p2, Double p3) {
		this.model.updateShape(this.id, (s)->{
			Triangle t = (Triangle)s;
			double cx = (p1.x + p2.x + p3.x)/3.0;
			double cy = (p1.y + p2.y + p3.y)/3.0;
			t.setCenter(cx, cy);
			t.setFirstPoint(p1.x - cx, p1.y - cy);
			t.setSecondPoint(p2.x - cx, p2.y - cy);
			t.setThirdPoint(p3.x - cx, p3.y - cy);
		});
	}

	@Override
	public Shape getGraphicalShape() {
		Triangle t = this.getTriangle();
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
		return new Polygon(x, y, 3);
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
