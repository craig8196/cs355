package cs355.solution;

import java.awt.Color;

public class Shape {
	private Color color = null;
	public Shape(Color c) {
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
