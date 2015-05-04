package cs355.solution.shapes;

import java.awt.Color;

public abstract class AbstractShape {
	private Color color = null;
	public AbstractShape(Color c) {
		this.color = c;
	}
	
	public Color getColor() {
		return this.color;
	}
	
	public void setColor(Color c) {
		if(c != null) {
			this.color = c;
		}
	}
}
