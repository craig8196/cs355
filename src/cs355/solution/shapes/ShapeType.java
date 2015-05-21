package cs355.solution.shapes;

public enum ShapeType {
	LINE {
		public AbstractShape makeShape() {
			return new Line();
		}
	},
	
	SQUARE {
		public AbstractShape makeShape() {
			return new Square();
		}
	},
	
	RECTANGLE {
		public AbstractShape makeShape() {
			return new Rectangle();
		}
	},
	
	CIRCLE {
		public AbstractShape makeShape() {
			return new Circle();
		}
	},
	
	ELLIPSE {
		public AbstractShape makeShape() {
			return new Ellipse();
		}
	},
	
	TRIANGLE {
		public AbstractShape makeShape() {
			return new Triangle();
		}
	},
	
	IMAGINARY;
	
	public AbstractShape makeShape() {
		return null;
	}
}
