/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cs355.solution;

import cs355.GUIFunctions;

/**
 *
 * @author [your name here]
 */
public class CS355 
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
    	Model m = new Model();
    	ControllerModelWrapper cm = new ControllerModelWrapper(m);
    	ViewModelWrapper vm = new ViewModelWrapper(m);
    	
    	Controller c = new Controller(cm);
    	View v = new View(vm);
    	// Fill in the parameters below with your controller, view, 
    	//   mouse listener, and mouse motion listener
        GUIFunctions.createCS355Frame(c, v, c, c);
        
        GUIFunctions.refresh();        
    }
}