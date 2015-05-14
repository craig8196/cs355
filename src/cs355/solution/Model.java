package cs355.solution;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Observable;

import cs355.solution.shapes.AbstractShape;
import cs355.solution.shapes.UpdateShape;

public class Model extends Observable {

	public static final int INVALID_HANDLE = -1;
	
	private ArrayList<AbstractShape> shapesList = new ArrayList<AbstractShape>();
	
	public Model() {}
	
	public int addShape(AbstractShape s) {
		this.shapesList.add(s);
		this.setChanged();
		this.notifyObservers();
		return this.getFrontShapeHandle();
	}
	
	public int hitShape(double x, double y, double tolerance) {
		for(int i = this.shapesList.size() - 1; i >= 0; i--) {
			if(this.shapesList.get(i).isPointInShape(new Point2D.Double(x, y), tolerance)) {
				return i;
			}
		}
		return Model.INVALID_HANDLE;
	}
	
	public Color getColorByHandle(int i) {
		if(this.inRange(this.shapesList, i)) {
			return this.shapesList.get(i).getColor();
		}
		return null;
	}
	
	public int getFrontShapeHandle() {
		return this.shapesList.size() - 1;
	}
	
	public AbstractShape getShapeByHandle(int i) {
		if(this.inRange(this.shapesList, i)) {
			return this.shapesList.get(i);
		}
		return null;
	}

	public Iterable<Integer> getShapeHandles() {
		ArrayList<Integer> result = new ArrayList<Integer>();
		for(int i = 0; i < this.shapesList.size(); i++) {
			result.add(i);
		}
		return result;
	}
	
	public boolean inRange(ArrayList<AbstractShape> l, int i) {
		if(l == null) {
			return false;
		}
		
		return i >= 0 && i <= l.size() - 1;
	}
	
	public void updateShape(int handle, UpdateShape f) {
		if(this.inRange(this.shapesList, handle)) {
			f.updateShape(this.shapesList.get(handle));
		}
		this.setChanged();
		this.notifyObservers();
	}
}
