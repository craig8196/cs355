package cs355.solution.shapes;

import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

import cs355.solution.Model;

public class TriangleWrapper extends AbstractShapeWrapper {

	public TriangleWrapper(Model m, int id) {
		super(m, id);
	}
	
	private Triangle getTriangle() {
		return (Triangle)this.model.getShapeById(this.id);
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
		Triangle t = this.getTriangle();
		int maxX = Utilities.max(t.getXCoords());
		return new Double(maxX + radius*2.0, 0.0);
	}

	@Override
	public Iterable<Double> getResizeHandleCenters() {
		ArrayList<Double> result = new ArrayList<Double>();
		Triangle t = this.getTriangle();
		Point2D.Double p1 = t.getFirstPoint();
		Point2D.Double p2 = t.getSecondPoint();
		Point2D.Double p3 = t.getThirdPoint();
		result.add(p1);
		result.add(p2);
		result.add(p3);
		return result;
	}

}
