package cs355.solution;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Iterator;

import cs355.solution.shapes.AbstractShapeWrapper;
import cs355.solution.shapes.ImaginaryShapeWrapper;
import cs355.solution.shapes.ShapeType;

public class Controller implements cs355.CS355Controller, MouseListener, MouseMotionListener {
	
	private View view = null;
	private ModelWrapper model = null;
	
	private Color currentColor = new Color(128, 128, 128);
	private ShapeType selectedShapeType = ShapeType.IMAGINARY;
	private AbstractShapeWrapper currentShape = null;
	
	// Used for creating shapes, tracking clicks, etc.
	private int clickCount = 0;
	private boolean mouseDown = false;
	private Point2D.Double firstPoint = null;
	private Point2D.Double secondPoint = null;
	
	// Used for selection, translation, and rotation.
	private boolean selecting = false;
	
	// Zooming in and out.
	private double[] zoomLevels = { 0.25, 0.5, 1.0, 2.0, 4.0 };
	private int currentZoomLevelIndex = 2;
	private Point2D.Double viewportTopLeft = new Point2D.Double(0.0, 0.0);
	private boolean updatingZoom = false;
	
	public double getToleranceInWorldCoords() {
		return ModelWrapper.TOLERANCE/this.getZoomScalingFactor();
	}
	
	public double getHandleRadiusInWorldCoords() {
		return ModelWrapper.HANDLE_RADIUS/this.getZoomScalingFactor();
	}
	
	public double getZoomScalingFactor() {
		return this.zoomLevels[this.currentZoomLevelIndex];
	}
	
	public AffineTransform getWorldToViewTransform() {
		double scaleFactor = this.getZoomScalingFactor();
		AffineTransform result = new AffineTransform();
		AffineTransform translate = new AffineTransform(1.0, 0.0, 0.0, 1.0, -this.viewportTopLeft.x, -this.viewportTopLeft.y);
		AffineTransform scale = new AffineTransform(scaleFactor, 0.0, 0.0, scaleFactor, 0.0, 0.0);
		result.concatenate(scale);
		result.concatenate(translate);
		return result;
	}
	
	public AffineTransform getViewToWorldTransform() {
		double scaleFactor = 1/this.getZoomScalingFactor();
		AffineTransform result = new AffineTransform();
		AffineTransform translate = new AffineTransform(1.0, 0.0, 0.0, 1.0, this.viewportTopLeft.x, this.viewportTopLeft.y);
		AffineTransform scale = new AffineTransform(scaleFactor, 0.0, 0.0, scaleFactor, 0.0, 0.0);
		result.concatenate(translate);
		result.concatenate(scale);
		return result;
	}
	
	public Point2D.Double getViewTopLeftCorner() {
		return this.viewportTopLeft;
	}
	
	public Controller(View v, ModelWrapper m) {
		this.model = m;
		this.view = v;
		this.currentShape = new ImaginaryShapeWrapper(m.model, Model.INVALID_ID);
	}
	
	@Override
	public void colorButtonHit(Color c) {
		if(c == null) {
			return;
		}
		this.currentColor = c;
		this.view.setColorSwatch(c);
		if(this.selecting) {
			this.currentShape.setColor(c);
		}
	}
	
	public Color getCurrentColor() {
		return this.currentColor;
	}
	
	@Override
	public void lineButtonHit() {
		this.selectedShapeType = ShapeType.LINE;
		this.clickCount = 0;
		this.selecting = false;
	}
	
	@Override
	public void triangleButtonHit() {
		this.selectedShapeType = ShapeType.TRIANGLE;
		this.clickCount = 0;
		this.selecting = false;
	}

	@Override
	public void squareButtonHit() {
		this.selectedShapeType = ShapeType.SQUARE;
		this.clickCount = 0;
		this.selecting = false;
	}

	@Override
	public void rectangleButtonHit() {
		this.selectedShapeType = ShapeType.RECTANGLE;
		this.clickCount = 0;
		this.selecting = false;
	}

