package chronoSimulator;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import chronoTimer.ChronoTimer;

/**
 * 
 * @author Riley Mahr
 * Class to start simulation object
 */

public class ChronoSimulatorDriver {
	public static void main(String[] args) {
		try {
            // Set cross-platform Java L&F (also called "Metal")
	        UIManager.setLookAndFeel(
	            UIManager.getSystemLookAndFeelClassName());
	    } 
	    catch (UnsupportedLookAndFeelException e) {
	       // handle exception
	    }
	    catch (ClassNotFoundException e) {
	       // handle exception
	    }
	    catch (InstantiationException e) {
	       // handle exception
	    }
	    catch (IllegalAccessException e) {
	       // handle exception
	    }
	
		ChronoTimerSimulator cts = new ChronoTimerSimulator();
	    cts.start();
	      
	}
	
	
}
