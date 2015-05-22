package cs355.solution.shapes;

import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

import cs355.solution.Model;

public class LineWrapper extends AbstractShapeWrapper {

	private Double originalP1 = null;
	private Double originalP2 = null;
	
	public LineWrapper(Model m, int id) {
		super(m, id);
	}
	
	private Line getLine() {
		return (Line)this.model.getShapeById(this.id);
	}

	@Override
	public void setFirstTwoPoints(Double p1, Double p2) {
		this.model.updateShape(this.id, (s)->{
			Line l = (Line)s;
			l.setFirstPoint(p1.x, p1.y);
			l.setSecondPoint(p2.x, p2.y);
		});
	}

	@Override
	public void setFirstThreePoints(Double p1, Double p2, Double p3) {
		return;
	}
	
	@Override
	public Shape getSelectedOutlineShape() {
		return null;
	}

	@Override
	public Shape getGraphicalShape() {
		Line l = this.getLine();
		Point2D.Double p1 = l.getFirstPoint();
		Point2D.Double p2 = l.getSecondPoint();
		return new Line2D.Double(p1.x, p1.y, p2.x, p2.y);
	}
	
	@Override
	public Double getRotateHandleCenter(double radius) {
		return null;
	}

	@Override
	public Iterable<Double> getResizeHandleCenters() {
		ArrayList<Double> result = new ArrayList<Double>();
		Line l = this.getLine();
		result.add(l.getFirstPoint());
		result.add(l.getSecondPoint());
		return result;
	}
	
	@Override
	public void translate(Double p) {
		System.out.println("Translating line.");
		Point2D.Double md = this.mouseDown;
		Point2D.Double diff = new Point2D.Double(p.x - md.x, p.y - md.y);
		this.model.updateShape(this.id, (s)->{
			Line l = (Line)s;
			l.setFirstPoint(this.originalP1.x + diff.x, this.originalP1.y + diff.y);
			l.setSecondPoint(this.originalP2.x + diff.x, this.originalP2.y + diff.y);
		});
	}
	
	@Override
	public void setMouseDown(Double p) {
		super.setMouseDown(p);
		Line l = this.getLine();
		Double p1 = l.getFirstPoint();
		Double p2 = l.getSecondPoint();
		this.originalP1 = new Double(p1.x, p1.y);
		this.originalP2 = new Double(p2.x, p2.y);
	}

}
