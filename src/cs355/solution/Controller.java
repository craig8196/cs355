package cs355.solution;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Iterator;


import cs355.solution.shapes.ShapeType;
import cs355.solution.shapes.Triangle;

public class Controller implements cs355.CS355Controller, MouseListener, MouseMotionListener {
	
	public static final double TOLERANCE = 4.0;
	
	private View view = null;
	private ControllerModelWrapper model = null;
	
	private Color currentColor = new Color(128, 128, 128);
	private ShapeType currentShapeType = null;
	private int currentShapeHandle = Model.INVALID_HANDLE;
	
	// Used for creating shapes, tracking clicks, etc.
	private boolean notTriangle = true;
	private int clickCount = 0;
	private boolean mouseDown = false;
	private Point2D.Double firstClick = null;
	private Point2D.Double secondClick = null;
	
	// Used for selection, translation, and rotation.
	private boolean selecting = false;
	private Point2D.Double selectingOriginalOrigin = null;
	private Point2D.Double selectingMouseDown = null;
	private boolean selectingTranslating = false;
	private boolean selectingRotating = false;
	private double selectingOriginalAngle = 0.0;
	
	public Controller(View v, ControllerModelWrapper m) {
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
	}
	
	public Color getCurrentColor() {
		return this.currentColor;
	}
	
	@Override
	public void lineButtonHit() {
		this.currentShapeType = ShapeType.LINE;
		this.notTriangle = true;
		this.clickCount = 0;
		this.selecting = false;
	}
	
	@Override
	public void triangleButtonHit() {
		this.currentShapeType = ShapeType.TRIANGLE;
		this.notTriangle = false;
		this.clickCount = 0;
		this.selecting = false;
	}

	@Override
	public void squareButtonHit() {
		this.currentShapeType = ShapeType.SQUARE;
		this.notTriangle = true;
		this.clickCount = 0;
		this.selecting = false;
	}

	@Override
	public void rectangleButtonHit() {
		this.currentShapeType = ShapeType.RECTANGLE;
		this.notTriangle = true;
		this.clickCount = 0;
		this.selecting = false;
	}

	@Override
	public void circleButtonHit() {
		this.currentShapeType = ShapeType.CIRCLE;
		this.notTriangle = true;
		this.clickCount = 0;
		this.selecting = false;
	}

	@Override
	public void ellipseButtonHit() {
		this.currentShapeType = ShapeType.ELLIPSE;
		this.notTriangle = true;
		this.clickCount = 0;
		this.selecting = false;
	}

	@Override
	public void selectButtonHit() {
		if(this.selecting) {
			return;
		}
		this.selecting = true;
		this.currentShapeType = null;
		this.clickCount = 0;
		this.currentShapeHandle = -1;
	}

