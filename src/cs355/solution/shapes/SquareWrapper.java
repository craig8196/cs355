package cs355.solution.shapes;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

import cs355.solution.Model;
import cs355.solution.ModelWrapper;

public class SquareWrapper extends AbstractShapeWrapper {

	public SquareWrapper(Model m, int id) {
		super(m, id);
	}

	private Square getSquare() {
		return (Square) this.model.getShapeById(this.id);
	}
	
	@Override
	public Iterable<Shape> getSelectedHandleShapes() {
		ArrayList<Shape> result = new ArrayList<Shape>();
		double hr = ModelWrapper.HANDLE_RADIUS;
		double hd = hr*2.0;
		Square sq = this.getSquare();
		double halfSide = sq.getHalfSide();
		Ellipse2D.Double handleAngle = new Ellipse2D.Double(halfSide + hd, -hr, hd, hd);
		result.add(Utilities.createCircleAtEndOfVector(new Point2D.Double(-halfSide, -halfSide), hr));
		result.add(Utilities.createCircleAtEndOfVector(new Point2D.Double(halfSide, -halfSide), hr));
		result.add(Utilities.createCircleAtEndOfVector(new Point2D.Double(-halfSide, halfSide), hr));
		result.add(Utilities.createCircleAtEndOfVector(new Point2D.Double(halfSide, halfSide), hr));
		result.add(handleAngle);
		return result;
	}

	@Override
	public void setFirstTwoPoints(Double p1, Double p2) {
		this.model.updateShape(this.id, (s)->{
			Square sq = (Square)s;
			double deltaX = p2.x - p1.x;
			double deltaY = p2.y - p1.y;
			double side = 0.0;
			if(Math.abs(deltaX) < Math.abs(deltaY)) {
				side = Math.abs(deltaX);
			} else {
				side = Math.abs(deltaY);
			}
			double x = Math.min(p1.x, p1.x + side*Utilities.signOf(deltaX)) + side/2.0;
			double y = Math.min(p1.y, p1.y + side*Utilities.signOf(deltaY)) + side/2.0;
			sq.setCenter(x, y);
			sq.setSide(side);
		});
	}

	@Override
	public void setFirstThreePoints(Double p1, Double p2, Double p3) {
		return;
	}

	@Override
	public Shape getGraphicalShape() {
		Square sq = this.getSquare();
		double side = sq.getSide();
		double halfSide = sq.getHalfSide();
		return new Rectangle2D.Double(-halfSide, -halfSide, side, side);
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
