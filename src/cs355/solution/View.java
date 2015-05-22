package cs355.solution;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.util.Observable;
import java.util.Observer;

import cs355.GUIFunctions;
import cs355.ViewRefresher;
import cs355.solution.shapes.AbstractShapeWrapper;


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
