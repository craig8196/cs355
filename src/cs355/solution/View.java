package cs355.solution;

import java.awt.Graphics2D;
import java.util.Observable;
import java.util.Observer;

import cs355.GUIFunctions;
import cs355.ViewRefresher;

public class View implements Observer, ViewRefresher {

	public View(Model m) {
		m.addObserver(this);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		GUIFunctions.refresh();
	}

	@Override
	public void refreshView(Graphics2D g2d) {
		// TODO Auto-generated method stub
	}
	
}
