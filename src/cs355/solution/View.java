package cs355.solution;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.util.Observable;
import java.util.Observer;

import cs355.GUIFunctions;
import cs355.ViewRefresher;


public class View implements Observer, ViewRefresher {

	private ViewModelWrapper model = null;
	
	public View(ViewModelWrapper m) {
		this.model = m;
		m.addObserver(this);
	}
	
	@Override
	public void refreshView(Graphics2D g2d) {
		for(Pair<Color, Shape> p: this.model.getGraphicalColorShapePairs()) {
			g2d.setColor(p.first);
			g2d.draw(p.second);
			g2d.fill(p.second);
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		System.out.println("Model changed.");
		GUIFunctions.refresh();
	}
}
