package cs355.solution;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Observable;

import cs355.HouseModel;
import cs355.solution.shapes.AbstractShape;
import cs355.solution.shapes.UpdateShape;

public class Model extends Observable {

	public static final int INVALID_ID = -1;
	
	// 2d shapes
	private ArrayList<AbstractShape> shapesList = new ArrayList<AbstractShape>();
	
	// 3d lines
	private HouseModel house = new HouseModel();
	private ObjectTransformation[] houses = new ObjectTransformation[]{
	    new ObjectTransformation().setColor(128, 128, 128),
	    (new ObjectTransformation(-15.0, 0.0, 15.0, -Math.PI/2.0)).setColor(255, 0, 0),
	    (new ObjectTransformation(15.0, 0.0, 15.0, Math.PI/2.0)).setColor(0, 255, 0),
	    (new ObjectTransformation(0.0, 0.0, 30.0, Math.PI)).setColor(0, 0, 255),
	    (new ObjectTransformation(-15.0, 0.0, -2.5, -Math.PI/4.0)).setColor(255, 0, 255),
	    (new ObjectTransformation(15.0, 0.0, -2.5, Math.PI/4.0)).setColor(255, 255, 0),
	    (new ObjectTransformation(-15.0, 0.0, 32.5, -Math.PI*3.0/4.0)).setColor(0, 255, 255),
	    (new ObjectTransformation(15.0, 0.0, 32.5, Math.PI*3.0/4.0)).setColor(255, 255, 255),
	};
	
	public Model() {}
	
	public int addShape(AbstractShape s) {
		if(s == null) {
			return Model.INVALID_ID;
		}
		this.shapesList.add(s);
		this.setChanged();
		this.notifyObservers();
		return this.getFrontShapeId();
	}
	
	public int hitShape(Point2D.Double p, double tolerance) {
		for(int i = this.shapesList.size() - 1; i >= 0; i--) {
			if(this.shapesList.get(i).isPointInShape(p, tolerance)) {
				return i;
			}
		}
		return Model.INVALID_ID;
	}
	
	public Color getColorById(int i) {
		if(this.inRange(this.shapesList, i)) {
			return this.shapesList.get(i).getColor();
		}
		return null;
	}
	
	public int getFrontShapeId() {
		return this.shapesList.size() - 1;
	}
	
	public AbstractShape getShapeById(int i) {
		if(this.inRange(this.shapesList, i)) {
			return this.shapesList.get(i);
		}
		return null;
	}

	public Iterable<Integer> getShapeIds() {
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
	
	public void updateShape(int id, UpdateShape f) {
		if(this.inRange(this.shapesList, id)) {
			f.updateShape(this.shapesList.get(id));
		}
		this.setChanged();
		this.notifyObservers();
	}
	
	public HouseModel getHouseModel() {
		return this.house;
	}
	
	public ObjectTransformation[] getHouseTransformations() {
		return this.houses;
	}
}
