package cs355.solution;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Iterator;

import cs355.solution.shapes.Point2D;
import cs355.solution.shapes.ShapeType;

public class Controller implements cs355.CS355Controller, MouseListener, MouseMotionListener {

	
	private ControllerModelWrapper model = null;
	
	private Color currentColor = new Color(128, 128, 128);
	private ShapeType currentShapeType = null;
	private int currentShapeHandle = -1;
	
	private boolean notTriangle = true;
	private int clickCount = 0;
	// private boolean selecting = false;
	private boolean mouseDown = false;
	private Point2D firstClick = null;
	private Point2D secondClick = null;
	
	public Controller(ControllerModelWrapper m) {
		this.model = m;
	}
	
	@Override
	public void colorButtonHit(Color c) {
		if(c == null) {
			return;
		}
		this.currentColor = c;
	}
	
	@Override
	public void lineButtonHit() {
		this.currentShapeType = ShapeType.LINE;
		this.notTriangle = true;
		this.clickCount = 0;
	}
	
	@Override
	public void triangleButtonHit() {
		this.currentShapeType = ShapeType.TRIANGLE;
		this.notTriangle = false;
		this.clickCount = 0;
	}

	@Override
	public void squareButtonHit() {
		this.currentShapeType = ShapeType.SQUARE;
		this.notTriangle = true;
		this.clickCount = 0;
	}

	@Override
	public void rectangleButtonHit() {
		this.currentShapeType = ShapeType.RECTANGLE;
		this.notTriangle = true;
		this.clickCount = 0;
	}

	@Override
	public void circleButtonHit() {
		this.currentShapeType = ShapeType.CIRCLE;
		this.notTriangle = true;
		this.clickCount = 0;
	}

	@Override
	public void ellipseButtonHit() {
		this.currentShapeType = ShapeType.ELLIPSE;
		this.notTriangle = true;
		this.clickCount = 0;
	}

	@Override
	public void selectButtonHit() {
		// TODO Auto-generated method stub
		
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
		if(this.notTriangle && this.mouseDown) {
			if(this.currentShapeHandle != -1) {
				this.secondClick = new Point2D(e.getX(), e.getY());
				this.model.setFirstTwoPoints(this.currentShapeHandle, this.firstClick, this.secondClick);
			}
		}
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		if(!this.notTriangle) {
			Point2D p = new Point2D(e.getX(), e.getY());
			if(this.clickCount == 1) {
				this.secondClick = p;
				this.model.setFirstTwoPoints(this.currentShapeHandle, this.firstClick, this.secondClick);
			} else if(this.clickCount == 2) {
				this.model.setThirdPoint(this.currentShapeHandle, p);
			}
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		if(!this.notTriangle) {
			Point2D p = new Point2D(e.getX(), e.getY());
			if(this.clickCount == 0) {
				this.firstClick = p;
				this.currentShapeHandle = this.model.shapeFactory(this.currentColor, this.firstClick.x, this.firstClick.y, this.currentShapeType);
			} else if(this.clickCount == 1) {
				this.secondClick = p;
				this.model.setFirstTwoPoints(this.currentShapeHandle, this.firstClick, this.secondClick);
			} else if(this.clickCount == 2) {
				this.model.setThirdPoint(this.currentShapeHandle, p);
			}
		}
		this.clickCount++;
		if(this.clickCount >= 3) {
			this.clickCount = 0;
		}
	}
	@Override
	public void mousePressed(MouseEvent e) {
		if(this.notTriangle) {
			this.mouseDown = true;
			this.firstClick = new Point2D(e.getX(), e.getY());
			this.currentShapeHandle = this.model.shapeFactory(this.currentColor, this.firstClick.x, this.firstClick.y, this.currentShapeType);
		}
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		if(this.mouseDown) {
			if(this.currentShapeHandle != -1) {
				this.secondClick = new Point2D(e.getX(), e.getY());
				this.model.setFirstTwoPoints(this.currentShapeHandle, this.firstClick, this.secondClick);
			}
		}
		this.mouseDown = false;
	}
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	@Override
	public void mouseExited(MouseEvent e) {
	}

}