	@Override
	public void circleButtonHit() {
		this.selectedShapeType = ShapeType.CIRCLE;
		this.clickCount = 0;
		this.selecting = false;
	}

	@Override
	public void ellipseButtonHit() {
		this.selectedShapeType = ShapeType.ELLIPSE;
		this.clickCount = 0;
		this.selecting = false;
	}

	@Override
	public void selectButtonHit() {
		if(this.selecting) {
			return;
		}
		this.clickCount = 0;
		this.selecting = true;
		this.selectedShapeType = ShapeType.IMAGINARY;
	}

	
	
	private Point2D.Double getViewPortCenter() { // In world coordinates.
		Point2D.Double result = new Point2D.Double();
		this.getViewToWorldTransform().transform(new Point2D.Double(ModelWrapper.VIEW_WIDTH/2.0, ModelWrapper.VIEW_HEIGHT/2.0), result);
		return result;
	}
	
	private void setNewViewPortTopLeftCorner(Point2D.Double c) {
		double postZoomScale = this.getZoomScalingFactor();
		double width = ModelWrapper.VIEW_WIDTH/postZoomScale;
		double height = ModelWrapper.VIEW_HEIGHT/postZoomScale;
		double halfWidth = width/2.0;
		double halfHeight = height/2.0;
		double x = c.x - halfWidth;
		double y = c.y - halfHeight;
		double xRight = c.x + halfWidth;
		double yBottom = c.y + halfHeight;
		if(x < ModelWrapper.WORLD_X_MIN) {
			x = ModelWrapper.WORLD_X_MIN;
		}
		if(y < ModelWrapper.WORLD_Y_MIN) {
			y = ModelWrapper.WORLD_Y_MIN;
		}
		if(xRight > ModelWrapper.WORLD_X_MAX) {
			x = ModelWrapper.WORLD_X_MAX - width;
		}
		if(yBottom > ModelWrapper.WORLD_Y_MAX) {
			y = ModelWrapper.WORLD_Y_MAX - height;
		}
		this.viewportTopLeft.x = x;
		this.viewportTopLeft.y = y;
	}
	
	@Override
	public void zoomOutButtonHit() {
		this.updatingZoom = true;
		if(this.currentZoomLevelIndex > 0) {
			Point2D.Double center = this.getViewPortCenter();
			this.currentZoomLevelIndex--;
			this.setNewViewPortTopLeftCorner(center);
			this.view.updateScrollBars();
			this.view.update();
		}
		this.updatingZoom = false;
	}

	@Override
	public void zoomInButtonHit() {
		this.updatingZoom = true;
		if(this.currentZoomLevelIndex < (this.zoomLevels.length - 1)) {
			Point2D.Double center = this.getViewPortCenter();
			this.currentZoomLevelIndex++;
			this.setNewViewPortTopLeftCorner(center);
			this.view.updateScrollBars();
			this.view.update();
		}
		this.updatingZoom = false;
	}

	@Override
	public void hScrollbarChanged(int value) {
		if(!this.updatingZoom) {
			this.viewportTopLeft.x = (double)value;
			this.view.update();
		}
	}

	@Override
	public void vScrollbarChanged(int value) {
		if(!this.updatingZoom) {
			this.viewportTopLeft.y = (double)value;
			this.view.update();
		}
	}

