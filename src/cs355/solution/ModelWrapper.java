package cs355.solution;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Observer;

import cs355.HouseModel;
import cs355.solution.shapes.AbstractShape;
import cs355.solution.shapes.AbstractShapeWrapper;
import cs355.solution.shapes.Circle;
import cs355.solution.shapes.CircleWrapper;
import cs355.solution.shapes.Ellipse;
import cs355.solution.shapes.EllipseWrapper;
import cs355.solution.shapes.ImaginaryShapeWrapper;
import cs355.solution.shapes.Line;
import cs355.solution.shapes.LineWrapper;
import cs355.solution.shapes.Rectangle;
import cs355.solution.shapes.RectangleWrapper;
import cs355.solution.shapes.ShapeType;
import cs355.solution.shapes.Square;
import cs355.solution.shapes.SquareWrapper;
import cs355.solution.shapes.Triangle;
import cs355.solution.shapes.TriangleWrapper;


public class ModelWrapper {
	
	public HouseModel getHouseModel() {
		return model.getHouseModel();
	}

	public ObjectTransformation[] getHouseTransformations() {
		return model.getHouseTransformations();
	}

	public static final double TOLERANCE = 4.0; // In view units.
	public static final double HANDLE_RADIUS = 10.0; // In view units.
	
	public static final double WORLD_X_MIN = 0.0;
	public static final double WORLD_X_MAX = 2048.0;
	public static final double WORLD_Y_MIN = 0.0;
	public static final double WORLD_Y_MAX = 2048.0;
	public static final double VIEW_WIDTH = 512.0; // In world units.
	public static final double VIEW_HEIGHT = 512.0; // In world units.
	
	public Model model = null;
	
	public ModelWrapper(Model m) {
		this.model = m;
	}
	
	public AbstractShapeWrapper hitShape(Point2D.Double p, double tolerance) {
		return this.getShapeWrapperById(this.model.hitShape(p, tolerance));
	}
	
	public AbstractShapeWrapper getShapeWrapperById(int id) {
		AbstractShape s = this.model.getShapeById(id);
		AbstractShapeWrapper result = null;
		if(s instanceof Line) {
			result = new LineWrapper(this.model, id);
		} else if(s instanceof Square) {
			result = new SquareWrapper(this.model, id);
		} else if(s instanceof Rectangle) {
			result = new RectangleWrapper(this.model, id);
		} else if(s instanceof Circle) {
			result = new CircleWrapper(this.model, id);
		} else if(s instanceof Ellipse) {
			result = new EllipseWrapper(this.model, id);
		} else if(s instanceof Triangle) {
			result = new TriangleWrapper(this.model, id);
		} else {
			result = new ImaginaryShapeWrapper(this.model, id);
		}
		return result;
	}
	
	public AbstractShapeWrapper addShape(ShapeType shape) {
		int id = this.model.addShape(shape.makeShape());
		return this.getShapeWrapperById(id);
	}
	
	public void addObserver(Observer o) {
		model.addObserver(o);
	}
	
	public void deleteObserver(Observer o) {
		model.deleteObserver(o);
	}
	
	public Iterable<Triple<Color, AffineTransform, Shape>> getGraphicalColorShapeTriples() {
		ArrayList<Triple<Color, AffineTransform, Shape>> result = new ArrayList<Triple<Color, AffineTransform, Shape>>();
		for(int id: this.model.getShapeIds()) {
			AbstractShapeWrapper s = this.getShapeWrapperById(id);
			result.add(new Triple<Color, AffineTransform, Shape>(s.getColor(), s.getObjectToWorldTransform(), s.getGraphicalShape()));
		}
		
		return result;
	}
	
}
