/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cs355.solution;

import cs355.GUIFunctions;
import java.util.logging.Level;
import java.util.logging.Logger;

import cs355.solution.Controller;

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
    	Controller c = new Controller(m);
    	View v = new View(m);
    	// Fill in the parameters below with your controller, view, 
    	//   mouse listener, and mouse motion listener
        GUIFunctions.createCS355Frame(c, v, c, c);
        
        GUIFunctions.refresh();        
    }
}