package cs355.solution;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Iterator;

import cs355.HouseModel;
import cs355.Point3D;
import cs355.solution.shapes.AbstractShapeWrapper;
import cs355.solution.shapes.ImaginaryShapeWrapper;
import cs355.solution.shapes.ShapeType;
import cs355.solution.shapes.Utilities;

public class Controller implements cs355.CS355Controller, MouseListener, MouseMotionListener {
	
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
	private int currentZoomLevelIndex = 0;
	private Point2D.Double viewportTopLeft = new Point2D.Double(0.0, 0.0);
	private boolean updatingZoom = false;
	
	// 3d Mode
	private boolean modelDisplay3d = false;
	private Point3D camera = new Point3D(0.0, 2.0, -40.0);
	private double xzCameraAngle = 0.0;
	private double[][] worldToCameraMatrix = new double[][]{
		{1.0, 0.0, 0.0, -this.camera.x},
		{0.0, 1.0, 0.0, -this.camera.y},
		{0.0, 0.0, 1.0, -this.camera.z},
		{0.0, 0.0, 0.0, 1.0}
	};
	private double fovY = Math.PI/3; // 60 degrees
	private double aspectRatio = 1.0; // Just to test the stretch component
	private double nearPlane = 1.0;
	private double farPlane = 1000.0;
	private double[][] clipMatrix = Utilities.new3dIdentityMatrix();
	private double[][] clipTo2dWorldMatrix = Utilities.new3dIdentityMatrix();
	
	public Controller(View v, ModelWrapper m) {
		this.model = m;
		this.view = v;
		this.currentShape = new ImaginaryShapeWrapper(m.model, Model.INVALID_ID);
		this.resetWorldToCameraMatrix();
		this.resetClipMatrix();
		this.resetClipTo2dWorldMatrix();
	}
	
	private void resetClipMatrix() {
		Utilities.zeroMatrix(this.clipMatrix);
		double zoomY = 1.0/Math.tan(this.fovY/2.0);
		double zoomX = zoomY*this.aspectRatio;
		double f = this.farPlane;
		double n = this.nearPlane;
		this.clipMatrix[0][0] = zoomX;
		this.clipMatrix[1][1] = zoomY;
		this.clipMatrix[2][2] = (f + n)/(f - n);
		this.clipMatrix[3][2] = 1.0;
		this.clipMatrix[2][3] = (-2.0*n*f)/(f - n);
	}
	
	private void resetClipTo2dWorldMatrix() {
		Utilities.zeroMatrix(this.clipTo2dWorldMatrix);
		double xScale = (ModelWrapper.WORLD_X_MAX - ModelWrapper.WORLD_X_MIN)/2.0;
		double yScale = (ModelWrapper.WORLD_Y_MAX - ModelWrapper.WORLD_Y_MIN)/2.0;
		this.clipTo2dWorldMatrix[0][0] = xScale;
		this.clipTo2dWorldMatrix[1][1] = -yScale; // Flip about x axis.
		this.clipTo2dWorldMatrix[0][3] = xScale + ModelWrapper.WORLD_X_MIN;
		this.clipTo2dWorldMatrix[1][3] = yScale + ModelWrapper.WORLD_Y_MIN;
		this.clipTo2dWorldMatrix[2][2] = 1.0;
		this.clipTo2dWorldMatrix[3][3] = 1.0;
	}
	
	public double[][] getClipMatrix() {
		return this.clipMatrix;
	}
	
	public double[][] getClipTo2dWorldMatrix() {
		return this.clipTo2dWorldMatrix;
	}
	
	public boolean is3dModeEnabled() {
		return this.modelDisplay3d;
	}
	
	public HouseModel getHouseModel() {
		return this.house;
	}
	
	public ObjectTransformation[] getHouseTransformations() {
		return this.houses;
	}
	
	public double getToleranceInWorldCoords() {
		return ModelWrapper.TOLERANCE/this.getZoomScalingFactor();
	}
	
	public double getHandleRadiusInWorldCoords() {
		return ModelWrapper.HANDLE_RADIUS/this.getZoomScalingFactor();
	}
	
	public double getZoomScalingFactor() {
		return this.zoomLevels[this.currentZoomLevelIndex];
	}
	
	public double[][] getWorldToCameraMatrix() {
		return this.worldToCameraMatrix;
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
	
	public void resetWorldToCameraMatrix() {
		Utilities.matrixMultiply(this.getWorldToCameraRotateMatrix(), this.getWorldToCameraTranslateMatrix(), this.worldToCameraMatrix);
	}
	
	public double[][] getWorldToCameraTranslateMatrix() {
		double[][] result = Utilities.new3dIdentityMatrix();
		result[0][3] = -this.camera.x;
		result[1][3] = -this.camera.y;
		result[2][3] = -this.camera.z;
		return result;
	}
	
	public double[][] getWorldToCameraRotateMatrix() {
		double[][] result = Utilities.new3dIdentityMatrix();
		double cos = Math.cos(this.xzCameraAngle);
		double sin = Math.sin(this.xzCameraAngle);
		result[0][0] = cos;
		result[2][2] = cos;
		result[0][2] = sin;
		result[2][0] = -sin;
		return result;
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
		System.out.println("toggle 3d");
		this.modelDisplay3d = !this.modelDisplay3d;
		this.view.update();
	}

	private static final double LR_INCR = 0.3;
	private static final double UD_INCR = 0.3;
	private static final double FB_INCR = 0.3;
	private static final double LR_ANGLE_INCR = (2*Math.PI)/200.0;
	private static final double TWO_PI = Math.PI*2.0;
	@Override
	public void keyPressed(Iterator<Integer> iterator) {
		if(this.is3dModeEnabled()) {
			double fbInc = 0.0; // Forward backward change.
	    	double lrInc = 0.0; // Left right change.
	    	double udInc = 0.0; // Up down change.
	    	double lrAngleInc = 0.0; // Angle change.
			while(iterator.hasNext()) {
				char key = (char)iterator.next().intValue();
				switch(key) {
					case 'W': fbInc += FB_INCR; break;
					case 'A': lrInc -= LR_INCR; break;
					case 'S': fbInc -= FB_INCR; break;
					case 'D': lrInc += LR_INCR; break;
					case 'Q': lrAngleInc += LR_ANGLE_INCR; break;
					case 'E': lrAngleInc -= LR_ANGLE_INCR; break;
					case 'R': udInc += UD_INCR; break;
					case 'F': udInc -= UD_INCR; break;
				}
			}
			if(lrInc != 0.0 || fbInc != 0.0 || udInc != 0.0 || lrAngleInc != 0.0) {
	        	double sin = Math.sin(-this.xzCameraAngle);
	        	double cos = Math.cos(-this.xzCameraAngle);
	        	this.camera.x += (fbInc*sin + lrInc*cos);
	        	this.camera.y += udInc;
	        	this.camera.z += (fbInc*cos - lrInc*sin);
	        	this.xzCameraAngle += lrAngleInc;
	        	
	        	// Wrap angles.
	        	if(this.xzCameraAngle > TWO_PI) {
	        		this.xzCameraAngle -= TWO_PI;
	        	}
	        	if(this.xzCameraAngle < -TWO_PI) {
	        		this.xzCameraAngle += TWO_PI;
	        	}
	        	
	        	// Update matrix.
	        	this.resetWorldToCameraMatrix();
	        	this.view.update();
	        }
		}
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
