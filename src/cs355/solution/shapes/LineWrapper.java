package cs355.solution.shapes;

import java.awt.Shape;
import java.awt.geom.Point2D.Double;

import cs355.solution.Model;

public class LineWrapper extends AbstractShapeWrapper {

	public LineWrapper(Model m, int id) {
		super(m, id);
	}
	
	private Line getLine() {
		return (Line)this.model.getShapeById(this.id);
	}

	@Override
	public Iterable<Shape> getSelectedHandleShapes() {
		return null;
	}

	@Override
	public void setFirstTwoPoints(Double p1, Double p2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFirstThreePoints(Double p1, Double p2, Double p3) {
		return;
	}

	@Override
	public Shape getGraphicalShape() {
		// TODO Auto-generated method stub
		return null;
	}

}