	@Override
	public void toggle3DModelDisplay() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(Iterator<Integer> iterator) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doEdgeDetection() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doSharpen() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doMedianBlur() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doUniformBlur() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doChangeContrast(int contrastAmountNum) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doChangeBrightness(int brightnessAmountNum) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doLoadImage(BufferedImage openImage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void toggleBackgroundDisplay() {
		// TODO Auto-generated method stub
		
	}
	
	private Point2D.Double getWorldPointFromClick(MouseEvent e) {
		Point2D.Double result = new Point2D.Double();
		this.getViewToWorldTransform().transform(new Point2D.Double(e.getX(), e.getY()), result);
		return result;
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		Point2D.Double p = this.getWorldPointFromClick(e);
		if(this.selecting) {
			if(this.currentShape.isSelected()) {
				this.currentShape.setMouseUp(p);
			}
		} else {
			if(this.selectedShapeType != ShapeType.TRIANGLE && this.mouseDown) {
				this.secondPoint = p;
				this.currentShape.setFirstTwoPointsWorld(this.firstPoint, this.secondPoint);
			}
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		Point2D.Double p = this.getWorldPointFromClick(e);
		if(this.selecting) {
			
		} else {
			if(this.selectedShapeType == ShapeType.TRIANGLE) {
				if(this.clickCount == 1) {
					this.secondPoint = p;
					this.currentShape.setFirstTwoPointsWorld(this.firstPoint, this.secondPoint);
				} else if(this.clickCount == 2) {
					this.currentShape.setFirstThreePointsWorld(this.firstPoint, this.secondPoint, p);
				}
			}
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		Point2D.Double p = this.getWorldPointFromClick(e);
		if(this.selecting) {
			double handleRadius = this.getHandleRadiusInWorldCoords();
			double tolerance = this.getToleranceInWorldCoords();
			
			if( // If it hit the already selected shape
				this.currentShape.isSelected() && 
				(this.currentShape.hitShape(p, tolerance) ||
				this.currentShape.hitRotateHandle(p, handleRadius) ||
				this.currentShape.hitResizeHandle(p, handleRadius))
			) {
				return;
			}
			
			this.currentShape = this.model.hitShape(p, tolerance);
			
			if(!(this.currentShape instanceof ImaginaryShapeWrapper)) { // Hit shape.
				this.currentColor = this.currentShape.getColor();
				this.view.setColorSwatch(this.currentColor);
				this.view.update();
				this.currentShape.setSelected(true);
			} else { // Clicked on not a shape.
				this.currentShape.setSelected(false);
			}
			
			this.view.update();
		} else {
			if(this.selectedShapeType == ShapeType.TRIANGLE) {
				if(this.clickCount == 0) {
					this.firstPoint = p;
					this.currentShape = this.model.addShape(this.selectedShapeType);
					this.currentShape.setColor(this.currentColor);
					this.currentShape.setFirstTwoPointsWorld(p, p);
				} else if(this.clickCount == 1) {
					this.secondPoint = p;
					this.currentShape.setFirstTwoPointsWorld(this.firstPoint, this.secondPoint);
				} else if(this.clickCount == 2) {
					this.currentShape.setFirstThreePointsWorld(this.firstPoint, this.secondPoint, p);
				}
				this.clickCount++;
				if(this.clickCount >= 3) {
					this.clickCount = 0;
				}
			}
		}
	}
	@Override
	public void mousePressed(MouseEvent e) {
		Point2D.Double p = this.getWorldPointFromClick(e);
		if(this.selecting) {
			if(this.currentShape.isSelected()) {
				this.currentShape.setMouseDown(p); // Save the current state.
				
				if(this.currentShape.hitResizeHandle(p, this.getHandleRadiusInWorldCoords())) {
					this.currentShape.setResizing(true);
				} else if(this.currentShape.hitShape(p, this.getToleranceInWorldCoords())) {
					this.currentShape.setTranslating(true);
				} else if(this.currentShape.hitRotateHandle(p, this.getHandleRadiusInWorldCoords())) {
					this.currentShape.setRotating(true);
				}
			}
		} else {
			if(this.selectedShapeType != ShapeType.TRIANGLE) {
				this.mouseDown = true;
				this.firstPoint = p;
				this.currentShape = this.model.addShape(this.selectedShapeType);
				this.currentShape.setColor(this.currentColor);
				this.currentShape.setFirstTwoPointsWorld(p, p);
			}
		}
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		Point2D.Double p = this.getWorldPointFromClick(e);
		if(this.selecting) {
			this.currentShape.resetAllSelectingFeatures();
		} else {
			if(this.mouseDown) {
				this.secondPoint = p;
				this.currentShape.setFirstTwoPointsWorld(this.firstPoint, this.secondPoint);
			}
			this.mouseDown = false;
		}
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
	}
	
	public AbstractShapeWrapper getCurrentShape() {
		return this.currentShape;
	}

}
