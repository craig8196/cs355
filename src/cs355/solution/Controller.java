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
	private AbstractShapeWrapper currentShape = new ImaginaryShapeWrapper();
	
	// Used for creating shapes, tracking clicks, etc.
	private int clickCount = 0;
	private boolean mouseDown = false;
	private Point2D.Double firstPoint = null;
	private Point2D.Double secondPoint = null;
	
	// Used for selection, translation, and rotation.
	private boolean selecting = false;
	
//	// TODO move this to the wrapper classes
//	private Point2D.Double selectingOriginalOrigin = null;
//	private Point2D.Double selectingMouseDown = null;
//	private double selectingOriginalAngle = 0.0;
//	private Point2D.Double selectingLineP1 = null;
//	private Point2D.Double selectingLineP2 = null;
	
	// Zooming in and out.
	private double[] zoomLevels = { 0.25, 0.5, 1.0, 2.0, 4.0 };
	private int currentZoomLevelIndex = 2;
	private Point2D.Double viewportTopLeft = new Point2D.Double(0.0, 0.0);
	private boolean updatingZoom = false;
	
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
		this.viewportTopLeft.x = (double)value;
		if(!this.updatingZoom) {
			this.view.update();
		}
	}

	@Override
	public void vScrollbarChanged(int value) {
		this.viewportTopLeft.y = (double)value;
		if(!this.updatingZoom) {
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
//			if(this.selectingTranslating) {
//				this.currentShape.setTranslatingSecondPoint(p);
//				this.model.updateShape(this.currentShapeHandle, (s)->{
//					Point2D.Double c = this.selectingOriginalOrigin;
//					Point2D.Double md = this.selectingMouseDown;
//					Point2D.Double diff = new Point2D.Double(p.x - md.x, p.y - md.y);
//					if(s instanceof Line) {
//						Line l = (Line)s;
//						l.setFirstPoint(diff.x + this.selectingLineP1.x, diff.y + this.selectingLineP1.y);
//						l.setSecondPoint(diff.x + this.selectingLineP2.x, diff.y + this.selectingLineP2.y);
//					} else {
//						s.setCenter(diff.x + c.x, diff.y + c.y);
//					}
//				});
//			} else if(this.selectingRotating) {
//				this.model.updateShape(this.currentShapeHandle, (s)->{
//					AffineTransform a = s.getWorldToObjectTransform();
//					Point2D.Double start = new Point2D.Double();
//					Point2D.Double end = new Point2D.Double();
//					a.transform(this.selectingMouseDown, start);
//					a.transform(p, end);
//					double angleDelta = Math.atan2(end.y, end.x) - Math.atan2(start.y, start.x);
////					System.out.println("rotating");
//					s.setAngle(this.selectingOriginalAngle + angleDelta);
//				});
//			}
		} else {
			if(this.selectedShapeType != ShapeType.TRIANGLE && this.mouseDown) {
				this.secondPoint = p;
				this.currentShape.setFirstTwoPoints(this.firstPoint, this.secondPoint);
			}
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		Point2D.Double p = this.getWorldPointFromClick(e);
		if(this.selecting) {
			
		} else {
			if(this.selectedShapeType != ShapeType.TRIANGLE) {
				if(this.clickCount == 1) {
					this.secondPoint = p;
					this.currentShape.setFirstTwoPoints(this.firstPoint, this.secondPoint);
				} else if(this.clickCount == 2) {
					this.currentShape.setFirstThreePoints(this.firstPoint, this.secondPoint, p);
				}
			}
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		Point2D.Double p = this.getWorldPointFromClick(e);
		if(this.selecting) {
//			if((this.model.getShapeByHandle(this.currentShapeHandle).isPointInShape(p, Controller.TOLERANCE) ||
//			    this.model.hitShapeHandle(this.currentShapeHandle, p) ||
//			    this.model.hitShapeRotateHandle(this.currentShapeHandle, p, this.getVisualHandleRadius()) )) {
//					return;
//			}
//			
//			int shapeHandle = this.model.hitShape(p.x, p.y);
//			boolean updateView = false;
//			
//			if(this.currentShapeHandle != shapeHandle){
//				updateView = true;
//			}
//			
//			this.currentShapeHandle = shapeHandle;
//			
//			if(shapeHandle != Model.INVALID_HANDLE) {
//				this.currentColor = this.model.getColorByHandle(shapeHandle);
//				this.view.setColorSwatch(this.currentColor);
//			}
//			
//			if(updateView) {
//				this.view.update();
//			}
		} else {
			if(this.selectedShapeType == ShapeType.TRIANGLE) {
				if(this.clickCount == 0) {
					this.firstPoint = p;
					this.currentShape = this.model.addShape(this.selectedShapeType);
				} else if(this.clickCount == 1) {
					this.secondPoint = p;
					this.currentShape.setFirstTwoPoints(this.firstPoint, this.secondPoint);
				} else if(this.clickCount == 2) {
					this.currentShape.setFirstThreePoints(this.firstPoint, this.secondPoint, p);
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
//			if(this.currentShapeHandle != Model.INVALID_HANDLE) {
//				this.selectingMouseDown = p;
//				AbstractShape as = this.model.getShapeByHandle(this.currentShapeHandle);
//				this.selectingOriginalOrigin = as.getCenter();
//				this.selectingOriginalAngle = as.getAngle();
//				if(as instanceof Line) {
//					Line l = (Line)as;
//					this.selectingLineP1 = l.getFirstPoint();
//					this.selectingLineP2 = l.getSecondPoint();
//				}
//				if(this.model.getShapeByHandle(this.currentShapeHandle).isPointInShape(p, Controller.TOLERANCE)) {
//					this.selectingTranslating = true;
//				} else if(this.model.hitShapeRotateHandle(this.currentShapeHandle, p, this.getVisualHandleRadius())) {
//					this.selectingRotating = true;
//				}
//			}
		} else {
			if(this.selectedShapeType != ShapeType.TRIANGLE) {
				this.mouseDown = true;
				this.firstPoint = p;
				this.currentShape = this.model.addShape(this.selectedShapeType);
			}
		}
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		Point2D.Double p = this.getWorldPointFromClick(e);
		if(this.selecting) {
//			if(this.selectingTranslating) {
//				this.selectingTranslating = false;
//			} else if(this.selectingRotating) {
//				this.selectingRotating = false;
//			}
		} else {
			if(this.mouseDown) {
				this.secondPoint = p;
				this.currentShape.setFirstTwoPoints(this.firstPoint, this.secondPoint);
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
	
	public double getVisualHandleRadius() {
		return 10.0;
	}
	
}
