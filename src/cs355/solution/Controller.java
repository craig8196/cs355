package cs355.solution;

import static java.lang.System.out;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Controller implements cs355.CS355Controller, MouseListener, MouseMotionListener {

	
	private Model model = null;
	
	private Color currentColor = new Color(128, 128, 128);
	private ShapeType currentShapeType = null;
	private int currentShapeHandle = -1;
	
	private boolean mouseDownMouseUp = true;
	private int clickCount = 0;
	private boolean selecting = false;
	private boolean mouseDown = false;
	
	public Controller(Model m) {
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
		this.mouseDownMouseUp = true;
		this.clickCount = 0;
	}
	
	@Override
	public void triangleButtonHit() {
		this.currentShapeType = ShapeType.TRIANGLE;
		this.mouseDownMouseUp = false;
		this.clickCount = 0;
	}

	@Override
	public void squareButtonHit() {
		this.currentShapeType = ShapeType.SQUARE;
		this.mouseDownMouseUp = true;
		this.clickCount = 0;
	}

	@Override
	public void rectangleButtonHit() {
		this.currentShapeType = ShapeType.RECTANGLE;
		this.mouseDownMouseUp = true;
		this.clickCount = 0;
	}

	@Override
	public void circleButtonHit() {
		this.currentShapeType = ShapeType.CIRCLE;
		this.mouseDownMouseUp = true;
		this.clickCount = 0;
	}

	@Override
	public void ellipseButtonHit() {
		this.currentShapeType = ShapeType.ELLIPSE;
		this.mouseDownMouseUp = true;
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
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		if(this.mouseDown) {
			
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		out.println("Clicked");
		if(!this.mouseDownMouseUp) {
			if(this.clickCount == 0) {
				out.println("First Point");
			} else if(this.clickCount == 1) {
				out.println("Second Point");
			} else if(this.clickCount == 2) {
				out.println("Third Point");
			}
		}
		this.clickCount++;
		if(this.clickCount >= 3) {
			this.clickCount = 0;
		}
	}
	@Override
	public void mousePressed(MouseEvent e) {
		out.println("Pressed");
		if(this.mouseDownMouseUp) {
			this.mouseDown = true;
			double x1 = 0.0;
			double y1 = 0.0;
			this.currentShapeHandle = this.model.shapeFactory(this.currentColor, x1, y1, this.currentShapeType);
		}
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		out.println("Released");
		if(this.mouseDown) {
			if(this.currentShapeHandle != -1) {
				this.model.updateShape(this.currentShapeHandle, (s) -> { System.out.println("hi"); });
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
