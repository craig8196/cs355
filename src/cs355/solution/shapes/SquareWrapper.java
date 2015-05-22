package cs355.solution.shapes;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import cs355.solution.Model;

public class SquareWrapper extends AbstractShapeWrapper {

	public SquareWrapper(Model m, int id) {
		super(m, id);
	}

	private Square getSquare() {
		return (Square) this.model.getShapeById(this.id);
	}
	
	@Override
	public void setFirstTwoPoints(Double p1, Double p2) {
		AffineTransform oToW = this.getObjectToWorldTransform();
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
			Double c = new Double();
			oToW.transform(new Double(x, y), c);
			sq.setCenter(c.x, c.y);
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
		Square sq = this.getSquare();
		double halfSide = sq.getHalfSide();
		return new Double(halfSide + radius*2.0, 0.0);
	}

	@Override
	public Iterable<Double> getResizeHandleCenters() {
		ArrayList<Double> result = new ArrayList<Double>();
		Square sq = this.getSquare();
		double halfSide = sq.getHalfSide();
		result.add(new Point2D.Double(-halfSide, -halfSide));
		result.add(new Point2D.Double(halfSide, -halfSide));
		result.add(new Point2D.Double(-halfSide, halfSide));
		result.add(new Point2D.Double(halfSide, halfSide));
		return result;
	}

}
