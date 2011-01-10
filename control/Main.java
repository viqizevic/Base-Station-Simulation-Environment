
/**
 * @mainpage Base Station Simulation Environment
 * 
 * A simulation environment to observe
 * the multi-point coordination of some base stations.
 */

package control;

/**
 * The main class for starting the program.
 * @author vicky
 *
 */
public class Main {

	/**
	 * The main method calling the controller for initializing.
	 * @param args
	 */
	public static void main( String[] args ) {
		Control.getControl().initialize();
	}

}
