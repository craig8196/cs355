package cs355.solution;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Stack;

import cs355.GUIFunctions;
import cs355.Line3D;
import cs355.ViewRefresher;
import cs355.solution.shapes.AbstractShapeWrapper;
import cs355.solution.shapes.Utilities;


public class View implements Observer, ViewRefresher {

	private ModelWrapper model = null;
	private Controller controller = null;
	private boolean initialized = false;
	
	public View(ModelWrapper m) {
		this.model = m;
		m.addObserver(this);
	}
	
	public void setController(Controller c) {
		this.controller = c;
	}
	
	@Override
	public void refreshView(Graphics2D g2d) {
		if(!this.initialized) { // Set the color swatch the first time through.
			this.setColorSwatch(this.controller.getCurrentColor());
			GUIFunctions.setHScrollBarMin((int)ModelWrapper.WORLD_X_MIN);
			GUIFunctions.setHScrollBarMax((int)ModelWrapper.WORLD_X_MAX);
			GUIFunctions.setVScrollBarMin((int)ModelWrapper.WORLD_Y_MIN);
			GUIFunctions.setVScrollBarMax((int)ModelWrapper.WORLD_Y_MAX);
			this.updateScrollBars();
			this.initialized = true;
		}
		if(this.controller.isBackgroundDisplayEnabled() && this.model.hasImage()) {
			g2d.setTransform(this.controller.getWorldToViewTransform());
			BufferedImage img = this.model.getImage();
			double width = ModelWrapper.WORLD_X_MAX - ModelWrapper.WORLD_X_MIN;
			double height = ModelWrapper.WORLD_Y_MAX - ModelWrapper.WORLD_Y_MIN;
			int x = 0;
			int y = 0;
			ImageObserver observer = null;
			g2d.drawImage(img, x, y, (int)width, (int)height, observer);
		}
		float width = 1.0f/(float)this.controller.getZoomScalingFactor();
		BasicStroke lineStroke = new BasicStroke(width);
		g2d.setStroke(lineStroke);
		AffineTransform worldToView = this.controller.getWorldToViewTransform();
		for(Triple<Color, AffineTransform, Shape> p: this.model.getGraphicalColorShapeTriples()) {
			g2d.setColor(p.first);
			AffineTransform objToWorldToView = new AffineTransform(worldToView);
			objToWorldToView.concatenate(p.second);
			g2d.setTransform(objToWorldToView);
			g2d.draw(p.third);
			g2d.fill(p.third);
		}
		AbstractShapeWrapper asw = this.controller.getCurrentShape();
		if(asw.isSelected()) {
			AffineTransform objToWorldToView = new AffineTransform(worldToView);
			objToWorldToView.concatenate(asw.getObjectToWorldTransform());
			g2d.setTransform(objToWorldToView);
			g2d.setColor(asw.getSelectedColor());
			Shape outline = asw.getSelectedOutlineShape();
			if(outline != null) {
				g2d.draw(outline);
			}
			
			for(Shape s: asw.getSelectedHandleShapes(this.controller.getHandleRadiusInWorldCoords())) {
				g2d.draw(s);
			}
		}
		if(this.controller.is3dModeEnabled()) {
			g2d.setTransform(this.controller.getWorldToViewTransform());
			double[][] clipMatrix = this.controller.getClipMatrix();
			double[][] clipTo2dWorldMatrix = this.controller.getClipTo2dWorldMatrix();
			Stack<double[][]> stack = new Stack<double[][]>();
			stack.push(Utilities.matrixMultiply(clipMatrix, this.controller.getWorldToCameraMatrix()));
			for(ObjectTransformation ot: this.model.getHouseTransformations()) {
				double[][] newMatrix = Utilities.matrixMultiply(stack.peek(), ot.getTranslateMatrix());
				newMatrix = Utilities.matrixMultiply(newMatrix, ot.getRotateMatrix());
				stack.push(newMatrix);
				
				Iterator<Line3D> iter = this.model.getHouseModel().getLines();
				while(iter.hasNext()) {
					Line3D l = iter.next();
					double[][] start = new double[][]{
						{l.start.x},
						{l.start.y},
						{l.start.z},
						{1.0}
					};
					double[][] end = new double[][]{
						{l.end.x},
						{l.end.y},
						{l.end.z},
						{1.0}
					};
					// clip space points
					double[][] start2 = Utilities.matrixMultiply(stack.peek(), start);
					double[][] end2 = Utilities.matrixMultiply(stack.peek(), end);
//					Utilities.printMatrix(start2);
//					Utilities.printMatrix(end2);
					if(Utilities.showLine(start2, end2)) {
						Utilities.homogenousDivide(start2);
						Utilities.homogenousDivide(end2);
//						Utilities.printMatrix(start2);
//						Utilities.printMatrix(end2);
						start2 = Utilities.matrixMultiply(clipTo2dWorldMatrix, start2);
						end2 = Utilities.matrixMultiply(clipTo2dWorldMatrix, end2);
//						Utilities.printMatrix(start2);
//						Utilities.printMatrix(end2);
						g2d.setColor(ot.getColor());
						g2d.draw(new Line2D.Double(new Point2D.Double(start2[0][0], start2[1][0]), new Point2D.Double(end2[0][0], end2[1][0])));
					}
				}
				stack.pop();
			}
		}
	}
	
	public void updateHScrollBar() {
		double scale = this.controller.getZoomScalingFactor();
		double size = ModelWrapper.VIEW_WIDTH;
		GUIFunctions.setHScrollBarKnob((int)(size/scale));
		GUIFunctions.setHScrollBarPosit((int)(this.controller.getViewTopLeftCorner().x));
		GUIFunctions.setHScrollBarKnob((int)(size/scale));
		GUIFunctions.setHScrollBarPosit((int)(this.controller.getViewTopLeftCorner().x));
	}
	
	public void updateVScrollBar() {
		double scale = this.controller.getZoomScalingFactor();
		double size = ModelWrapper.VIEW_HEIGHT;
		GUIFunctions.setVScrollBarKnob((int)(size/scale));
		GUIFunctions.setVScrollBarPosit((int)(this.controller.getViewTopLeftCorner().y));
		GUIFunctions.setVScrollBarKnob((int)(size/scale));
		GUIFunctions.setVScrollBarPosit((int)(this.controller.getViewTopLeftCorner().y));
	}
	
	public void updateScrollBars() {
		this.updateHScrollBar();
		this.updateVScrollBar();
	}
	
	public void update() {
		GUIFunctions.refresh();
	}

	@Override
	public void update(Observable o, Object arg) {
		GUIFunctions.refresh();
	}
	
	public void setColorSwatch(Color c) {
		GUIFunctions.changeSelectedColor(c);
	}
}
