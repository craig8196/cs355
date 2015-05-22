package cs355.solution.shapes;

import java.awt.Shape;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

import cs355.solution.Model;

public class ImaginaryShapeWrapper extends AbstractShapeWrapper {

	public ImaginaryShapeWrapper(Model m, int id) {
		super(m, id);
	}

	@Override
	public void setFirstTwoPoints(Double p1, Double p2) {
		return;
	}

	@Override
	public void setFirstThreePoints(Double p1, Double p2, Double p3) {
		return;
	}

	@Override
	public Shape getGraphicalShape() {
		return null;
	}

	@Override
	public Double getRotateHandleCenter(double radius) {
		return null;
	}
	
	@Override
	public boolean isSelected() {
		return false;
	}
	
	@Override
	public boolean isTranslating() {
		return false;
	}
	
	@Override
	public boolean isRotating() {
		return false;
	}
	
	@Override
	public boolean isResizing() {
		return false;
	}

	@Override
	public Iterable<Double> getResizeHandleCenters() {
		return new ArrayList<Double>();
	}
	
}
