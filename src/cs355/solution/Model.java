package cs355.solution;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Observable;

import cs355.HouseModel;
import cs355.solution.shapes.AbstractShape;
import cs355.solution.shapes.UpdateShape;
import cs355.solution.shapes.Utilities;

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
	
	// images
	private int imageWidth = 0;
	private int imageHeight = 0;
	private int[][] imageMatrix = null;
	
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
	
	public void setImage(int width, int height, int[][] matrix) {
		this.imageWidth = width;
		this.imageHeight = height;
		this.imageMatrix = matrix;
		this.setChanged();
		this.notifyObservers();
	}
	
	public boolean hasImage() {
		if(this.imageMatrix == null) {
			return false;
		} else {
			return true;
		}
	}
	
	public int getImageWidth() {
		return imageWidth;
	}
	
	public int getImageHeight() {
		return imageHeight;
	}
	
	public int[][] getImageMatrix() {
		return this.imageMatrix;
	}
	
	public void imageChangeBrightness(int amount) {
		Utilities.pixelLevelOperation(
			this.imageWidth, 
			this.imageHeight, 
			this.imageMatrix, 
			(v)->{
				v += amount;
				v = Utilities.clampToRange(v, 0, 255);
				return v;
			}
		);
		this.setChanged();
		this.notifyObservers();
	}
	
	public void imageChangeContrast(int amount) {
		double ratio = (amount + 100.0f)/100.0f;
		double scalar = ratio*ratio*ratio*ratio;
		Utilities.pixelLevelOperation(
			this.imageWidth, 
			this.imageHeight, 
			this.imageMatrix, 
			(v)->{
				v = (int)(scalar*(v - 128.0) + 128.0);
				v = Utilities.clampToRange(v, 0, 255);
				return v;
			}
		);
		this.setChanged();
		this.notifyObservers();
	}
	
	public void imageUniformBlur() {
		this.imageMatrix = Utilities.applyKernel(
			this.imageWidth, 
			this.imageHeight, 
			this.imageMatrix, 
			new int[][]{
				{1,1,1},
				{1,1,1},
				{1,1,1}
			}, 
			0
		);
		double scalar = 1.0/9.0;
		Utilities.pixelLevelOperation(
			this.imageWidth, 
			this.imageHeight, 
			this.imageMatrix, 
			(v)->{
				double result = v*scalar;
				return Utilities.clampToRange((int)Math.round(result), 0, 255);
			}
		);
		this.setChanged();
		this.notifyObservers();
	}
	
	public void imageMedianBlur() {
		this.imageMatrix = Utilities.findMedians(
			this.imageWidth, 
			this.imageHeight, 
			this.imageMatrix, 
			0
		);
		this.setChanged();
		this.notifyObservers();
	}
	
	public void imageSharpen() {
		this.imageMatrix = Utilities.applyKernel(
				this.imageWidth, 
				this.imageHeight, 
				this.imageMatrix, 
				new int[][]{
					{0,-1,0},
					{-1,6,-1},
					{0,-1,0}
				}, 
				0
			);
			double scalar = 1.0/2.0;
			Utilities.pixelLevelOperation(
				this.imageWidth, 
				this.imageHeight, 
				this.imageMatrix, 
				(v)->{
					double result = v*scalar;
					return Utilities.clampToRange((int)Math.round(result), 0, 255);
				}
			);
		this.setChanged();
		this.notifyObservers();
	}
	
	public void imageEdgeDetection() {
		this.setChanged();
		this.notifyObservers();
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
