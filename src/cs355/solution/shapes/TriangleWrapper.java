package cs355.solution.shapes;

import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

import cs355.solution.Model;

public class TriangleWrapper extends AbstractShapeWrapper {

	private int whichPointIndex = 0;
	
	public TriangleWrapper(Model m, int id) {
		super(m, id);
	}
	
	private Triangle getTriangle() {
		return (Triangle)this.model.getShapeById(this.id);
	}
	
	@Override
	public void setMouseDown(Point2D.Double p) {
		super.setMouseDown(p);
		Iterable<Point2D.Double> hc = this.getResizeHandleCenters();
		AffineTransform worldToObject = this.getWorldToObjectTransform();
		Point2D.Double pObj = new Point2D.Double();
		worldToObject.transform(p, pObj);
		Point2D.Double nearest = Utilities.getNearestPoint(pObj, hc);
		int index = 0;
		for(Point2D.Double h: hc) {
			if(h == nearest) {
				this.whichPointIndex = index;
			}
			index++;
		}
	}

	@Override
	public void setFirstTwoPoints(Double p1, Double p2) {
		this.setFirstThreePoints(p1, p2, p2);
	}

	@Override
	public void setFirstThreePoints(Double p1, Double p2, Double p3) {
		AffineTransform oToW = this.getObjectToWorldTransform();
		this.model.updateShape(this.id, (s)->{
			Triangle t = (Triangle)s;
			double cx = (p1.x + p2.x + p3.x)/3.0;
			double cy = (p1.y + p2.y + p3.y)/3.0;
			Double c = new Double();
			oToW.transform(new Double(cx, cy), c);
			t.setCenter(c.x, c.y);
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

	@Override
	public void resize(Point2D.Double p) {
		Triangle t = this.getTriangle();
		Point2D.Double md = this.mouseDown;
		Point2D.Double diff = new Point2D.Double(p.x - md.x, p.y - md.y);
		Point2D.Double newNearest = new Point2D.Double(this.nearestResize.x + diff.x, this.nearestResize.y + diff.y);
		Point2D.Double p1 = new Point2D.Double();
		Point2D.Double p2 = new Point2D.Double();
		Point2D.Double p3 = new Point2D.Double();
		AffineTransform objToWorld = this.getObjectToWorldTransform();
		objToWorld.transform(t.getFirstPoint(), p1);
		objToWorld.transform(t.getSecondPoint(), p2);
		objToWorld.transform(t.getThirdPoint(), p3);
		switch(this.whichPointIndex) {
		case 0: p1 = newNearest; break;
		case 1: p2 = newNearest; break;
		case 2: p3 = newNearest; break;
		}
		this.setFirstThreePointsWorld(p1, p2, p3);
	}
}