	@Override
	public void zoomInButtonHit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void zoomOutButtonHit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hScrollbarChanged(int value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void vScrollbarChanged(int value) {
		// TODO Auto-generated method stub
		
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
	@Override
	public void mouseDragged(MouseEvent e) {
		Point2D.Double p = new Point2D.Double(e.getX(), e.getY());
		if(this.selecting) {
			if(this.selectingTranslating) {
				this.model.updateShape(this.currentShapeHandle, (s)->{
					if(s instanceof Triangle) {
						
					} else {
						Point2D.Double c = this.selectingOriginalOrigin;
						Point2D.Double md = this.selectingMouseDown;
						s.setCenter(p.x - md.x + c.x, p.y - md.y + c.y);
					}
				});
			} else if(this.selectingRotating) {
				this.model.updateShape(this.currentShapeHandle, (s)->{
					AffineTransform a = s.getWorldToObjectTransform();
					Point2D.Double start = new Point2D.Double();
					Point2D.Double end = new Point2D.Double();
					a.transform(this.selectingMouseDown, start);
					a.transform(p, end);
					double angleDelta = Math.atan2(end.y, end.x) - Math.atan2(start.y, start.x);
					System.out.println("rotating");
					s.setAngle(this.selectingOriginalAngle + angleDelta);
				});
			}
		} else {
			if(this.notTriangle && this.mouseDown) {
				if(this.currentShapeHandle != Model.INVALID_HANDLE) {
					this.secondClick = p;
					this.model.setFirstTwoPoints(this.currentShapeHandle, this.firstClick, this.secondClick);
				}
			}
		}
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		Point2D.Double p = new Point2D.Double(e.getX(), e.getY());
		if(this.selecting) {
			
		} else {
			if(!this.notTriangle) {
				if(this.clickCount == 1) {
					this.secondClick = p;
					this.model.setFirstTwoPoints(this.currentShapeHandle, this.firstClick, this.secondClick);
				} else if(this.clickCount == 2) {
					this.model.setThirdPoint(this.currentShapeHandle, this.firstClick, this.secondClick, p);
				}
			}
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		Point2D.Double p = new Point2D.Double(e.getX(), e.getY());
		if(this.selecting) {
			if(this.currentShapeHandle != Model.INVALID_HANDLE &&
			   (this.model.getShapeByHandle(this.currentShapeHandle).isPointInShape(p, Controller.TOLERANCE) ||
			    this.model.hitShapeHandle(this.currentShapeHandle, p) ||
			    this.model.hitShapeRotateHandle(this.currentShapeHandle, p, this.getVisualHandleRadius()) )) {
					return;
			}
			
			int shapeHandle = this.model.hitShape(p.x, p.y);
			boolean updateView = false;
			
			if(this.currentShapeHandle != shapeHandle){
				updateView = true;
			}
			
			this.currentShapeHandle = shapeHandle;
			
			if(updateView) {
				this.view.update();
			}
		} else if(!this.notTriangle) {
			if(this.clickCount == 0) {
				this.firstClick = p;
				this.currentShapeHandle = this.model.shapeFactory(this.currentColor, this.firstClick.x, this.firstClick.y, this.currentShapeType);
			} else if(this.clickCount == 1) {
				this.secondClick = p;
				this.model.setFirstTwoPoints(this.currentShapeHandle, this.firstClick, this.secondClick);
			} else if(this.clickCount == 2) {
				this.model.setThirdPoint(this.currentShapeHandle, this.firstClick, this.secondClick, p);
			}
			this.clickCount++;
			if(this.clickCount >= 3) {
				this.clickCount = 0;
			}
		}
	}
	@Override
	public void mousePressed(MouseEvent e) {
		Point2D.Double p = new Point2D.Double(e.getX(), e.getY());
		if(this.selecting) {
			if(this.currentShapeHandle != Model.INVALID_HANDLE) {
				this.selectingMouseDown = p;
				this.selectingOriginalOrigin = this.model.getShapeByHandle(this.currentShapeHandle).getCenter();
				this.selectingOriginalAngle = this.model.getShapeByHandle(this.currentShapeHandle).getAngle();
				if(this.model.getShapeByHandle(this.currentShapeHandle).isPointInShape(p, Controller.TOLERANCE)) {
					this.selectingTranslating = true;
				} else if(this.model.hitShapeRotateHandle(this.currentShapeHandle, p, this.getVisualHandleRadius())) {
					this.selectingRotating = true;
				}
			}
		} else {
			if(this.notTriangle) {
				this.mouseDown = true;
				this.firstClick = p;
				this.currentShapeHandle = this.model.shapeFactory(this.currentColor, this.firstClick.x, this.firstClick.y, this.currentShapeType);
			}
		}
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		if(this.selecting) {
			if(this.selectingTranslating) {
				this.selectingTranslating = false;
			} else if(this.selectingRotating) {
				this.selectingRotating = false;
			}
		} else {
			if(this.mouseDown) {
				if(this.currentShapeHandle != Model.INVALID_HANDLE) {
					this.secondClick = new Point2D.Double(e.getX(), e.getY());
					this.model.setFirstTwoPoints(this.currentShapeHandle, this.firstClick, this.secondClick);
				}
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

	public boolean isSelecting() {
		return this.selecting;
	}
	
	public int getCurrentShapeHandle() {
		return this.currentShapeHandle;
	}
	
	public double getVisualHandleRadius() {
		return 10.0;
	}
	
}
